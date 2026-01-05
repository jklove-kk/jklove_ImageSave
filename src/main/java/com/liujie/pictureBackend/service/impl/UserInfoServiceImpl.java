package com.liujie.pictureBackend.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liujie.pictureBackend.common.DeleteRequest;
import com.liujie.pictureBackend.constans.UserConstant;
import com.liujie.pictureBackend.entity.UserInfo;
import com.liujie.pictureBackend.exception.BusinessException;
import com.liujie.pictureBackend.exception.ErrorCode;
import com.liujie.pictureBackend.exception.ThrowUtils;
import com.liujie.pictureBackend.mapper.UserInfoMapper;
import com.liujie.pictureBackend.model.dto.user.UserAddRequest;
import com.liujie.pictureBackend.model.dto.user.UserQueryRequest;
import com.liujie.pictureBackend.model.dto.user.UserUpdateRequest;
import com.liujie.pictureBackend.model.enums.UserRoleEnum;
import com.liujie.pictureBackend.model.vo.LoginUserVO;
import com.liujie.pictureBackend.model.vo.UserVo;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        userInfo.setUserNo(passWordUtils.getSnowflakeId());
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

    @Override
    public String addUser(UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest==null,ErrorCode.PARAMS_ERROR);
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userAddRequest,userInfo);
        //管理员创建给于默认密码
        String enPassword = passWordUtils.getEncryptPassword(UserConstant.DEFAULT_PASSWORD);
        userInfo.setUserPassword(enPassword);
        userInfo.setCreateTime(new Date());
        userInfo.setUserNo(passWordUtils.getSnowflakeId());
        boolean save = this.save(userInfo);
        ThrowUtils.throwIf(!save,ErrorCode.OPERATION_ERROR);
        return userInfo.getUserNo();
    }

    @Override
    public Integer deleteUser(DeleteRequest deleteRequest) {
        ThrowUtils.throwIf(deleteRequest==null,ErrorCode.PARAMS_ERROR);
        boolean b = this.removeById(deleteRequest.getId());
        return  b?1:0;
    }

    @Override
    public String updateUser(UserUpdateRequest userUpdateRequest) {
        ThrowUtils.throwIf(userUpdateRequest==null,ErrorCode.PARAMS_ERROR);
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userUpdateRequest,userInfo);
        userInfo.setUpdateTime(new Date());
        boolean b = this.updateById(userInfo);
        ThrowUtils.throwIf(!b,ErrorCode.OPERATION_ERROR);
        return userInfo.getUserNo();
    }

    @Override
    public Page<UserVo> getUserList(UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest==null,ErrorCode.PARAMS_ERROR);
        int pageSize = userQueryRequest.getPageSize();
        int page = userQueryRequest.getPage();
        Page<UserInfo> userInfoPage = this.page(new Page<>(page, pageSize), this.getQueryWrapperByUser(userQueryRequest));
        Page<UserVo> userVoPage = new Page<>(page,pageSize,userInfoPage.getRecords().size());
        userVoPage.setRecords(this.getUserVoList(userInfoPage.getRecords()));
        return userVoPage;

    }

    @Override
    public UserInfo getUser(String userNo) {
        ThrowUtils.throwIf(StrUtil.isEmpty(userNo),ErrorCode.PARAMS_ERROR);
        UserInfo user = this.getById(userNo);
        ThrowUtils.throwIf(user==null,ErrorCode.PARAMS_ERROR);
        return user;
    }

    @Override
    public UserVo getUserVoById(String userNo) {
        UserInfo user = this.getUser(userNo);
        return this.getUserVo(user);

    }


    public QueryWrapper<UserInfo> getQueryWrapperByUser(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String userNo = userQueryRequest.getUserNo();
        String userAccount = userQueryRequest.getUserAccount();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotBlank(userNo), "user_no", userNo);
        queryWrapper.eq(StrUtil.isNotBlank(userRole), "user_role", userRole);
        queryWrapper.like(StrUtil.isNotBlank(userAccount), "user_account", userAccount);
        queryWrapper.like(StrUtil.isNotBlank(userName), "user_name", userName);
        queryWrapper.like(StrUtil.isNotBlank(userProfile), "user_profile", userProfile);
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }


    /**
     * 脱敏用户信息
     * @param user
     * @return
     */
    public UserVo getUserVo(UserInfo user) {
        if (user == null) {
            return null;
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }


    /**
     * 脱敏用户列表
     * @param userList
     * @return
     */
    public List<UserVo> getUserVoList(List<UserInfo> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVo).collect(Collectors.toList());
    }




}




