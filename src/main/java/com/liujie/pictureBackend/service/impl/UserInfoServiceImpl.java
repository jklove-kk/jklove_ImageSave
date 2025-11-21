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
import com.liujie.pictureBackend.service.UserInfoService;
import com.liujie.pictureBackend.utils.common.passWordUtils;
import com.liujie.pictureBackend.utils.common.uuidUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author Administrator
 * @description 针对表【user_info(用户信息表)】的数据库操作Service实现
 * @createDate 2025-11-17 16:51:00
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {


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
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.验证账号和密码
        if(StrUtil.isEmpty(userAccount)||StrUtil.isEmpty(userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户名或密码为空");
        }
        //加密
        String encryptPassword = passWordUtils.getEncryptPassword(userPassword);

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount",userAccount);
        queryWrapper.eq("userPassword",encryptPassword);
        UserInfo userInfo = this.baseMapper.selectOne(queryWrapper);
        if(userInfo==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"账号不存在或密码错误");
        }

        //2.记录登录状态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE,userInfo);
        return getLoginUserVO(userInfo);



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
    public UserInfo getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        UserInfo currentUser = (UserInfo) userObj;
        if (currentUser == null || currentUser.getUserNo() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"未登录");
        }
        // 从数据库查询（追求性能的话可以注释，直接返回上述结果）
        String userNo = currentUser.getUserNo();
        currentUser = this.getById(userNo);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"未登录");
        }
        return currentUser;
    }

}




