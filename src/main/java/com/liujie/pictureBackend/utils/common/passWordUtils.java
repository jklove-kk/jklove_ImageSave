package com.liujie.pictureBackend.utils.common;

import cn.hutool.core.util.StrUtil;
import com.liujie.pictureBackend.common.SnowFlake;
import com.liujie.pictureBackend.exception.BusinessException;
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


    public static String getSnowflakeId(){
        String string = String.valueOf(new SnowFlake().nextId());
        if(StrUtil.isEmpty(string)){
            throw new BusinessException(503,"雪花算法生成主键错误");
        }
        return string;
    }



}
