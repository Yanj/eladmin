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
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.recovery.domain.RcvUserItem;
import me.zhengjie.modules.recovery.repository.RcvUserItemRepository;
import me.zhengjie.modules.recovery.service.RcvUserItemService;
import me.zhengjie.modules.recovery.service.dto.RcvItemDto;
import me.zhengjie.modules.recovery.service.dto.RcvUserDto;
import me.zhengjie.modules.recovery.service.dto.RcvUserItemDto;
import me.zhengjie.modules.recovery.service.dto.RcvUserItemQueryCriteria;
import me.zhengjie.modules.recovery.service.mapstruct.RcvUserItemMapper;
import me.zhengjie.service.dto.HisCkItemDto;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @website https://el-admin.vip
 * @description 服务实现
 * @date 2020-10-25
 **/
@Service
@Slf4j
@RequiredArgsConstructor
public class RcvUserItemServiceImpl implements RcvUserItemService {

    private final RcvUserItemRepository rcvUserItemRepository;
    private final RcvUserItemMapper rcvUserItemMapper;

    @Override
    public List<RcvUserItemDto> createOrUpdate(RcvUserDto user, List<RcvItemDto> itemList, List<HisCkItemDto> ckItemList) {
        log.debug("createOrUpdate: ->: %s, %s, %s", user, itemList, ckItemList);
        if (null == user) {
            throw new IllegalArgumentException();
        }
        if (null == itemList || itemList.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (null == ckItemList || ckItemList.isEmpty()) {
            throw new IllegalArgumentException();
        }

        List<RcvUserItem> list = new ArrayList<>(itemList.size());
        for (int i = 0; i < itemList.size() && i < ckItemList.size(); i++) {
            RcvItemDto item = itemList.get(i);
            HisCkItemDto ckItem = ckItemList.get(i);

            RcvUserItem userItem = rcvUserItemRepository.findFirstByUserIdAndItemId(user.getId(), item.getId()).orElseGet(RcvUserItem::new);
            if (userItem.getId() == null) {
                Snowflake snowflake = IdUtil.createSnowflake(1, 1);
                userItem.setId(snowflake.nextId());
                userItem.setUserId(user.getId());
                userItem.setItemId(item.getId());
                userItem.setItemCode(item.getCode());
                userItem.setItemName(item.getName());
                userItem.setItemPrice(item.getPrice());
                userItem.setItemTimes(item.getTimes());
                userItem.setItemUnit(item.getUnit());
                userItem.setItemAmount(item.getAmount());
                userItem.setTimes(item.getTimes());

                userItem.setAmount(ckItem.getActualCosts());

                userItem = rcvUserItemRepository.save(userItem);
            }

            list.add(userItem);
        }

        List<RcvUserItemDto> res = rcvUserItemMapper.toDto(list);
        log.debug("createOrUpdate: <-: %s", res);

        return res;
    }

    @Override
    public Map<String, Object> queryAll(RcvUserItemQueryCriteria criteria, Pageable pageable) {
        Page<RcvUserItem> page = rcvUserItemRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(rcvUserItemMapper::toDto));
    }

    @Override
    public List<RcvUserItemDto> queryAll(RcvUserItemQueryCriteria criteria) {
        return rcvUserItemMapper.toDto(rcvUserItemRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public RcvUserItemDto findById(Long id) {
        RcvUserItem rcvUserItem = rcvUserItemRepository.findById(id).orElseGet(RcvUserItem::new);
        ValidationUtil.isNull(rcvUserItem.getId(), "RcvUserItem", "id", id);
        return rcvUserItemMapper.toDto(rcvUserItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RcvUserItemDto create(RcvUserItem resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId());
        return rcvUserItemMapper.toDto(rcvUserItemRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RcvUserItem resources) {
        RcvUserItem rcvUserItem = rcvUserItemRepository.findById(resources.getId()).orElseGet(RcvUserItem::new);
        ValidationUtil.isNull(rcvUserItem.getId(), "RcvUserItem", "id", resources.getId());
        rcvUserItem.copy(resources);
        rcvUserItemRepository.save(rcvUserItem);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rcvUserItemRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RcvUserItemDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RcvUserItemDto rcvUserItem : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户ID", rcvUserItem.getUserId());
            map.put("套餐ID", rcvUserItem.getItemId());
            map.put("套餐编码", rcvUserItem.getItemCode());
            map.put("套餐名称", rcvUserItem.getItemName());
            map.put("套餐单价", rcvUserItem.getItemPrice());
            map.put("套餐次数", rcvUserItem.getItemTimes());
            map.put("套餐单位", rcvUserItem.getItemUnit());
            map.put("套餐总价", rcvUserItem.getItemAmount());
            map.put("实际支付", rcvUserItem.getAmount());
            map.put("剩余次数", rcvUserItem.getTimes());
            map.put("上次预约机构", rcvUserItem.getOrgId());
            map.put("上次预约部门", rcvUserItem.getDeptId());
            map.put("上次预约日期", rcvUserItem.getReserveDate());
            map.put("机构变更次数", rcvUserItem.getOrgChangeCount());
            map.put("状态", rcvUserItem.getStatus());
            map.put("创建人", rcvUserItem.getCreateBy());
            map.put("修改人", rcvUserItem.getUpdatedBy());
            map.put("创建时间", rcvUserItem.getCreateTime());
            map.put("修改时间", rcvUserItem.getUpdateTime());
            map.put("备注", rcvUserItem.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}