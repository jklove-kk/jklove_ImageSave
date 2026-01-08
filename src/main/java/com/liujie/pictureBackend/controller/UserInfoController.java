package com.liujie.pictureBackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liujie.pictureBackend.annotation.AuthCheck;
import com.liujie.pictureBackend.common.BaseResponse;
import com.liujie.pictureBackend.common.DeleteRequest;
import com.liujie.pictureBackend.common.ResultUtils;
import com.liujie.pictureBackend.entity.UserInfo;
import com.liujie.pictureBackend.exception.ErrorCode;
import com.liujie.pictureBackend.exception.ThrowUtils;
import com.liujie.pictureBackend.model.dto.user.*;
import com.liujie.pictureBackend.model.enums.UserRoleEnum;
import com.liujie.pictureBackend.model.vo.LoginUserVO;
import com.liujie.pictureBackend.model.vo.UserVo;
import com.liujie.pictureBackend.service.UserInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    /**
     *
     * 用户注册
     */
    @PostMapping("/register")
    public BaseResponse<String> userInfoRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String result = userInfoService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }


    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request, HttpServletResponse response) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        LoginUserVO loginUserVO = userInfoService.userLogin(userAccount, userPassword, request,response);
        return ResultUtils.success(loginUserVO);
    }


    /**
     * 获取登录信息
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        return ResultUtils.success(userInfoService.getLoginUser(request));
    }


    /**
     * 新增用户
     * @param userAddRequest
     * @return
     */
    @PostMapping("/addUser")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public BaseResponse<String> addUser(@RequestBody UserAddRequest userAddRequest ) {
        return ResultUtils.success(userInfoService.addUser(userAddRequest));
    }

    /**
     * 删除用户
     * @param deleteRequest
     * @return
     */
    @PostMapping("/deleteUser")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public BaseResponse<Integer> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        return ResultUtils.success(userInfoService.deleteUser(deleteRequest));
    }

    /**
     * 更新用户
     * @param userUpdateRequest
     * @return
     */
    @PostMapping("/updateUser")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public BaseResponse<String> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        return ResultUtils.success(userInfoService.updateUser(userUpdateRequest));
    }


    /**
     * 获取全部用户列表
     * @return
     */
    @PostMapping("/getUserList")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public BaseResponse<Page<UserVo>> getUserList(@RequestBody UserQueryRequest userQueryRequest) {
        return ResultUtils.success(userInfoService.getUserList(userQueryRequest));
    }

    /**
     * 查询用户
     * @param userNo
     * @return
     */
    @GetMapping("/getUser")
    @AuthCheck(mustRole = UserRoleEnum.ADMIN)
    public BaseResponse<UserInfo> getUser(@RequestParam(value = "userNo") String userNo) {
        return ResultUtils.success(userInfoService.getUser(userNo));
    }

    /**
     * 查询用户（脱敏）
     * @param userNo
     * @return
     */
    @GetMapping("/getUserVo")
    public BaseResponse<UserVo> getUserVo(@RequestParam(value = "userNo") String userNo) {
        return ResultUtils.success(userInfoService.getUserVoById(userNo));
    }






}

