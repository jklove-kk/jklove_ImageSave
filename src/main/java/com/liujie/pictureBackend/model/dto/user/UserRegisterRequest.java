package com.liujie.pictureBackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;


/**
 * 接收用户请求参数
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String checkPassword;
}
