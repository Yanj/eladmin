package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.yy.domain.Resource;
import me.zhengjie.modules.yy.domain.ResourceCategory;
import me.zhengjie.modules.yy.repository.ResourceCategoryRepository;
import me.zhengjie.modules.yy.repository.ResourceRepository;
import me.zhengjie.modules.yy.service.ResourceService;
import me.zhengjie.modules.yy.service.dto.ResourceCriteria;
import me.zhengjie.modules.yy.service.dto.ResourceDto;
import me.zhengjie.modules.yy.service.mapstruct.ResourceMapper;
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
 * @date 2020-12-24 14:34
 */
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository repository;
    private final ResourceMapper mapper;

    private final ResourceCategoryRepository resourceCategoryRepository;

    @Override
    public Map<String, Object> queryAll(ResourceCriteria criteria, Pageable pageable) {
        Page<Resource> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<ResourceDto> queryAll(ResourceCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public ResourceDto findById(Long id) {
        Resource instance = repository.findById(id).orElseGet(Resource::new);
        ValidationUtil.isNull(instance.getId(), "Resource", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResourceDto create(Resource resources) {
        // 更新资源分类总数
        updateResourceCategoryCount(resources);
        // 保存
        return mapper.toDto(repository.save(resources));
    }

    /*
     * 更新资源分类总数
     */
    private void updateResourceCategoryCount(Resource resources) {
        // 获取资源分类
        if (null == resources.getResourceCategory() || null == resources.getResourceCategory().getId()) {
            throw new RuntimeException("资源分类不能为空");
        }
        ResourceCategory resourceCategory = resourceCategoryRepository.getResourceCategoryForUpdate(resources.getResourceCategory().getId());
        if (null == resourceCategory) {
            throw new RuntimeException("资源分类不存在");
        }

        if (null == resources.getId()) {
            // 资源新增
            // 更新分类资源总数
            int count = null == resourceCategory.getCount() ? 0 : resourceCategory.getCount();
            int addCount = null == resources.getCount() ? 0 : resources.getCount();
            resourceCategory.setCount(count + addCount);
            resourceCategoryRepository.save(resourceCategory);
            return;
        }

        // 原始资源
        Resource oldResource = repository.findById(resources.getId()).orElseGet(Resource::new);
        if (null == oldResource.getId()) {
            throw new RuntimeException("资源不存在");
        }

        if (null != oldResource.getResourceCategory() && null != oldResource.getResourceCategory().getId()) {
            // 原始资源设置了资源分类
            // 查询原始资源分类
            ResourceCategory oldResourceCategory =
                    resourceCategoryRepository.findById(oldResource.getResourceCategory().getId()).orElseGet(ResourceCategory::new);
            if (null == oldResourceCategory.getId()) {
                throw new RuntimeException("资源分类不存在");
            }
            if (!Objects.equals(resourceCategory.getId(), oldResourceCategory.getId())) {
                // 资源分类修改了
                // 更新原类型总数
                int oldCategoryCount = oldResourceCategory.getCount() != null ? oldResourceCategory.getCount() : 0;
                int oldResourceCount = oldResource.getCount() != null ? oldResource.getCount() : 0;
                oldResourceCategory.setCount(oldCategoryCount - oldResourceCount);
                resourceCategoryRepository.save(oldResourceCategory);

                // 更新新类型总数
                int categoryCount = resourceCategory.getCount() != null ? resourceCategory.getCount() : 0;
                int resourceCount = resources.getCount() != null ? resources.getCount() : 0;
                resourceCategory.setCount(categoryCount + resourceCount);
                resourceCategoryRepository.save(resourceCategory);
                return;
            }
        }

        int oldCount = oldResource.getCount() != null ? oldResource.getCount() : 0;
        int count = resources.getCount() != null ? resources.getCount() : 0;
        int categoryCount = resourceCategory.getCount() != null ? resourceCategory.getCount() : 0;

        resourceCategory.setCount(categoryCount + (count - oldCount));
        resourceCategoryRepository.save(resourceCategory);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Resource resources) {
        // 更新分类资源总数
        updateResourceCategoryCount(resources);

        // 更新
        Resource instance = repository.findById(resources.getId()).orElseGet(Resource::new);
        ValidationUtil.isNull(instance.getId(), "Resource", "id", resources.getId());
        instance.copy(resources);
        repository.save(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            Resource resource = repository.findById(id).orElseGet(Resource::new);
            if (null == resource.getId()) {
                continue;
            }

            // 更新分类资源总数
            resource.setCount(0);
            updateResourceCategoryCount(resource);

            repository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResourceDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResourceDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", item.getId());
            map.put("状态", item.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
