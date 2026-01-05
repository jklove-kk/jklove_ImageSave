package com.liujie.pictureBackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liujie.pictureBackend.common.DeleteRequest;
import com.liujie.pictureBackend.entity.UserInfo;
import com.liujie.pictureBackend.model.dto.user.UserAddRequest;
import com.liujie.pictureBackend.model.dto.user.UserQueryRequest;
import com.liujie.pictureBackend.model.dto.user.UserUpdateRequest;
import com.liujie.pictureBackend.model.vo.LoginUserVO;
import com.liujie.pictureBackend.model.vo.UserVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @author Administrator
* @description 针对表【user_info(用户信息表)】的数据库操作Service
* @createDate 2025-11-17 16:51:00
*/
public interface UserInfoService extends IService<UserInfo> {


    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    String userRegister(String userAccount, String userPassword, String checkPassword);


    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request, HttpServletResponse response);


    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(UserInfo user);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    LoginUserVO getLoginUser(HttpServletRequest request);


    /**
     * 新增用户
     * @param userAddRequest
     * @return
     */
    String addUser(UserAddRequest userAddRequest);

    Integer deleteUser(DeleteRequest deleteRequest);

    String updateUser(UserUpdateRequest userUpdateRequest);

    Page<UserVo> getUserList(UserQueryRequest userQueryRequest);

    UserInfo getUser(String userNo);

    UserVo getUserVoById(String userNo);
}
