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
import me.zhengjie.modules.recovery.domain.RcvPrinterSetting;
import me.zhengjie.modules.recovery.repository.RcvPrinterSettingRepository;
import me.zhengjie.modules.recovery.service.RcvPrinterSettingService;
import me.zhengjie.modules.recovery.service.dto.RcvPrinterSettingDto;
import me.zhengjie.modules.recovery.service.dto.RcvPrinterSettingQueryCriteria;
import me.zhengjie.modules.recovery.service.mapstruct.RcvPrinterSettingMapper;
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
public class RcvPrinterSettingServiceImpl implements RcvPrinterSettingService {

    private final RcvPrinterSettingRepository rcvPrinterSettingRepository;
    private final RcvPrinterSettingMapper rcvPrinterSettingMapper;

    @Override
    public Map<String, Object> queryAll(RcvPrinterSettingQueryCriteria criteria, Pageable pageable) {
        Page<RcvPrinterSetting> page = rcvPrinterSettingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(rcvPrinterSettingMapper::toDto));
    }

    @Override
    public List<RcvPrinterSettingDto> queryAll(RcvPrinterSettingQueryCriteria criteria) {
        return rcvPrinterSettingMapper.toDto(rcvPrinterSettingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public RcvPrinterSettingDto findById(Long id) {
        RcvPrinterSetting rcvPrinterSetting = rcvPrinterSettingRepository.findById(id).orElseGet(RcvPrinterSetting::new);
        ValidationUtil.isNull(rcvPrinterSetting.getId(), "RcvPrinterSetting", "id", id);
        return rcvPrinterSettingMapper.toDto(rcvPrinterSetting);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RcvPrinterSettingDto create(RcvPrinterSetting resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId());
        return rcvPrinterSettingMapper.toDto(rcvPrinterSettingRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RcvPrinterSetting resources) {
        RcvPrinterSetting rcvPrinterSetting = rcvPrinterSettingRepository.findById(resources.getId()).orElseGet(RcvPrinterSetting::new);
        ValidationUtil.isNull(rcvPrinterSetting.getId(), "RcvPrinterSetting", "id", resources.getId());
        rcvPrinterSetting.copy(resources);
        rcvPrinterSettingRepository.save(rcvPrinterSetting);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rcvPrinterSettingRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RcvPrinterSettingDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RcvPrinterSettingDto rcvPrinterSetting : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("名称", rcvPrinterSetting.getName());
            map.put("地址", rcvPrinterSetting.getAddress());
            map.put("协议", rcvPrinterSetting.getProtocol());
            map.put("机构id", rcvPrinterSetting.getOrgId());
            map.put("部门id", rcvPrinterSetting.getDeptId());
            map.put("状态", rcvPrinterSetting.getStatus());
            map.put("创建人", rcvPrinterSetting.getCreateBy());
            map.put("修改人", rcvPrinterSetting.getUpdatedBy());
            map.put("创建时间", rcvPrinterSetting.getCreateTime());
            map.put("修改时间", rcvPrinterSetting.getUpdateTime());
            map.put("备注", rcvPrinterSetting.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}