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
import me.zhengjie.modules.recovery.domain.RcvResource;
import me.zhengjie.modules.recovery.repository.RcvResourceRepository;
import me.zhengjie.modules.recovery.service.RcvResourceService;
import me.zhengjie.modules.recovery.service.RcvResourceTypeService;
import me.zhengjie.modules.recovery.service.dto.RcvResourceDto;
import me.zhengjie.modules.recovery.service.dto.RcvResourceQueryCriteria;
import me.zhengjie.modules.recovery.service.dto.RcvResourceTypeDto;
import me.zhengjie.modules.recovery.service.mapstruct.RcvResourceMapper;
import me.zhengjie.modules.system.domain.Dept;
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
public class RcvResourceServiceImpl implements RcvResourceService {

    private final RcvResourceRepository rcvResourceRepository;
    private final RcvResourceMapper rcvResourceMapper;
    private final RcvResourceTypeService rcvResourceTypeService;

    @Override
    public Map<String, Object> queryAll(RcvResourceQueryCriteria criteria, Pageable pageable) {
        Page<RcvResource> page = rcvResourceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(rcvResourceMapper::toDto));
    }

    @Override
    public List<RcvResourceDto> queryAll(RcvResourceQueryCriteria criteria) {
        return rcvResourceMapper.toDto(rcvResourceRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public RcvResourceDto findById(Long id) {
        RcvResource rcvResource = rcvResourceRepository.findById(id).orElseGet(RcvResource::new);
        ValidationUtil.isNull(rcvResource.getId(), "RcvResource", "id", id);
        return rcvResourceMapper.toDto(rcvResource);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RcvResourceDto create(RcvResource resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId());

        // 更新组织
        updateOrg(resources);

        return rcvResourceMapper.toDto(rcvResourceRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RcvResource resources) {
        RcvResource rcvResource = rcvResourceRepository.findById(resources.getId()).orElseGet(RcvResource::new);
        ValidationUtil.isNull(rcvResource.getId(), "RcvResource", "id", resources.getId());
        rcvResource.copy(resources);

        // 更新组织
        updateOrg(rcvResource);

        rcvResourceRepository.save(rcvResource);
    }

    // 更新组织和部门
    private void updateOrg(RcvResource resources) {
        // 从类型中获取组织和部门, 并填入
        if (null != resources && null != resources.getType() && null != resources.getType().getId()) {
            RcvResourceTypeDto resourceType = rcvResourceTypeService.findById(resources.getType().getId());
            if (null != resourceType) {
                if (null != resourceType.getDept()) { // 部门
                    if (null == resources.getDept()) {
                        resources.setDept(new Dept());
                    }
                    resources.getDept().setId(resourceType.getDept().getId());
                }
                if (null != resourceType.getOrg()) { // 组织
                    if (null == resources.getOrg()) {
                        resources.setOrg(new Dept());
                    }
                    resources.getOrg().setId(resourceType.getOrg().getId());
                }
            }
        }
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rcvResourceRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RcvResourceDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RcvResourceDto rcvResource : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("编码", rcvResource.getCode());
            map.put("名称", rcvResource.getName());
            map.put("状态", rcvResource.getStatus());
            map.put("创建人", rcvResource.getCreateBy());
            map.put("修改人", rcvResource.getUpdatedBy());
            map.put("创建时间", rcvResource.getCreateTime());
            map.put("修改时间", rcvResource.getUpdateTime());
            map.put("备注", rcvResource.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}