package com.liujie.pictureBackend;

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
       redisService.setObject("Hello","你好", Duration.ofMinutes(10));
    }

}
