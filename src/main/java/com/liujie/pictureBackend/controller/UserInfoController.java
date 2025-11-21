package com.liujie.pictureBackend.controller;

import com.liujie.pictureBackend.common.BaseResponse;
import com.liujie.pictureBackend.common.ResultUtils;
import com.liujie.pictureBackend.entity.UserInfo;
import com.liujie.pictureBackend.exception.ErrorCode;
import com.liujie.pictureBackend.exception.ThrowUtils;
import com.liujie.pictureBackend.model.dto.user.UserLoginRequest;
import com.liujie.pictureBackend.model.dto.user.UserRegisterRequest;
import com.liujie.pictureBackend.model.vo.LoginUserVO;
import com.liujie.pictureBackend.service.UserInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        LoginUserVO loginUserVO = userInfoService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }


    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        UserInfo user = userInfoService.getLoginUser(request);
        return ResultUtils.success(userInfoService.getLoginUserVO(user));
    }




}

