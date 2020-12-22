/**
 * eladmin
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author yanjun
 * @date 2020-11-13 15:16
 */
package com.test1;

import com.google.gson.Gson;
import me.zhengjie.domain.vo.HisCkItemVo;
import me.zhengjie.service.dto.HisCkItemDto;
import me.zhengjie.utils.HisCkInfoTypeEnum;
import me.zhengjie.utils.HisUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

/**
 * @author yanjun
 * @date 2020-11-13 15:16
 */
public class Dom4jTest {

    public static void main(String[] args) throws Exception {
        HisCkItemVo vo = new HisCkItemVo();
        vo.setPatientInfo("353045");
        vo.setInfoType(HisCkInfoTypeEnum.MRN);
        String queryString = HisUtil.getQueryString(vo);
        System.out.println(queryString);

        String content = readFile("/Users/yanjun/Downloads/eladmin/test.xml");
        System.out.println(content);

        List<HisCkItemDto> list = HisUtil.parseCkItemList(content);
        System.out.println(list);
        System.out.println(new Gson().toJson(list));
    }

    private static String readFile(String path) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        StringBuilder out = new StringBuilder();
        char[] buffer = new char[1024 * 3];
        int len;
        while ((len = reader.read(buffer)) != -1) {
            out.append(buffer, 0, len);
        }
        reader.close();
        return out.toString();
    }


}
