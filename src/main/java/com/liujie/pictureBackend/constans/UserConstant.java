package com.liujie.pictureBackend.constans;

/**
 * 用户模块常量类
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "user_login";

    //  region 权限

    /**
     * 默认角色
     */
    String DEFAULT_ROLE = "user";

    /**
     * 管理员角色
     */
    String ADMIN_ROLE = "admin";

    // endregion


    //用户保存登录信息的token前缀
    String TOKEN_WEB_COOKIE = "token:web:cookie:";

    //用户登录cookie过期时间换算（一天对应的秒数）
    int TIME_SECONDS_DAY=24*60*60;
}
