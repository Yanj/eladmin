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
import me.zhengjie.modules.recovery.domain.RcvItemReserveLog;
import me.zhengjie.modules.recovery.repository.RcvItemReserveLogRepository;
import me.zhengjie.modules.recovery.service.RcvItemReserveLogService;
import me.zhengjie.modules.recovery.service.dto.RcvItemReserveLogDto;
import me.zhengjie.modules.recovery.service.dto.RcvItemReserveLogQueryCriteria;
import me.zhengjie.modules.recovery.service.mapstruct.RcvItemReserveLogMapper;
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
public class RcvItemReserveLogServiceImpl implements RcvItemReserveLogService {

    private final RcvItemReserveLogRepository rcvItemReserveLogRepository;
    private final RcvItemReserveLogMapper rcvItemReserveLogMapper;

    @Override
    public Map<String, Object> queryAll(RcvItemReserveLogQueryCriteria criteria, Pageable pageable) {
        Page<RcvItemReserveLog> page = rcvItemReserveLogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(rcvItemReserveLogMapper::toDto));
    }

    @Override
    public List<RcvItemReserveLogDto> queryAll(RcvItemReserveLogQueryCriteria criteria) {
        return rcvItemReserveLogMapper.toDto(rcvItemReserveLogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public RcvItemReserveLogDto findById(Long id) {
        RcvItemReserveLog rcvItemReserveLog = rcvItemReserveLogRepository.findById(id).orElseGet(RcvItemReserveLog::new);
        ValidationUtil.isNull(rcvItemReserveLog.getId(), "RcvItemReserveLog", "id", id);
        return rcvItemReserveLogMapper.toDto(rcvItemReserveLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RcvItemReserveLogDto create(RcvItemReserveLog resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId());
        return rcvItemReserveLogMapper.toDto(rcvItemReserveLogRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RcvItemReserveLog resources) {
        RcvItemReserveLog rcvItemReserveLog = rcvItemReserveLogRepository.findById(resources.getId()).orElseGet(RcvItemReserveLog::new);
        ValidationUtil.isNull(rcvItemReserveLog.getId(), "RcvItemReserveLog", "id", resources.getId());
        rcvItemReserveLog.copy(resources);
        rcvItemReserveLogRepository.save(rcvItemReserveLog);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rcvItemReserveLogRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RcvItemReserveLogDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RcvItemReserveLogDto rcvItemReserveLog : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("变更内容", rcvItemReserveLog.getContent());
            map.put("之前状态", rcvItemReserveLog.getBeforeStatus());
            map.put("现在状态", rcvItemReserveLog.getAfterStatus());
            map.put("创建人", rcvItemReserveLog.getCreateBy());
            map.put("修改人", rcvItemReserveLog.getUpdatedBy());
            map.put("创建时间", rcvItemReserveLog.getCreateTime());
            map.put("修改时间", rcvItemReserveLog.getUpdateTime());
            map.put("备注", rcvItemReserveLog.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}