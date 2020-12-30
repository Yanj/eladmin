package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.yy.domain.WorkTime;
import me.zhengjie.modules.yy.repository.WorkTimeRepository;
import me.zhengjie.modules.yy.service.WorkTimeService;
import me.zhengjie.modules.yy.service.dto.WorkTimeCriteria;
import me.zhengjie.modules.yy.service.dto.WorkTimeDto;
import me.zhengjie.modules.yy.service.mapstruct.WorkTimeMapper;
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
public class WorkTimeServiceImpl implements WorkTimeService {

    private final WorkTimeRepository repository;
    private final WorkTimeMapper mapper;

    @Override
    public Map<String, Object> queryAll(WorkTimeCriteria criteria, Pageable pageable) {
        Page<WorkTime> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<WorkTimeDto> queryAll(WorkTimeCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public WorkTimeDto findById(Long id) {
        WorkTime instance = repository.findById(id).orElseGet(WorkTime::new);
        ValidationUtil.isNull(instance.getId(), "WorkTime", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public WorkTimeDto create(WorkTime resources) {
        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(WorkTime resources) {
        WorkTime instance = repository.findById(resources.getId()).orElseGet(WorkTime::new);
        ValidationUtil.isNull(instance.getId(), "WorkTime", "id", resources.getId());
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
    public void download(List<WorkTimeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (WorkTimeDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", item.getId());
            map.put("状态", item.getStatus());
            map.put("备注", item.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
