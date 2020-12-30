package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.yy.domain.PatientTermLog;
import me.zhengjie.modules.yy.repository.PatientTermLogRepository;
import me.zhengjie.modules.yy.service.PatientTermLogService;
import me.zhengjie.modules.yy.service.dto.PatientTermLogCriteria;
import me.zhengjie.modules.yy.service.dto.PatientTermLogDto;
import me.zhengjie.modules.yy.service.mapstruct.PatientTermLogMapper;
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
 * @date 2020-12-24 14:34
 */
@Service
@RequiredArgsConstructor
public class PatientTermLogServiceImpl implements PatientTermLogService {

    private final PatientTermLogRepository repository;
    private final PatientTermLogMapper mapper;

    @Override
    public Map<String, Object> queryAll(PatientTermLogCriteria criteria, Pageable pageable) {
        Page<PatientTermLog> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<PatientTermLogDto> queryAll(PatientTermLogCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public PatientTermLogDto findById(Long id) {
        PatientTermLog instance = repository.findById(id).orElseGet(PatientTermLog::new);
        ValidationUtil.isNull(instance.getId(), "PatientTermLog", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PatientTermLogDto create(PatientTermLog resources) {
        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(PatientTermLog resources) {
        PatientTermLog instance = repository.findById(resources.getId()).orElseGet(PatientTermLog::new);
        ValidationUtil.isNull(instance.getId(), "PatientTermLog", "id", resources.getId());
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
    public void download(List<PatientTermLogDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PatientTermLogDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", item.getId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
