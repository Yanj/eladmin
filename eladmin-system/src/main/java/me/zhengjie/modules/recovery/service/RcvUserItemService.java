/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.recovery.service;

import me.zhengjie.modules.recovery.domain.RcvUserItem;
import me.zhengjie.modules.recovery.service.dto.RcvItemDto;
import me.zhengjie.modules.recovery.service.dto.RcvUserDto;
import me.zhengjie.modules.recovery.service.dto.RcvUserItemDto;
import me.zhengjie.modules.recovery.service.dto.RcvUserItemQueryCriteria;
import me.zhengjie.service.dto.HisCkItemDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @website https://el-admin.vip
 * @description 服务接口
 * @date 2020-10-25
 **/
public interface RcvUserItemService {

    /**
     * 新增或者更新
     *
     * @param user       .
     * @param itemList   .
     * @param ckItemList .
     * @return .
     */
    List<RcvUserItemDto> createOrUpdate(RcvUserDto user, List<RcvItemDto> itemList, List<HisCkItemDto> ckItemList);

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(RcvUserItemQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<RcvUserItemDto>
     */
    List<RcvUserItemDto> queryAll(RcvUserItemQueryCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return RcvUserItemDto
     */
    RcvUserItemDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return RcvUserItemDto
     */
    RcvUserItemDto create(RcvUserItem resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(RcvUserItem resources);

    /**
     * 多选删除
     *
     * @param ids /
     */
    void deleteAll(Long[] ids);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<RcvUserItemDto> all, HttpServletResponse response) throws IOException;
}