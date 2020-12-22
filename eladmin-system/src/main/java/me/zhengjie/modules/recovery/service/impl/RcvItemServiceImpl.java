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
package me.zhengjie.modules.recovery.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.recovery.domain.RcvItem;
import me.zhengjie.modules.recovery.domain.RcvItemResourceType;
import me.zhengjie.modules.recovery.domain.RcvItemTimeSetting;
import me.zhengjie.modules.recovery.repository.RcvItemRepository;
import me.zhengjie.modules.recovery.service.RcvItemResourceTypeService;
import me.zhengjie.modules.recovery.service.RcvItemService;
import me.zhengjie.modules.recovery.service.RcvItemTimeSettingService;
import me.zhengjie.modules.recovery.service.dto.RcvItemDto;
import me.zhengjie.modules.recovery.service.dto.RcvItemQueryCriteria;
import me.zhengjie.modules.recovery.service.mapstruct.RcvItemMapper;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.service.dto.HisCkItemDto;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author yanjun
 * @website https://el-admin.vip
 * @description 服务实现
 * @date 2020-10-25
 **/
@Service
@RequiredArgsConstructor
public class RcvItemServiceImpl implements RcvItemService {

    private final RcvItemRepository rcvItemRepository;
    private final RcvItemMapper rcvItemMapper;

    private final RcvItemResourceTypeService rcvItemResourceTypeService;
    private final RcvItemTimeSettingService rcvItemTimeSettingService;

    @Transactional
    @Override
    public List<RcvItemDto> createOrUpdate(Dept userDept, List<HisCkItemDto> ckItemList) {
        if (null == ckItemList || ckItemList.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Map<String, RcvItemDto> itemDtoMap = new HashMap<>();
        for (HisCkItemDto ckItem : ckItemList) {
            // 忽略空数据
            if (null == ckItem || StringUtils.isEmpty(ckItem.getItemCode())) continue;
            // 忽略已添加的数据
            if (itemDtoMap.containsKey(ckItem.getItemCode())) continue;

            RcvItem item = rcvItemRepository.findFirstByCode(ckItem.getItemCode()).orElseGet(RcvItem::new);
            if (item.getId() == null) {
                Snowflake snowflake = IdUtil.createSnowflake(1, 1);
                item.setId(snowflake.nextId());
                item.setCode(ckItem.getItemCode());
                item.setName(ckItem.getItemName());
                item.setPrice(ckItem.getPrice());
                item.setTimes(ckItem.getAmount().intValue());
                item.setUnit(ckItem.getUnit());
                item.setAmount(ckItem.getCosts());
                item = rcvItemRepository.save(item);

                // 更新套餐资源类型
                RcvItemResourceType rcvItemResourceType = new RcvItemResourceType();
                rcvItemResourceType.setItem(item);
                rcvItemResourceType.setDept(userDept);
                rcvItemResourceTypeService.create(rcvItemResourceType);

                // 更新套餐时长
                RcvItemTimeSetting itemTimeSetting = new RcvItemTimeSetting();
                itemTimeSetting.setItem(item);
                itemTimeSetting.setDept(userDept);
                itemTimeSetting.setTimeAmount(0); // 未限制
                rcvItemTimeSettingService.create(itemTimeSetting);
            }

            // 保存 CkItem
            RcvItemDto dto = rcvItemMapper.toDto(item);
            dto.setCkItem(ckItem);
            itemDtoMap.put(item.getCode(), dto);
        }

        return new ArrayList<>(itemDtoMap.values());
    }

    @Override
    public Map<String, Object> queryAll(RcvItemQueryCriteria criteria, Pageable pageable) {
        Page<RcvItem> page = rcvItemRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(rcvItemMapper::toDto));
    }

    @Override
    public List<RcvItemDto> queryAll(RcvItemQueryCriteria criteria) {
        return rcvItemMapper.toDto(rcvItemRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public RcvItemDto findById(Long id) {
        RcvItem rcvItem = rcvItemRepository.findById(id).orElseGet(RcvItem::new);
        ValidationUtil.isNull(rcvItem.getId(), "RcvItem", "id", id);
        return rcvItemMapper.toDto(rcvItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RcvItemDto create(RcvItem resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId());
        return rcvItemMapper.toDto(rcvItemRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RcvItem resources) {
        RcvItem rcvItem = rcvItemRepository.findById(resources.getId()).orElseGet(RcvItem::new);
        ValidationUtil.isNull(rcvItem.getId(), "RcvItem", "id", resources.getId());
        rcvItem.copy(resources);
        rcvItemRepository.save(rcvItem);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rcvItemRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RcvItemDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RcvItemDto rcvItem : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("套餐编码", rcvItem.getCode());
            map.put("套餐名称", rcvItem.getName());
            map.put("单价", rcvItem.getPrice());
            map.put("次数", rcvItem.getTimes());
            map.put("单位", rcvItem.getUnit());
            map.put("总价", rcvItem.getAmount());
            map.put("状态", rcvItem.getStatus());
            map.put("创建人", rcvItem.getCreateBy());
            map.put("修改人", rcvItem.getUpdatedBy());
            map.put("创建时间", rcvItem.getCreateTime());
            map.put("修改时间", rcvItem.getUpdateTime());
            map.put("备注", rcvItem.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}