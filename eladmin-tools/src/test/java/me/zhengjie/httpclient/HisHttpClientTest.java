package me.zhengjie.httpclient;

import me.zhengjie.domain.vo.HisCkItemVo;
import me.zhengjie.service.dto.HisCkItemDto;
import me.zhengjie.utils.HisCkInfoTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author yanjun
 * @date 2020-11-15 20:30
 */
@RunWith(value = SpringRunner.class)
@SpringBootTest
public class HisHttpClientTest {

    @Autowired
    private HisHttpClient hisHttpClient;

    @Test
    public void basic() throws Exception {
        List<HisCkItemDto> list = hisHttpClient.query(new HisCkItemVo("353045", HisCkInfoTypeEnum.MRN));
        System.out.println(list);
    }

}
