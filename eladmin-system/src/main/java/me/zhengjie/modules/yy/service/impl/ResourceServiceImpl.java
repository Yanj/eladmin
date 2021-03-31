package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.modules.yy.domain.Resource;
import me.zhengjie.modules.yy.domain.ResourceCategory;
import me.zhengjie.modules.yy.repository.ResourceCategoryRepository;
import me.zhengjie.modules.yy.repository.ResourceRepository;
import me.zhengjie.modules.yy.service.ResourceService;
import me.zhengjie.modules.yy.service.dto.ResourceCriteria;
import me.zhengjie.modules.yy.service.dto.ResourceDto;
import me.zhengjie.modules.yy.service.mapstruct.ResourceMapper;
import me.zhengjie.utils.*;
import me.zhengjie.utils.enums.YesNoEnum;
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

    private final DeptService deptService;

    @Override
    public Map<String, Object> queryAll(ResourceCriteria criteria, Pageable pageable) {
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();

        criteria.setUser(user);
        // 非管理员用户, 只能看到"未删除"的数据
        if (!user.isAdmin()) {
            criteria.setStatus(YesNoEnum.YES);
        }

        Page<Resource> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<ResourceDto> queryAll(ResourceCriteria criteria) {
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
    public ResourceDto findById(Long id) {
        Resource instance = repository.findById(id).orElseGet(Resource::new);
        ValidationUtil.isNull(instance.getId(), "Resource", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResourceDto create(Resource resources) {
        if (null == resources.getResourceCategory() || null == resources.getResourceCategory().getId()) {
            throw new RuntimeException("资源分类不能为空");
        }
        ResourceCategory resourceCategory = resourceCategoryRepository.getResourceCategoryForUpdate(resources.getResourceCategory().getId());
        if (null == resourceCategory) {
            throw new RuntimeException("资源分类不存在");
        }

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

        // 检查名称是否重复
        checkResourceName(resources, resources.getName());

        // 设置资源组状态
        if (resources.getStatus() == null) {
            resources.setStatus(YesNoEnum.YES);
        }

        // 设置资源数量
        if (resources.getCount() == null) {
            resources.setCount(0);
        }

        // 保存
        ResourceDto dto = mapper.toDto(repository.save(resources));

        // 更新资源分类总数
        updateResourceCategoryCount(resources, null);

        return dto;
    }

    /*
     * 更新资源分类总数
     */
    private void updateResourceCategoryCount(Resource resources, Long oldResourceCategoryId) {
        // 更新分类资源总数
        Long resourceCategoryId = resources.getResourceCategory().getId();
        resourceCategoryRepository.updateResourceCategoryCount(resourceCategoryId);

        // 资源新增
        if (null == resources.getId()) {
            return;
        }

        // 分类发送了改变
        if (null != oldResourceCategoryId && !resourceCategoryId.equals(oldResourceCategoryId)) {
            // 更新"旧记录"类型总数
            resourceCategoryRepository.updateResourceCategoryCount(oldResourceCategoryId);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Resource resources) {
        Resource instance = repository.findById(resources.getId()).orElseGet(Resource::new);
        ValidationUtil.isNull(instance.getId(), "Resource", "id", resources.getId());

        // 如果名称改变
        if (null != resources.getName() && !StringUtils.equals(instance.getName(), resources.getName())) {
            checkResourceName(instance, resources.getName());
        }

        // 旧记录的分类
        Long oldResourceCategoryId = instance.getResourceCategory().getId();

        // 更新
        instance.copy(resources);
        repository.save(instance);

        // 跟新分类资源总数
        updateResourceCategoryCount(instance, oldResourceCategoryId);
    }

    private void checkResourceName(Resource instance, String name) {
        ResourceCriteria criteria = new ResourceCriteria();
        criteria.setOrgId(instance.getOrgId());
        criteria.setComId(instance.getComId());
        criteria.setDeptId(instance.getDeptId());
        criteria.setName(name);
        List<Resource> list = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        if (!list.isEmpty()) {
            throw new BadRequestException("资源名称: " + name + ", 已存在");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAll(Long[] ids) {
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        if (user.isAdmin()) {
            for (Long id : ids) {
                Resource resource = repository.findById(id).orElseGet(Resource::new);
                if (null == resource.getId()) {
                    continue;
                }

                // 删除
                repository.deleteById(id);

                // 更新分类资源总数
                updateResourceCategoryCount(resource, null);
            }
        } else {
            for (Long id : ids) {
                Resource resource = repository.findById(id).orElseGet(Resource::new);
                if (null == resource.getId()) {
                    continue;
                }

                // 删除
                resource.setStatus(YesNoEnum.NO);
                repository.save(resource);

                // 更新分类资源总数
                updateResourceCategoryCount(resource, null);
            }
        }
    }

    @Override
    public void download(List<ResourceDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResourceDto item : all) {
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
