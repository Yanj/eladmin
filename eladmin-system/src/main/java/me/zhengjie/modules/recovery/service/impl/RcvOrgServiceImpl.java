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
import me.zhengjie.modules.recovery.domain.RcvOrg;
import me.zhengjie.modules.recovery.repository.RcvOrgRepository;
import me.zhengjie.modules.recovery.service.RcvOrgService;
import me.zhengjie.modules.recovery.service.dto.RcvOrgDto;
import me.zhengjie.modules.recovery.service.dto.RcvOrgQueryCriteria;
import me.zhengjie.modules.recovery.service.mapstruct.RcvOrgMapper;
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
public class RcvOrgServiceImpl implements RcvOrgService {

    private final RcvOrgRepository rcvOrgRepository;
    private final RcvOrgMapper rcvOrgMapper;

    @Override
    public Map<String, Object> queryAll(RcvOrgQueryCriteria criteria, Pageable pageable) {
        Page<RcvOrg> page = rcvOrgRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(rcvOrgMapper::toDto));
    }

    @Override
    public List<RcvOrgDto> queryAll(RcvOrgQueryCriteria criteria) {
        return rcvOrgMapper.toDto(rcvOrgRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public RcvOrgDto findById(Long id) {
        RcvOrg rcvOrg = rcvOrgRepository.findById(id).orElseGet(RcvOrg::new);
        ValidationUtil.isNull(rcvOrg.getId(), "RcvOrg", "id", id);
        return rcvOrgMapper.toDto(rcvOrg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RcvOrgDto create(RcvOrg resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId());
        return rcvOrgMapper.toDto(rcvOrgRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RcvOrg resources) {
        RcvOrg rcvOrg = rcvOrgRepository.findById(resources.getId()).orElseGet(RcvOrg::new);
        ValidationUtil.isNull(rcvOrg.getId(), "RcvOrg", "id", resources.getId());
        rcvOrg.copy(resources);
        rcvOrgRepository.save(rcvOrg);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rcvOrgRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RcvOrgDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RcvOrgDto rcvOrg : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("机构名称", rcvOrg.getName());
            map.put("机构地址", rcvOrg.getAddress());
            map.put("联系人", rcvOrg.getContactName());
            map.put("联系电话", rcvOrg.getContactPhone());
            map.put("状态", rcvOrg.getStatus());
            map.put("创建人", rcvOrg.getCreateBy());
            map.put("修改人", rcvOrg.getUpdatedBy());
            map.put("创建时间", rcvOrg.getCreateTime());
            map.put("修改时间", rcvOrg.getUpdateTime());
            map.put("备注", rcvOrg.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}