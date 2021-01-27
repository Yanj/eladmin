package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.yy.domain.HisLog;
import me.zhengjie.modules.yy.repository.HisLogRepository;
import me.zhengjie.modules.yy.service.HisLogService;
import me.zhengjie.modules.yy.service.dto.HisLogCriteria;
import me.zhengjie.modules.yy.service.dto.HisLogDto;
import me.zhengjie.modules.yy.service.mapstruct.HisLogMapper;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @date 2020-12-24 14:34
 */
@Service
@RequiredArgsConstructor
public class HisLogServiceImpl implements HisLogService {

    private final HisLogRepository repository;
    private final HisLogMapper mapper;

    @Override
    public List<HisLogDto> create(List<HisCkItemDto> list) {
        List<HisLog> resList = new ArrayList<>();
        if (null == list || list.isEmpty()) {
            return mapper.toDto(resList);
        }

        for (HisCkItemDto item : list) {
            HisLog log = new HisLog();
            log.setPatientId(item.getPatientId());
            log.setName(item.getName());
            log.setMobilePhone(item.getMobilePhone());
            if (null != item.getMrn()) {
                log.setMrn(item.getMrn().toString());
            }
            log.setVisitDept(item.getVisitDept());
            if (null != item.getVisitDate()) {
                log.setVisitDate(new Timestamp(item.getVisitDate().getTime()));
            }
            log.setItemCode(item.getItemCode());
            log.setItemName(item.getItemName());
            log.setPrice(item.getPrice());
            log.setAmount(item.getAmount());
            log.setUnit(item.getUnit());
            log.setCosts(item.getCosts());
            log.setActualCosts(item.getActualCosts());
            log.setPatItemId(item.getPatItemId());
            log.setVisitId(item.getVisitId());
            resList.add(repository.save(log));
        }

        return mapper.toDto(resList);
    }

    @Override
    public Map<String, Object> queryAll(HisLogCriteria criteria, Pageable pageable) {
        Page<HisLog> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<HisLogDto> queryAll(HisLogCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public HisLogDto findById(Long id) {
        HisLog instance = repository.findById(id).orElseGet(HisLog::new);
        ValidationUtil.isNull(instance.getId(), "HisLog", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public HisLogDto create(HisLog resources) {
        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(HisLog resources) {
        HisLog instance = repository.findById(resources.getId()).orElseGet(HisLog::new);
        ValidationUtil.isNull(instance.getId(), "HisLog", "id", resources.getId());
        instance.copy(resources);
        repository.save(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            repository.deleteById(id);
        }
    }

    @Override
    public void download(List<HisLogDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (HisLogDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", item.getId());
            map.put("备注", item.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
