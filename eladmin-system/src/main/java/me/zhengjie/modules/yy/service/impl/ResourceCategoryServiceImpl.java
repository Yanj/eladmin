package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.modules.yy.domain.ResourceCategory;
import me.zhengjie.modules.yy.repository.ResourceCategoryRepository;
import me.zhengjie.modules.yy.service.ResourceCategoryService;
import me.zhengjie.modules.yy.service.dto.ResourceCategoryCriteria;
import me.zhengjie.modules.yy.service.dto.ResourceCategoryDto;
import me.zhengjie.modules.yy.service.mapstruct.ResourceCategoryMapper;
import me.zhengjie.utils.*;
import me.zhengjie.utils.enums.YesNoEnum;
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

    private final DeptService deptService;

    @Override
    public Map<String, Object> queryAll(ResourceCategoryCriteria criteria, Pageable pageable) {
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();

        criteria.setUser(user);
        // 非管理员用户, 只能看到"未删除"的数据
        if (!user.isAdmin()) {
            criteria.setStatus(YesNoEnum.YES);
        }

        Page<ResourceCategory> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<ResourceCategoryDto> queryAll(ResourceCategoryCriteria criteria) {
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();

        criteria.setUser(user);
        // 非管理员用户, 只能看到"未删除"的数据
        if (!user.isAdmin()) {
            criteria.setStatus(YesNoEnum.YES);
        }

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
    public ResourceCategoryDto create(ResourceCategory resources) {
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();

        // 设置资源组部门
        if (!user.isAdmin()) {
            resources.setOrgId(user.getOrgId());
            resources.setComId(user.getComId());
            resources.setDeptId(user.getDeptId());
        }
        if (resources.getOrgId() == null) {
            throw new BadRequestException("组织Id不能为空");
        }
        deptService.findById(resources.getOrgId());

        if (resources.getComId() == null) {
            throw new BadRequestException("公司Id不能为空");
        }
        deptService.findById(resources.getComId());

        // 如果传入了部门 ID, 检查部门 ID
        if (null != resources.getDeptId()) {
            deptService.findById(resources.getDeptId());
        }

        // 设置资源组状态
        if (resources.getStatus() == null) {
            resources.setStatus(YesNoEnum.YES);
        }

        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(ResourceCategory resources) {
        ResourceCategory instance = repository.findById(resources.getId()).orElseGet(ResourceCategory::new);
        ValidationUtil.isNull(instance.getId(), "ResourceCategory", "id", resources.getId());
        // 名称改变
        if (null != resources.getName() && !StringUtils.equals(instance.getName(), resources.getName())) {
            // 检查名称是否存在
            checkCategoryName(instance, resources.getName());
        }
        instance.copy(resources);
        repository.save(instance);
    }

    private void checkCategoryName(ResourceCategory instance, String name) {
        ResourceCategoryCriteria criteria = new ResourceCategoryCriteria();
        criteria.setOrgId(instance.getOrgId());
        criteria.setComId(instance.getComId());
        criteria.setDeptId(instance.getDeptId());
        criteria.setName(name);
        List<ResourceCategory> list = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria,
                criteriaBuilder));
        if (!list.isEmpty()) {
            throw new BadRequestException("资源分类名称: " + name + ", 已存");
        }
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
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        if (user.isAdmin()) {
            for (Long id : ids) {
                repository.deleteById(id);
            }
        } else {
            for (Long id : ids) {
                ResourceCategory instance = repository.findById(id).orElseGet(ResourceCategory::new);
                ValidationUtil.isNull(instance.getId(), "ResourceCategory", "id", id);
                instance.setStatus(YesNoEnum.NO);
                repository.save(instance);
            }
        }
    }

    @Override
    public void download(List<ResourceCategoryDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResourceCategoryDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("ID", item.getId());
            map.put("组织ID", item.getOrgId());
            map.put("公司ID", item.getComId());
            map.put("部门ID", item.getDeptId());
            map.put("名称", item.getName());
            map.put("资源数量", item.getCount());
            map.put("状态", item.getStatus());
            map.put("备注", item.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
