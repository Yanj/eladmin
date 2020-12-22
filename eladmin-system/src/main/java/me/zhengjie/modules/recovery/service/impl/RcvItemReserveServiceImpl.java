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
import me.zhengjie.modules.recovery.domain.RcvItemReserve;
import me.zhengjie.modules.recovery.domain.RcvItemReserveLog;
import me.zhengjie.modules.recovery.repository.RcvItemReserveRepository;
import me.zhengjie.modules.recovery.service.RcvItemReserveLogService;
import me.zhengjie.modules.recovery.service.RcvItemReserveService;
import me.zhengjie.modules.recovery.service.dto.RcvItemReserveDto;
import me.zhengjie.modules.recovery.service.dto.RcvItemReserveQueryCriteria;
import me.zhengjie.modules.recovery.service.mapstruct.RcvItemReserveMapper;
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
public class RcvItemReserveServiceImpl implements RcvItemReserveService {

    private final RcvItemReserveRepository rcvItemReserveRepository;
    private final RcvItemReserveMapper rcvItemReserveMapper;
    private final RcvItemReserveLogService rcvItemReserveLogService;

    @Override
    public Map<String, Object> queryAll(RcvItemReserveQueryCriteria criteria, Pageable pageable) {
        Page<RcvItemReserve> page = rcvItemReserveRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(rcvItemReserveMapper::toDto));
    }

    @Override
    public List<RcvItemReserveDto> queryAll(RcvItemReserveQueryCriteria criteria) {
        return rcvItemReserveMapper.toDto(rcvItemReserveRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public RcvItemReserveDto findById(Long id) {
        RcvItemReserve rcvItemReserve = rcvItemReserveRepository.findById(id).orElseGet(RcvItemReserve::new);
        ValidationUtil.isNull(rcvItemReserve.getId(), "RcvItemReserve", "id", id);
        return rcvItemReserveMapper.toDto(rcvItemReserve);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RcvItemReserveDto create(RcvItemReserve resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId());

        // TODO 生成 code
        resources.setCode(resources.getId().toString());

        RcvItemReserve itemReserve = rcvItemReserveRepository.save(resources);

        // 更新 log
        RcvItemReserveLog log = new RcvItemReserveLog();
        log.setContent("新增预约");
        log.setReserve(itemReserve);
        log.setAfterStatus(itemReserve.getStatus());
        rcvItemReserveLogService.create(log);

        return rcvItemReserveMapper.toDto(itemReserve);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RcvItemReserve resources) {
        RcvItemReserve rcvItemReserve = rcvItemReserveRepository.findById(resources.getId()).orElseGet(RcvItemReserve::new);
        ValidationUtil.isNull(rcvItemReserve.getId(), "RcvItemReserve", "id", resources.getId());

        // 更新前的状态
        String oldStatus = rcvItemReserve.getStatus();

        // 更新
        rcvItemReserve.copy(resources);
        rcvItemReserveRepository.save(rcvItemReserve);

        // 更新 log
        RcvItemReserveLog log = new RcvItemReserveLog();
        log.setContent("更新预约信息");
        log.setReserve(rcvItemReserve);
        log.setBeforeStatus(oldStatus);
        log.setAfterStatus(rcvItemReserve.getStatus());
        rcvItemReserveLogService.create(log);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            rcvItemReserveRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<RcvItemReserveDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RcvItemReserveDto rcvItemReserve : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("唯一编码", rcvItemReserve.getCode());
            map.put("预约日期", rcvItemReserve.getDate());
            map.put("开始时间", rcvItemReserve.getBeginTime());
            map.put("结束时间", rcvItemReserve.getEndTime());
            map.put("时长", rcvItemReserve.getTimeAmount());
            map.put("实际开始时间", rcvItemReserve.getActualBeginTime());
            map.put("实际结束时间", rcvItemReserve.getActualEndTime());
            map.put("状态", rcvItemReserve.getStatus());
            map.put("创建人", rcvItemReserve.getCreateBy());
            map.put("修改人", rcvItemReserve.getUpdatedBy());
            map.put("创建时间", rcvItemReserve.getCreateTime());
            map.put("修改时间", rcvItemReserve.getUpdateTime());
            map.put("备注", rcvItemReserve.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}