package com.liujie.pictureBackend;

import com.liujie.pictureBackend.common.SnowFlake;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PictureBackendApplicationTests {

    @Test
    public void main() {
        SnowFlake snowFlake = new SnowFlake();
        for (int i = 0; i < 10; i++) {
            System.out.println(snowFlake.nextId()+"次数"+i);
        }
    }

}
