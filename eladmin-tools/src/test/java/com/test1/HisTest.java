/**
 * eladmin
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author yanjun
 * @date 2020-11-13 16:37
 */
package com.test1;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.vo.HisCkItemVo;
import me.zhengjie.service.dto.HisCkItemDto;
import me.zhengjie.utils.HisCkInfoTypeEnum;
import me.zhengjie.utils.HisUtil;
import okhttp3.OkHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yanjun
 * @date 2020-11-13 16:37
 */
public class HisTest {

    public static void main(String[] args) throws Exception {
        HisCkItemVo vo = new HisCkItemVo();
        vo.setPatientInfo("353045");
        vo.setInfoType(HisCkInfoTypeEnum.MRN);
        String queryString = HisUtil.getQueryString(vo);

        CloseableHttpClient client = HttpClients.createDefault();

        URI uri = new URIBuilder("http://171.221.244.91:8080/Web_kbj.asmx/GetCkItem")
                .addParameter("xmlpara", queryString)
                .build();
        HttpGet get = new HttpGet(uri);

        List<HisCkItemDto> list = null;
        CloseableHttpResponse response = client.execute(get);
        try {
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            System.out.println("========================");
            System.out.println(content);
            content = content.replace("&lt;", "<").replace("&gt;", ">");
            System.out.println(content);
            System.out.println("========================");
            list = HisUtil.parseCkItemList(content);
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }

        System.out.println(list);
    }

}
