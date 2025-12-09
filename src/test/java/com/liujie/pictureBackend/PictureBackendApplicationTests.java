package com.liujie.pictureBackend;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.liujie.pictureBackend.redis.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.Duration;


@SpringBootTest
class PictureBackendApplicationTests {

    @Resource
    private RedisService redisService;

    @Test
    public void main() {

        JSONObject entries = new JSONObject();
        entries.put("key1", "value1");
        entries.put("key2", "value2");
        JSONArray put = new JSONArray().put(entries);
        JSONObject entries2 = new JSONObject();
        entries2.put("key3", "value3");
        entries2.put("key4", "value4");
        put.add(entries2);
        redisService.setObject("Hello",put, Duration.ofMinutes(10));
        Object hello = redisService.getObject("Hello");
        System.out.println(hello);
    }

}
