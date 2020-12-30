package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.yy.domain.ResourceGroup;
import me.zhengjie.modules.yy.repository.ResourceGroupRepository;
import me.zhengjie.modules.yy.service.ResourceGroupService;
import me.zhengjie.modules.yy.service.dto.ResourceGroupCriteria;
import me.zhengjie.modules.yy.service.dto.ResourceGroupDto;
import me.zhengjie.modules.yy.service.mapstruct.ResourceGroupMapper;
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
public class ResourceGroupServiceImpl implements ResourceGroupService {

    private final ResourceGroupRepository repository;
    private final ResourceGroupMapper mapper;

    @Override
    public List<ResourceGroupDto> getResourceGroups(Long pid, Long deptId) {
        if (null != pid && !pid.equals(0L)) {
            return new ArrayList<>(0);
        }
        return mapper.toDto(repository.findAllByDeptId(deptId));
    }

    @Override
    public Map<String, Object> queryAll(ResourceGroupCriteria criteria, Pageable pageable) {
        Page<ResourceGroup> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<ResourceGroupDto> queryAll(ResourceGroupCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public List<ResourceGroupDto> queryByDeptIdAndTermCode(Long deptId, String termCode) {
        return mapper.toDto(repository.findAllByDeptIdAndTermCode(deptId, termCode));
    }

    @Transactional
    @Override
    public ResourceGroupDto findById(Long id) {
        ResourceGroup instance = repository.findById(id).orElseGet(ResourceGroup::new);
        ValidationUtil.isNull(instance.getId(), "ResourceGroup", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResourceGroupDto create(ResourceGroup ResourceGroups) {
        return mapper.toDto(repository.save(ResourceGroups));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(ResourceGroup ResourceGroups) {
        ResourceGroup instance = repository.findById(ResourceGroups.getId()).orElseGet(ResourceGroup::new);
        ValidationUtil.isNull(instance.getId(), "ResourceGroup", "id", ResourceGroups.getId());
        instance.copy(ResourceGroups);
        repository.save(instance);
    }

    @Transactional
    @Override
    public void updateResourceCategory(ResourceGroup resources, ResourceGroupDto resourceGroupDto) {
        ResourceGroup resourceGroup = mapper.toEntity(resourceGroupDto);
        resourceGroup.setResourceCategories(resources.getResourceCategories());
        repository.save(resourceGroup);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            repository.deleteById(id);
        }
    }

    @Override
    public void download(List<ResourceGroupDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResourceGroupDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", item.getId());
            map.put("状态", item.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
