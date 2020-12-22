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
import me.zhengjie.modules.recovery.domain.RcvReceiptSetting;
import me.zhengjie.modules.recovery.repository.RcvReceiptSettingRepository;
import me.zhengjie.modules.recovery.service.RcvReceiptSettingService;
import me.zhengjie.modules.recovery.service.dto.RcvReceiptSettingDto;
import me.zhengjie.modules.recovery.service.dto.RcvReceiptSettingQueryCriteria;
import me.zhengjie.modules.recovery.service.mapstruct.RcvReceiptSettingMapper;
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
@RequiredArgsConstructor
public class RcvReceiptSettingServiceImpl implements RcvReceiptSettingService {

    private final RcvReceiptSettingRepository rcvReceiptSettingRepository;
    private final RcvReceiptSettingMapper rcvReceiptSettingMapper;

    @Override
    public Map<String, Object> queryAll(RcvReceiptSettingQueryCriteria criteria, Pageable pageable) {
        Page<RcvReceiptSetting> page = rcvReceiptSettingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(rcvReceiptSettingMapper::toDto));
    }

    @Override
    public List<RcvReceiptSettingDto> queryAll(RcvReceiptSettingQueryCriteria criteria) {
        return rcvReceiptSettingMapper.toDto(rcvReceiptSettingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public RcvReceiptSettingDto findById(Long id) {
        RcvReceiptSetting rcvReceiptSetting = rcvReceiptSettingRepository.findById(id).orElseGet(RcvReceiptSetting::new);
        ValidationUtil.isNull(rcvReceiptSetting.getId(), "RcvReceiptSetting", "id", id);
        return rcvReceiptSettingMapper.toDto(rcvReceiptSetting);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RcvReceiptSettingDto create(RcvReceiptSetting resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId());
        return rcvReceiptSettingMapper.toDto(rcvReceiptSettingRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RcvReceiptSetting resources) {
        RcvReceiptSetting rcvReceiptSetting = rcvReceiptSettingRepository.findById(resources.getId()).orElseGet(RcvReceiptSetting::new);
        ValidationUtil.isNull(rcvReceiptSetting.getId(), "RcvReceiptSetting", "id", resources.getId());
        rcvReceiptSetting.copy(resources);
        rcvReceiptSettingRepository.save(rcvReceiptSetting);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rcvReceiptSettingRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RcvReceiptSettingDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RcvReceiptSettingDto rcvReceiptSetting : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("标题", rcvReceiptSetting.getTitle());
            map.put("机构id", rcvReceiptSetting.getOrgId());
            map.put("部门id", rcvReceiptSetting.getDeptId());
            map.put("状态", rcvReceiptSetting.getStatus());
            map.put("创建人", rcvReceiptSetting.getCreateBy());
            map.put("修改人", rcvReceiptSetting.getUpdatedBy());
            map.put("创建时间", rcvReceiptSetting.getCreateTime());
            map.put("修改时间", rcvReceiptSetting.getUpdateTime());
            map.put("备注", rcvReceiptSetting.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}