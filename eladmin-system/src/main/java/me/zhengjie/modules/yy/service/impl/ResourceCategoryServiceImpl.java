package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.yy.domain.ResourceCategory;
import me.zhengjie.modules.yy.repository.ResourceCategoryRepository;
import me.zhengjie.modules.yy.service.ResourceCategoryService;
import me.zhengjie.modules.yy.service.dto.ResourceCategoryCriteria;
import me.zhengjie.modules.yy.service.dto.ResourceCategoryDto;
import me.zhengjie.modules.yy.service.mapstruct.ResourceCategoryMapper;
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
public class ResourceCategoryServiceImpl implements ResourceCategoryService {

    private final ResourceCategoryRepository repository;
    private final ResourceCategoryMapper mapper;

    @Override
    public List<ResourceCategoryDto> getResourceCategories(Long pid, Long deptId) {
        if (null != pid && !pid.equals(0L)) {
            return new ArrayList<>(0);
        }
        return mapper.toDto(repository.findAllByDeptId(deptId));
    }

    @Override
    public Map<String, Object> queryAll(ResourceCategoryCriteria criteria, Pageable pageable) {
        Page<ResourceCategory> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<ResourceCategoryDto> queryAll(ResourceCategoryCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public ResourceCategoryDto findById(Long id) {
        ResourceCategory instance = repository.findById(id).orElseGet(ResourceCategory::new);
        ValidationUtil.isNull(instance.getId(), "ResourceCategory", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResourceCategoryDto create(ResourceCategory ResourceCategorys) {
        return mapper.toDto(repository.save(ResourceCategorys));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(ResourceCategory ResourceCategorys) {
        ResourceCategory instance = repository.findById(ResourceCategorys.getId()).orElseGet(ResourceCategory::new);
        ValidationUtil.isNull(instance.getId(), "ResourceCategory", "id", ResourceCategorys.getId());
        instance.copy(ResourceCategorys);
        repository.save(instance);
    }

    @Transactional
    @Override
    public void updateResourceGroup(ResourceCategory resources, ResourceCategoryDto resourceCategoryDto) {
        ResourceCategory resourceCategory = mapper.toEntity(resourceCategoryDto);
        resourceCategory.setResourceGroups(resources.getResourceGroups());
        repository.save(resourceCategory);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            repository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResourceCategoryDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResourceCategoryDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", item.getId());
            map.put("状态", item.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
