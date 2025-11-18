package com.liujie.pictureBackend.controller;

import com.liujie.pictureBackend.common.BaseResponse;
import com.liujie.pictureBackend.common.ResultUtils;
import com.liujie.pictureBackend.exception.ErrorCode;
import com.liujie.pictureBackend.exception.ThrowUtils;
import com.liujie.pictureBackend.model.dto.UserRegisterRequest;
import com.liujie.pictureBackend.service.UserInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    /**
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
}

