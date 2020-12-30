package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.yy.domain.ReserveResource;
import me.zhengjie.modules.yy.repository.ReserveResourceRepository;
import me.zhengjie.modules.yy.service.ReserveResourceService;
import me.zhengjie.modules.yy.service.dto.ReserveResourceCriteria;
import me.zhengjie.modules.yy.service.dto.ReserveResourceDto;
import me.zhengjie.modules.yy.service.mapstruct.ReserveResourceMapper;
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
public class ReserveResourceServiceImpl implements ReserveResourceService {

    private final ReserveResourceRepository repository;
    private final ReserveResourceMapper mapper;

    @Override
    public Map<String, Object> queryAll(ReserveResourceCriteria criteria, Pageable pageable) {
        Page<ReserveResource> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<ReserveResourceDto> queryAll(ReserveResourceCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public ReserveResourceDto findById(Long id) {
        ReserveResource instance = repository.findById(id).orElseGet(ReserveResource::new);
        ValidationUtil.isNull(instance.getId(), "ReserveResource", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReserveResourceDto create(ReserveResource resources) {
        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(ReserveResource resources) {
        ReserveResource instance = repository.findById(resources.getId()).orElseGet(ReserveResource::new);
        ValidationUtil.isNull(instance.getId(), "ReserveResource", "id", resources.getId());
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
    public void download(List<ReserveResourceDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReserveResourceDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", item.getId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
