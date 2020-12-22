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
import me.zhengjie.modules.recovery.domain.RcvUserNotification;
import me.zhengjie.modules.recovery.repository.RcvUserNotificationRepository;
import me.zhengjie.modules.recovery.service.RcvUserNotificationService;
import me.zhengjie.modules.recovery.service.dto.RcvUserNotificationDto;
import me.zhengjie.modules.recovery.service.dto.RcvUserNotificationQueryCriteria;
import me.zhengjie.modules.recovery.service.mapstruct.RcvUserNotificationMapper;
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
public class RcvUserNotificationServiceImpl implements RcvUserNotificationService {

    private final RcvUserNotificationRepository rcvUserNotificationRepository;
    private final RcvUserNotificationMapper rcvUserNotificationMapper;

    @Override
    public Map<String, Object> queryAll(RcvUserNotificationQueryCriteria criteria, Pageable pageable) {
        Page<RcvUserNotification> page = rcvUserNotificationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(rcvUserNotificationMapper::toDto));
    }

    @Override
    public List<RcvUserNotificationDto> queryAll(RcvUserNotificationQueryCriteria criteria) {
        return rcvUserNotificationMapper.toDto(rcvUserNotificationRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public RcvUserNotificationDto findById(Long id) {
        RcvUserNotification rcvUserNotification = rcvUserNotificationRepository.findById(id).orElseGet(RcvUserNotification::new);
        ValidationUtil.isNull(rcvUserNotification.getId(), "RcvUserNotification", "id", id);
        return rcvUserNotificationMapper.toDto(rcvUserNotification);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RcvUserNotificationDto create(RcvUserNotification resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId());
        return rcvUserNotificationMapper.toDto(rcvUserNotificationRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RcvUserNotification resources) {
        RcvUserNotification rcvUserNotification = rcvUserNotificationRepository.findById(resources.getId()).orElseGet(RcvUserNotification::new);
        ValidationUtil.isNull(rcvUserNotification.getId(), "RcvUserNotification", "id", resources.getId());
        rcvUserNotification.copy(resources);
        rcvUserNotificationRepository.save(rcvUserNotification);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rcvUserNotificationRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RcvUserNotificationDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RcvUserNotificationDto rcvUserNotification : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户id", rcvUserNotification.getUserId());
            map.put("机构id", rcvUserNotification.getOrgId());
            map.put("部门id", rcvUserNotification.getDeptId());
            map.put("内容", rcvUserNotification.getContent());
            map.put("状态", rcvUserNotification.getStatus());
            map.put("创建人", rcvUserNotification.getCreateBy());
            map.put("修改人", rcvUserNotification.getUpdatedBy());
            map.put("创建时间", rcvUserNotification.getCreateTime());
            map.put("修改时间", rcvUserNotification.getUpdateTime());
            map.put("备注", rcvUserNotification.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}