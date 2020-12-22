/**
 * eladmin
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author yanjun
 * @date 2020-11-13 15:59
 */
package me.zhengjie.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.config.HisConfig;
import me.zhengjie.domain.vo.HisCkItemVo;
import me.zhengjie.httpclient.HisHttpClient;
import me.zhengjie.service.HisService;
import me.zhengjie.service.dto.HisCkItemDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * HIS 服务
 *
 * @author yanjun
 * @date 2020-11-13 15:59
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class HisServiceImpl implements HisService {

    private final HisHttpClient hisHttpClient;

    @Override
    public List<HisCkItemDto> asyncQueryCkItemList(HisCkItemVo vo) throws Exception {
        ThreadLocal<Long> currentTime = new ThreadLocal<>();
        currentTime.set(System.currentTimeMillis());
        List<HisCkItemDto> list = hisHttpClient.query(vo);
        long useTime = System.currentTimeMillis() - currentTime.get();
        log.debug("His: 耗时: " + useTime);
        return list;
    }

}
