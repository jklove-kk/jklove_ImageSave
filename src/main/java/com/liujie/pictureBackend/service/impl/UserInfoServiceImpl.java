package com.liujie.pictureBackend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liujie.pictureBackend.common.SnowFlake;
import com.liujie.pictureBackend.constans.UserConstant;
import com.liujie.pictureBackend.entity.UserInfo;
import com.liujie.pictureBackend.exception.BusinessException;
import com.liujie.pictureBackend.exception.ErrorCode;
import com.liujie.pictureBackend.mapper.UserInfoMapper;
import com.liujie.pictureBackend.model.enums.UserRoleEnum;
import com.liujie.pictureBackend.model.vo.LoginUserVO;
import com.liujie.pictureBackend.redis.RedisService;
import com.liujie.pictureBackend.service.UserInfoService;
import com.liujie.pictureBackend.utils.common.passWordUtils;
import com.liujie.pictureBackend.utils.common.uuidUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

/**
 * @author Administrator
 * @description 针对表【user_info(用户信息表)】的数据库操作Service实现
 * @createDate 2025-11-17 16:51:00
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private RedisService redisService;

    /**
     * 用户注册
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return
     */
    @Override
    public String userRegister(String userAccount, String userPassword, String checkPassword) {

        //注册流程
        //1.参数规范校验
        if(StrUtil.isEmpty(userAccount)||StrUtil.isEmpty(userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名或密码为空");
        }

        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码确认错误");
        }

        //账号查重
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        Long count = this.baseMapper.selectCount(queryWrapper);
        if(count>0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名重复");
        }

        //TODO 自定义密码规则检验

        //2.注册成功信息保存

        //密码加密
        String encryptPassword= passWordUtils.getEncryptPassword(userPassword);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserNo(String.valueOf(new SnowFlake().nextId()));
        userInfo.setUserAccount(userAccount);
        userInfo.setUserPassword(encryptPassword);
        userInfo.setCreateTime(new Date());
        userInfo.setUserRole(UserRoleEnum.USER.getValue());
        userInfo.setUserName(uuidUtils.getUuid(10));
        if(!this.save(userInfo)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"用户信息注册失败，数据库错误");
        }

        return userInfo.getUserNo();
    }


    /**
     * 用户登录
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return
     */
    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request, HttpServletResponse response) {
        //1.验证账号和密码
        if(StrUtil.isEmpty(userAccount)||StrUtil.isEmpty(userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名或密码为空");
        }
        //加密
        String encryptPassword = passWordUtils.getEncryptPassword(userPassword);

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        queryWrapper.eq("user_password",encryptPassword);
        UserInfo userInfo = this.baseMapper.selectOne(queryWrapper);
        if(userInfo==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"账号不存在或密码错误");
        }

        //清除原来的缓存信息
        Cookie[] cookies = request.getCookies();
        if (cookies != null&&cookies.length>0) {
            String token = null;
            for (Cookie cookie : cookies) {
                if (UserConstant.TOKEN_WEB_COOKIE.equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
            if (!StrUtil.isEmpty(token)) {
                redisService.cleanToken(token);
            }
        }



        //2.记录登录状态
        //request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE,userInfo);
        //记录cookie和缓存
        String token= UUID.randomUUID().toString();
        redisService.saveToken2Cookie(response,token);
        //存入缓存
        LoginUserVO loginUserVO = getLoginUserVO(userInfo);
        loginUserVO.setToken(token);
        loginUserVO.setExpireAt(UserConstant.TIME_MILLIS_SECONDS_DAY_7);
        redisService.saveTokenInfo(loginUserVO);
        return loginUserVO;



    }

    @Override
    public LoginUserVO getLoginUserVO(UserInfo user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }


    @Override
    public LoginUserVO getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        LoginUserVO tokenInfo = redisService.getTokenInfoFromCookie();
        return tokenInfo;
    }

}




