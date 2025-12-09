package com.liujie.pictureBackend.redis;

import com.liujie.pictureBackend.constans.UserConstant;
import com.liujie.pictureBackend.model.vo.LoginUserVO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

@Service
public class RedisService<V> {

    @Resource
    private RedisTemplate<String, V> redisTemplate;

    @Resource
    private RedisUtils redisUtils;

    /**
     * 设置对象值
     */
    public void setObject(String key, V value, Duration timeout) {
        redisTemplate.opsForValue().set(key, value, timeout);
    }

    /**
     * 获取对象值
     */
    public V getObject(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    /**
     * 删除键
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 设置过期时间
     */
    public Boolean expire(String key, Duration timeout) {
        return redisTemplate.expire(key, timeout);
    }

    /**
     * 检查键是否存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }


    /**
     * 保存cookie
     * @param response
     * @param token
     */
    public void saveToken2Cookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(UserConstant.TOKEN_WEB_COOKIE, token);
        //-1会话级别 单位秒
        cookie.setMaxAge(UserConstant.TIME_SECONDS_DAY * 7);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 从cookie中获取登录的用户信息
     * @return
     */
    public LoginUserVO getTokenInfoFromCookie() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = getTokenFromCookie(request);
        if (token == null) {
            return null;
        }
        return (LoginUserVO) getObject(UserConstant.TOKEN_WEB_COOKIE+token);
    }

    /**
     * 从cookie中获取token
     * @param request
     * @return
     */
    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase(UserConstant.TOKEN_WEB_COOKIE)) {
                return cookie.getValue();
            }
        }
        return null;
    }


    /**
     * 清空cookie
     * @param response
     */
    public void cleanCookie(HttpServletResponse response) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(UserConstant.TOKEN_WEB_COOKIE)) {
                //清除token
                this.cleanToken(cookie.getValue());
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
                break;
            }
        }
    }

    private void cleanToken(String value) {
        redisUtils.delete(UserConstant.TOKEN_WEB_COOKIE+value);
    }


}