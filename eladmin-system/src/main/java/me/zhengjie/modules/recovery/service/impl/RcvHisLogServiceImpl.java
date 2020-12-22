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
import me.zhengjie.modules.recovery.domain.RcvHisLog;
import me.zhengjie.modules.recovery.repository.RcvHisLogRepository;
import me.zhengjie.modules.recovery.service.RcvHisLogService;
import me.zhengjie.modules.recovery.service.dto.RcvHisLogDto;
import me.zhengjie.modules.recovery.service.dto.RcvHisLogQueryCriteria;
import me.zhengjie.modules.recovery.service.mapstruct.RcvHisLogMapper;
import me.zhengjie.service.dto.HisCkItemDto;
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
import java.util.*;

/**
 * @author yanjun
 * @website https://el-admin.vip
 * @description 服务实现
 * @date 2020-10-25
 **/
@Service
@RequiredArgsConstructor
public class RcvHisLogServiceImpl implements RcvHisLogService {

    private final RcvHisLogRepository rcvHisLogRepository;
    private final RcvHisLogMapper rcvHisLogMapper;

    @Transactional
    @Override
    public List<RcvHisLogDto> createOrUpdate(List<HisCkItemDto> ckItemList) {
        if (null == ckItemList || ckItemList.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Map<Long, RcvHisLog> logMap = new HashMap<>();
        for (HisCkItemDto ckItem : ckItemList) {
            // 忽略空数据
            if (null == ckItem || null == ckItem.getPatItemId()) continue;
            // 忽略已添加过的数据
            if (logMap.containsKey(ckItem.getPatItemId())) continue;

            RcvHisLog logInfo = rcvHisLogRepository.findById(ckItem.getPatItemId()).orElseGet(RcvHisLog::new);
            if (logInfo.getPatItemId() == null) {
                logInfo = RcvHisLog.create(ckItem);
                logInfo = rcvHisLogRepository.save(logInfo);
            }
            logMap.put(logInfo.getPatItemId(), logInfo);
        }

        return rcvHisLogMapper.toDto(new ArrayList<>(logMap.values()));
    }

    @Override
    public Map<String, Object> queryAll(RcvHisLogQueryCriteria criteria, Pageable pageable) {
        Page<RcvHisLog> page = rcvHisLogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(rcvHisLogMapper::toDto));
    }

    @Override
    public List<RcvHisLogDto> queryAll(RcvHisLogQueryCriteria criteria) {
        return rcvHisLogMapper.toDto(rcvHisLogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public RcvHisLogDto findById(Long patItemId) {
        RcvHisLog rcvHisLog = rcvHisLogRepository.findById(patItemId).orElseGet(RcvHisLog::new);
        ValidationUtil.isNull(rcvHisLog.getPatItemId(), "RcvHisLog", "patItemId", patItemId);
        return rcvHisLogMapper.toDto(rcvHisLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RcvHisLogDto create(RcvHisLog resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setPatItemId(snowflake.nextId());
        return rcvHisLogMapper.toDto(rcvHisLogRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RcvHisLog resources) {
        RcvHisLog rcvHisLog = rcvHisLogRepository.findById(resources.getPatItemId()).orElseGet(RcvHisLog::new);
        ValidationUtil.isNull(rcvHisLog.getPatItemId(), "RcvHisLog", "id", resources.getPatItemId());
        rcvHisLog.copy(resources);
        rcvHisLogRepository.save(rcvHisLog);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long patItemId : ids) {
            rcvHisLogRepository.deleteById(patItemId);
        }
    }

    @Override
    public void download(List<RcvHisLogDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RcvHisLogDto rcvHisLog : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("就诊ID", rcvHisLog.getVisitId());
            map.put("患者ID", rcvHisLog.getPatientId());
            map.put("患者姓名", rcvHisLog.getName());
            map.put("患者电话", rcvHisLog.getMobilePhone());
            map.put("患者档案编号", rcvHisLog.getMrn());
            map.put("就诊科室", rcvHisLog.getVisitDept());
            map.put("就诊日期", rcvHisLog.getVisitDate());
            map.put("项目编码", rcvHisLog.getItemCode());
            map.put("项目名称", rcvHisLog.getItemName());
            map.put("单价", rcvHisLog.getPrice());
            map.put("数量", rcvHisLog.getAmount());
            map.put("单位", rcvHisLog.getUnit());
            map.put("应收金额", rcvHisLog.getCosts());
            map.put("实收金额", rcvHisLog.getActualCosts());
            map.put("创建人", rcvHisLog.getCreateBy());
            map.put("修改人", rcvHisLog.getUpdatedBy());
            map.put("创建时间", rcvHisLog.getCreateTime());
            map.put("修改时间", rcvHisLog.getUpdateTime());
            map.put("备注", rcvHisLog.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}