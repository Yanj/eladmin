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
import me.zhengjie.modules.recovery.domain.RcvResourceType;
import me.zhengjie.modules.recovery.repository.RcvResourceTypeRepository;
import me.zhengjie.modules.recovery.service.RcvResourceTypeService;
import me.zhengjie.modules.recovery.service.dto.RcvResourceTypeDto;
import me.zhengjie.modules.recovery.service.dto.RcvResourceTypeQueryCriteria;
import me.zhengjie.modules.recovery.service.mapstruct.RcvResourceTypeMapper;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.modules.system.service.dto.DeptDto;
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
public class RcvResourceTypeServiceImpl implements RcvResourceTypeService {

    private final RcvResourceTypeRepository rcvResourceTypeRepository;
    private final RcvResourceTypeMapper rcvResourceTypeMapper;
    private final DeptService deptService;

    @Override
    public Map<String, Object> queryAll(RcvResourceTypeQueryCriteria criteria, Pageable pageable) {
        Page<RcvResourceType> page = rcvResourceTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(rcvResourceTypeMapper::toDto));
    }

    @Override
    public List<RcvResourceTypeDto> queryAll(RcvResourceTypeQueryCriteria criteria) {
        return rcvResourceTypeMapper.toDto(rcvResourceTypeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public RcvResourceTypeDto findById(Long id) {
        RcvResourceType rcvResourceType = rcvResourceTypeRepository.findById(id).orElseGet(RcvResourceType::new);
        ValidationUtil.isNull(rcvResourceType.getId(), "RcvResourceType", "id", id);
        return rcvResourceTypeMapper.toDto(rcvResourceType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RcvResourceTypeDto create(RcvResourceType resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId());

        // 更新组织 id
        updateOrgId(resources);

        return rcvResourceTypeMapper.toDto(rcvResourceTypeRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RcvResourceType resources) {
        RcvResourceType rcvResourceType = rcvResourceTypeRepository.findById(resources.getId()).orElseGet(RcvResourceType::new);
        ValidationUtil.isNull(rcvResourceType.getId(), "RcvResourceType", "id", resources.getId());
        rcvResourceType.copy(resources);

        // 更新组织 id
        updateOrgId(rcvResourceType);

        rcvResourceTypeRepository.save(rcvResourceType);
    }

    // 更新组织 id
    private void updateOrgId(RcvResourceType resources) {
        // 从部门中获取上级部门, 然后填入组织
        if (null != resources && null != resources.getDept() && null != resources.getDept().getId()) {
            DeptDto org = deptService.findById(resources.getDept().getId());
            if (null != org && null != org.getPid()) {
                if (null == resources.getOrg()) {
                    resources.setOrg(new Dept());
                }
                resources.getOrg().setId(org.getPid());
            }
        }
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rcvResourceTypeRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RcvResourceTypeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RcvResourceTypeDto rcvResourceType : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("名称", rcvResourceType.getName());
            map.put("状态", rcvResourceType.getStatus());
            map.put("创建人", rcvResourceType.getCreateBy());
            map.put("修改人", rcvResourceType.getUpdatedBy());
            map.put("创建时间", rcvResourceType.getCreateTime());
            map.put("修改时间", rcvResourceType.getUpdateTime());
            map.put("备注", rcvResourceType.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}