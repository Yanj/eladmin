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
import me.zhengjie.modules.recovery.domain.RcvDept;
import me.zhengjie.modules.recovery.repository.RcvDeptRepository;
import me.zhengjie.modules.recovery.service.RcvDeptService;
import me.zhengjie.modules.recovery.service.dto.RcvDeptDto;
import me.zhengjie.modules.recovery.service.dto.RcvDeptQueryCriteria;
import me.zhengjie.modules.recovery.service.mapstruct.RcvDeptMapper;
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
public class RcvDeptServiceImpl implements RcvDeptService {

    private final RcvDeptRepository rcvDeptRepository;
    private final RcvDeptMapper rcvDeptMapper;

    @Override
    public Map<String, Object> queryAll(RcvDeptQueryCriteria criteria, Pageable pageable) {
        Page<RcvDept> page = rcvDeptRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(rcvDeptMapper::toDto));
    }

    @Override
    public List<RcvDeptDto> queryAll(RcvDeptQueryCriteria criteria) {
        return rcvDeptMapper.toDto(rcvDeptRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public RcvDeptDto findById(Long id) {
        RcvDept rcvDept = rcvDeptRepository.findById(id).orElseGet(RcvDept::new);
        ValidationUtil.isNull(rcvDept.getId(), "RcvDept", "id", id);
        return rcvDeptMapper.toDto(rcvDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RcvDeptDto create(RcvDept resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId());
        return rcvDeptMapper.toDto(rcvDeptRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RcvDept resources) {
        RcvDept rcvDept = rcvDeptRepository.findById(resources.getId()).orElseGet(RcvDept::new);
        ValidationUtil.isNull(rcvDept.getId(), "RcvDept", "id", resources.getId());
        rcvDept.copy(resources);
        rcvDeptRepository.save(rcvDept);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rcvDeptRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RcvDeptDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RcvDeptDto rcvDept : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("机构id", rcvDept.getOrgId());
            map.put("名称", rcvDept.getName());
            map.put("上级部门", rcvDept.getParentId());
            map.put("状态", rcvDept.getStatus());
            map.put("创建人", rcvDept.getCreateBy());
            map.put("修改人", rcvDept.getUpdatedBy());
            map.put("创建时间", rcvDept.getCreateTime());
            map.put("修改时间", rcvDept.getUpdateTime());
            map.put("备注", rcvDept.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}