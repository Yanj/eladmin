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
import me.zhengjie.modules.recovery.domain.RcvUserCustomColSetting;
import me.zhengjie.modules.recovery.repository.RcvUserCustomColSettingRepository;
import me.zhengjie.modules.recovery.service.RcvUserCustomColSettingService;
import me.zhengjie.modules.recovery.service.dto.RcvUserCustomColSettingDto;
import me.zhengjie.modules.recovery.service.dto.RcvUserCustomColSettingQueryCriteria;
import me.zhengjie.modules.recovery.service.mapstruct.RcvUserCustomColSettingMapper;
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
public class RcvUserCustomColSettingServiceImpl implements RcvUserCustomColSettingService {

    private final RcvUserCustomColSettingRepository rcvUserCustomColSettingRepository;
    private final RcvUserCustomColSettingMapper rcvUserCustomColSettingMapper;

    @Override
    public Map<String, Object> queryAll(RcvUserCustomColSettingQueryCriteria criteria, Pageable pageable) {
        Page<RcvUserCustomColSetting> page = rcvUserCustomColSettingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(rcvUserCustomColSettingMapper::toDto));
    }

    @Override
    public List<RcvUserCustomColSettingDto> queryAll(RcvUserCustomColSettingQueryCriteria criteria) {
        return rcvUserCustomColSettingMapper.toDto(rcvUserCustomColSettingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public RcvUserCustomColSettingDto findById(Long id) {
        RcvUserCustomColSetting rcvUserCustomColSetting = rcvUserCustomColSettingRepository.findById(id).orElseGet(RcvUserCustomColSetting::new);
        ValidationUtil.isNull(rcvUserCustomColSetting.getId(), "RcvUserCustomColSetting", "id", id);
        return rcvUserCustomColSettingMapper.toDto(rcvUserCustomColSetting);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RcvUserCustomColSettingDto create(RcvUserCustomColSetting resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId());
        return rcvUserCustomColSettingMapper.toDto(rcvUserCustomColSettingRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RcvUserCustomColSetting resources) {
        RcvUserCustomColSetting rcvUserCustomColSetting = rcvUserCustomColSettingRepository.findById(resources.getId()).orElseGet(RcvUserCustomColSetting::new);
        ValidationUtil.isNull(rcvUserCustomColSetting.getId(), "RcvUserCustomColSetting", "id", resources.getId());
        rcvUserCustomColSetting.copy(resources);
        rcvUserCustomColSettingRepository.save(rcvUserCustomColSetting);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rcvUserCustomColSettingRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RcvUserCustomColSettingDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RcvUserCustomColSettingDto rcvUserCustomColSetting : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("机构id", rcvUserCustomColSetting.getOrgId());
            map.put("部门id", rcvUserCustomColSetting.getDeptId());
            map.put("列名称", rcvUserCustomColSetting.getName());
            map.put("列描述", rcvUserCustomColSetting.getDescription());
            map.put("字典id", rcvUserCustomColSetting.getDictId());
            map.put("字典名称", rcvUserCustomColSetting.getDictName());
            map.put("字典描述", rcvUserCustomColSetting.getDictDescription());
            map.put("状态", rcvUserCustomColSetting.getStatus());
            map.put("创建人", rcvUserCustomColSetting.getCreateBy());
            map.put("修改人", rcvUserCustomColSetting.getUpdatedBy());
            map.put("创建时间", rcvUserCustomColSetting.getCreateTime());
            map.put("修改时间", rcvUserCustomColSetting.getUpdateTime());
            map.put("备注", rcvUserCustomColSetting.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}