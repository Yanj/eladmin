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
import me.zhengjie.modules.recovery.domain.RcvItemResourceType;
import me.zhengjie.modules.recovery.repository.RcvItemResourceTypeRepository;
import me.zhengjie.modules.recovery.service.RcvItemResourceTypeService;
import me.zhengjie.modules.recovery.service.dto.RcvItemResourceTypeDto;
import me.zhengjie.modules.recovery.service.dto.RcvItemResourceTypeQueryCriteria;
import me.zhengjie.modules.recovery.service.mapstruct.RcvItemResourceTypeMapper;
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
public class RcvItemResourceTypeServiceImpl implements RcvItemResourceTypeService {

    private final RcvItemResourceTypeRepository rcvItemResourceTypeRepository;
    private final RcvItemResourceTypeMapper rcvItemResourceTypeMapper;

    @Override
    public Map<String, Object> queryAll(RcvItemResourceTypeQueryCriteria criteria, Pageable pageable) {
        Page<RcvItemResourceType> page = rcvItemResourceTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(rcvItemResourceTypeMapper::toDto));
    }

    @Override
    public List<RcvItemResourceTypeDto> queryAll(RcvItemResourceTypeQueryCriteria criteria) {
        return rcvItemResourceTypeMapper.toDto(rcvItemResourceTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public RcvItemResourceTypeDto findById(Long id) {
        RcvItemResourceType rcvItemResourceType = rcvItemResourceTypeRepository.findById(id).orElseGet(RcvItemResourceType::new);
        ValidationUtil.isNull(rcvItemResourceType.getId(), "RcvItemResourceType", "id", id);
        return rcvItemResourceTypeMapper.toDto(rcvItemResourceType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RcvItemResourceTypeDto create(RcvItemResourceType resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId());
        return rcvItemResourceTypeMapper.toDto(rcvItemResourceTypeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RcvItemResourceType resources) {
        RcvItemResourceType rcvItemResourceType = rcvItemResourceTypeRepository.findById(resources.getId()).orElseGet(RcvItemResourceType::new);
        ValidationUtil.isNull(rcvItemResourceType.getId(), "RcvItemResourceType", "id", resources.getId());
        rcvItemResourceType.copy(resources);
        rcvItemResourceTypeRepository.save(rcvItemResourceType);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rcvItemResourceTypeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RcvItemResourceTypeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RcvItemResourceTypeDto rcvItemResourceType : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("状态", rcvItemResourceType.getStatus());
            map.put("创建人", rcvItemResourceType.getCreateBy());
            map.put("修改人", rcvItemResourceType.getUpdatedBy());
            map.put("创建时间", rcvItemResourceType.getCreateTime());
            map.put("修改时间", rcvItemResourceType.getUpdateTime());
            map.put("备注", rcvItemResourceType.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}