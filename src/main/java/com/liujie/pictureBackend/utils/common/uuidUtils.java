package com.liujie.pictureBackend.utils.common;

import cn.hutool.core.util.RandomUtil;

public class uuidUtils {

    /**
     * 生成指定位数的uuid
     * @return
     */
    public static String getUuid(int length){
        return RandomUtil.randomString(length);
    }
}
