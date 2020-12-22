package me.zhengjie.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author yanjun
 * @date 2020-11-15 09:09
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyApplication.class)
public class HisConfigTest {

    @Autowired
    HisConfig hisConfig;

    @Test
    public void testDemo() throws Exception {
        System.out.println(hisConfig);
        assertEquals("http", hisConfig.getProtocol());
        assertEquals("171.221.244.91", hisConfig.getHost());
        assertEquals(8080, hisConfig.getPort());
        assertEquals("http://171.221.244.91:8080/Web_kbj.asmx", hisConfig.getBaseUrl());
        assertEquals("http://171.221.244.91:8080/Web_kbj.asmx/GetCkItem", hisConfig.getOpUrl());
    }

}
