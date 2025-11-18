package com.liujie.pictureBackend.utils.common;

import org.springframework.util.DigestUtils;

public class passWordUtils {

    /**
     * 密码加密
     * @param password
     * @return
     */
    public static String getEncryptPassword(String password) {
        // 盐值，混淆密码
        final String SALT = "liujie_";
        return DigestUtils.md5DigestAsHex((SALT + password).getBytes());
    }



}
