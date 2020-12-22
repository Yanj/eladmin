package me.zhengjie.modules.ptt.service.impl;

import lombok.AllArgsConstructor;
import me.zhengjie.modules.ptt.domain.PatientTermReserveLog;
import me.zhengjie.modules.ptt.repository.PatientTermReserveLogRepository;
import me.zhengjie.modules.ptt.service.PatientTermReserveLogService;
import me.zhengjie.modules.ptt.service.dto.PatientTermReserveLogCriteria;
import me.zhengjie.modules.ptt.service.dto.PatientTermReserveLogDto;
import me.zhengjie.modules.ptt.service.mapstruct.PatientTermReserveLogMapper;
import me.zhengjie.utils.*;
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
 * 患者套餐预约日志服务
 *
 * @author yanjun
 * @date 2020-11-28 11:17
 */
@Service
@AllArgsConstructor
public class PatientTermReserveLogServiceImpl implements PatientTermReserveLogService {

    private final PatientTermReserveLogRepository repository;
    private final PatientTermReserveLogMapper mapper;

    @Override
    public Map<String, Object> queryAll(PatientTermReserveLogCriteria criteria, Pageable pageable) {
        Page<PatientTermReserveLog> page;
        if (criteria.getPatientId() != null) {
            page = repository.findAllByPatientId(criteria.getPatientId(), pageable);
        } else if (StringUtils.isNotBlank(criteria.getPatientName())) {
            page = repository.findAllByPatientName(criteria.getPatientName(), pageable);
        } else {
            page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        }
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<PatientTermReserveLogDto> queryAll(PatientTermReserveLogCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public PatientTermReserveLogDto findById(Long id) {
        PatientTermReserveLog instance = repository.findById(id).orElseGet(PatientTermReserveLog::new);
        ValidationUtil.isNull(instance.getId(), "PatientTermReserveLog", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PatientTermReserveLogDto create(PatientTermReserveLog resources) {
        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(PatientTermReserveLog resources) {
        PatientTermReserveLog instance = repository.findById(resources.getId()).orElseGet(PatientTermReserveLog::new);
        ValidationUtil.isNull(instance.getId(), "PatientTermReserveLog", "id", resources.getId());
        instance.copy(resources);
        repository.save(instance);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            repository.deleteById(id);
        }
    }

    @Override
    public void download(List<PatientTermReserveLogDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PatientTermReserveLogDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", item.getId());
            map.put("状态", item.getStatus());
            map.put("创建人", item.getCreateBy());
            map.put("修改人", item.getUpdatedBy());
            map.put("创建时间", item.getCreateTime());
            map.put("修改时间", item.getUpdateTime());
            map.put("备注", item.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
