package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.yy.domain.ReserveLog;
import me.zhengjie.modules.yy.repository.ReserveLogRepository;
import me.zhengjie.modules.yy.service.ReserveLogService;
import me.zhengjie.modules.yy.service.dto.ReserveLogCriteria;
import me.zhengjie.modules.yy.service.dto.ReserveLogDto;
import me.zhengjie.modules.yy.service.mapstruct.ReserveLogMapper;
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
public class ReserveLogServiceImpl implements ReserveLogService {

    private final ReserveLogRepository repository;
    private final ReserveLogMapper mapper;

    @Override
    public Map<String, Object> queryAll(ReserveLogCriteria criteria, Pageable pageable) {
        Page<ReserveLog> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<ReserveLogDto> queryAll(ReserveLogCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public ReserveLogDto findById(Long id) {
        ReserveLog instance = repository.findById(id).orElseGet(ReserveLog::new);
        ValidationUtil.isNull(instance.getId(), "ReserveLog", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReserveLogDto create(ReserveLog resources) {
        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(ReserveLog resources) {
        ReserveLog instance = repository.findById(resources.getId()).orElseGet(ReserveLog::new);
        ValidationUtil.isNull(instance.getId(), "ReserveLog", "id", resources.getId());
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
    public void download(List<ReserveLogDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReserveLogDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", item.getId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
