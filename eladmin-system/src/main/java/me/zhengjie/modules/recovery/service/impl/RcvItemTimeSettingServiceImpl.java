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
import me.zhengjie.modules.recovery.domain.RcvItemTimeSetting;
import me.zhengjie.modules.recovery.repository.RcvItemTimeSettingRepository;
import me.zhengjie.modules.recovery.service.RcvItemTimeSettingService;
import me.zhengjie.modules.recovery.service.dto.RcvItemTimeSettingDto;
import me.zhengjie.modules.recovery.service.dto.RcvItemTimeSettingQueryCriteria;
import me.zhengjie.modules.recovery.service.mapstruct.RcvItemTimeSettingMapper;
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
public class RcvItemTimeSettingServiceImpl implements RcvItemTimeSettingService {

    private final RcvItemTimeSettingRepository rcvItemTimeSettingRepository;
    private final RcvItemTimeSettingMapper rcvItemTimeSettingMapper;

    @Override
    public Map<String, Object> queryAll(RcvItemTimeSettingQueryCriteria criteria, Pageable pageable) {
        Page<RcvItemTimeSetting> page = rcvItemTimeSettingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(rcvItemTimeSettingMapper::toDto));
    }

    @Override
    public List<RcvItemTimeSettingDto> queryAll(RcvItemTimeSettingQueryCriteria criteria) {
        return rcvItemTimeSettingMapper.toDto(rcvItemTimeSettingRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public RcvItemTimeSettingDto findById(Long id) {
        RcvItemTimeSetting rcvItemTimeSetting = rcvItemTimeSettingRepository.findById(id).orElseGet(RcvItemTimeSetting::new);
        ValidationUtil.isNull(rcvItemTimeSetting.getId(), "RcvItemTimeSetting", "id", id);
        return rcvItemTimeSettingMapper.toDto(rcvItemTimeSetting);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RcvItemTimeSettingDto create(RcvItemTimeSetting resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId());
        return rcvItemTimeSettingMapper.toDto(rcvItemTimeSettingRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RcvItemTimeSetting resources) {
        RcvItemTimeSetting rcvItemTimeSetting = rcvItemTimeSettingRepository.findById(resources.getId()).orElseGet(RcvItemTimeSetting::new);
        ValidationUtil.isNull(rcvItemTimeSetting.getId(), "RcvItemTimeSetting", "id", resources.getId());
        rcvItemTimeSetting.copy(resources);
        rcvItemTimeSettingRepository.save(rcvItemTimeSetting);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rcvItemTimeSettingRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RcvItemTimeSettingDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RcvItemTimeSettingDto rcvItemTimeSetting : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("套餐时长", rcvItemTimeSetting.getTimeAmount());
            map.put("机构id", rcvItemTimeSetting.getOrgId());
            map.put("状态", rcvItemTimeSetting.getStatus());
            map.put("创建人", rcvItemTimeSetting.getCreateBy());
            map.put("修改人", rcvItemTimeSetting.getUpdatedBy());
            map.put("创建时间", rcvItemTimeSetting.getCreateTime());
            map.put("修改时间", rcvItemTimeSetting.getUpdateTime());
            map.put("备注", rcvItemTimeSetting.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}