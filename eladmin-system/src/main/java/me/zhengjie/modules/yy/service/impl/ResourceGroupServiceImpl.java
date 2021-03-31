package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.modules.yy.domain.ResourceGroup;
import me.zhengjie.modules.yy.repository.ResourceGroupRepository;
import me.zhengjie.modules.yy.service.ResourceGroupService;
import me.zhengjie.modules.yy.service.dto.ResourceGroupCriteria;
import me.zhengjie.modules.yy.service.dto.ResourceGroupDto;
import me.zhengjie.modules.yy.service.mapstruct.ResourceGroupMapper;
import me.zhengjie.utils.*;
import me.zhengjie.utils.enums.YesNoEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final DeptService deptService;

    @Override
    public Map<String, Object> queryAll(ResourceGroupCriteria criteria, Pageable pageable) {
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        criteria.setUser(user);
        // 非管理员, 只能看到"未删除"的数据
        if (!user.isAdmin()) {
            criteria.setStatus(YesNoEnum.YES);
        }
        Page<ResourceGroup> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<ResourceGroupDto> queryAll(ResourceGroupCriteria criteria) {
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        criteria.setUser(user);
        // 非管理员, 只能看到"未删除"的数据
        if (!user.isAdmin()) {
            criteria.setStatus(YesNoEnum.YES);
        }
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
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
    public ResourceGroupDto create(ResourceGroup resources) {
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

        // 检查名称是否存在
        checkGroupName(resources, resources.getName());

        // 设置资源组状态
        if (resources.getStatus() == null) {
            resources.setStatus(YesNoEnum.YES);
        }

        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(ResourceGroup resources) {
        ResourceGroup instance = repository.findById(resources.getId()).orElseGet(ResourceGroup::new);
        ValidationUtil.isNull(instance.getId(), "ResourceGroup", "id", resources.getId());
        // 名称改变
        if (null != resources.getName() && !StringUtils.equals(resources.getName(), instance.getName())) {
            // 检查名称是否存在
            checkGroupName(instance, resources.getName());
        }
        instance.copy(resources);
        repository.save(instance);
    }

    private void checkGroupName(ResourceGroup instance, String name) {
        ResourceGroupCriteria criteria = new ResourceGroupCriteria();
        criteria.setOrgId(instance.getOrgId());
        criteria.setComId(instance.getComId());
        criteria.setDeptId(instance.getDeptId());
        criteria.setName(name);
        List<ResourceGroup> list = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria,
                criteriaBuilder));
        if (!list.isEmpty()) {
            throw new BadRequestException("资源分组名称: " + name + ", 已存在");
        }
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
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        // 管理员删除
        if (user.isAdmin()) {
            for (Long id : ids) {
                repository.deleteById(id);
            }
        } else {
            for (Long id : ids) {
                ResourceGroup instance = repository.findById(id).orElseGet(ResourceGroup::new);
                ValidationUtil.isNull(instance.getId(), "ResourceGroup", "id", id);
                instance.setStatus(YesNoEnum.NO);
                repository.save(instance);
            }
        }
    }

    @Override
    public void download(List<ResourceGroupDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResourceGroupDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("ID", item.getId());
            map.put("组织ID", item.getOrgId());
            map.put("公司ID", item.getComId());
            map.put("部门ID", item.getDeptId());
            map.put("名称", item.getName());
            map.put("状态", item.getStatus());
            map.put("备注", item.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
