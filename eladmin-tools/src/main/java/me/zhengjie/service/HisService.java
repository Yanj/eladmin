/**
 * eladmin
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author yanjun
 * @date 2020-11-13 15:55
 */
package me.zhengjie.service;

import me.zhengjie.domain.vo.HisCkItemVo;
import me.zhengjie.service.dto.HisCkItemDto;

import java.util.List;

/**
 * HIS 服务
 *
 * @author yanjun
 * @date 2020-11-13 15:55
 */
public interface HisService {

    /**
     * 远程查询患者信息
     *
     * @param vo .
     * @return .
     */
    List<HisCkItemDto> asyncQueryCkItemList(HisCkItemVo vo) throws Exception;

}
