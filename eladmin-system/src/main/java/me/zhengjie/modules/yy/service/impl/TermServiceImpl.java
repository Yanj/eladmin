package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.modules.yy.domain.Term;
import me.zhengjie.modules.yy.repository.TermRepository;
import me.zhengjie.modules.yy.service.TermService;
import me.zhengjie.modules.yy.service.dto.TermCriteria;
import me.zhengjie.modules.yy.service.dto.TermDto;
import me.zhengjie.modules.yy.service.mapstruct.TermMapper;
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
public class TermServiceImpl implements TermService {

    private final TermRepository repository;
    private final TermMapper mapper;

    private final DeptService deptService;

    @Override
    public Map<String, Object> queryAll(TermCriteria criteria, Pageable pageable) {
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        criteria.setUser(user);
        // 非管理员, 只能看到"未删除"的数据
        if (!user.isAdmin()) {
            criteria.setStatus(YesNoEnum.YES);
        }
        Page<Term> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<TermDto> queryAll(TermCriteria criteria) {
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        criteria.setUser(user);
        // 非管理员, 只能看到"未删除"的数据
        if (!user.isAdmin()) {
            criteria.setStatus(YesNoEnum.YES);
        }
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TermDto findById(Long id) {
        Term instance = repository.findById(id).orElseGet(Term::new);
        ValidationUtil.isNull(instance.getId(), "Term", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TermDto create(Term resources) {
        // 用户信息
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

        // 检查"Code"是否存在
        checkCode(resources, resources.getCode());
        // 检查"名称"是否存在
        checkName(resources, resources.getName());

        // 状态
        if (null == resources.getStatus()) {
            resources.setStatus(YesNoEnum.YES);
        }

        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Term resources) {
        Term instance = repository.findById(resources.getId()).orElseGet(Term::new);
        ValidationUtil.isNull(instance.getId(), "Term", "id", resources.getId());

        // 检查 code
        if (null != resources.getCode() && !StringUtils.equals(instance.getCode(), resources.getCode())) {
            checkCode(instance, resources.getCode());
        }

        // 检查 name
        if (null != resources.getName() && !StringUtils.equals(instance.getName(), resources.getName())) {
            checkName(instance, resources.getName());
        }

        instance.copy(resources);
        repository.save(instance);
    }

    private void checkCode(Term resources, String code) {
        TermCriteria criteria = new TermCriteria();
        criteria.setOrgId(resources.getOrgId());
        criteria.setComId(resources.getComId());
        criteria.setDeptId(resources.getDeptId());
        criteria.setCode(code);
        List<Term> list = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        if (!list.isEmpty()) {
            throw new BadRequestException("套餐外部系统ID:" + code + ", 已存在");
        }
    }

    private void checkName(Term resources, String name) {
        TermCriteria criteria = new TermCriteria();
        criteria.setOrgId(resources.getOrgId());
        criteria.setComId(resources.getComId());
        criteria.setDeptId(resources.getDeptId());
        criteria.setName(name);
        List<Term> list = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        if (!list.isEmpty()) {
            throw new BadRequestException("套餐名称:" + name + ", 已存在");
        }
    }

    @Transactional
    @Override
    public void updateResourceCategory(Term resources, TermDto resourceGroupDto) {
        Term term = mapper.toEntity(resourceGroupDto);
        term.setResourceGroups(resources.getResourceGroups());
        repository.save(term);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAll(Long[] ids) {
        // 用户信息
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        if (user.isAdmin()) {
            for (Long id : ids) {
                repository.deleteById(id);
            }
        } else {
            for (Long id : ids) {
                Term instance = repository.findById(id).orElseGet(Term::new);
                ValidationUtil.isNull(instance.getId(), "Term", "id", id);
                instance.setStatus(YesNoEnum.NO);
                repository.save(instance);
            }
        }
    }

    @Override
    public void download(List<TermDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TermDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("ID", item.getId());
            map.put("组织ID", item.getOrgId());
            map.put("公司ID", item.getComId());
            map.put("部门ID", item.getDeptId());
            map.put("外部系统ID", item.getCode());
            map.put("名称", item.getName());
            map.put("价格", item.getPrice());
            map.put("原价", item.getOriginalPrice());
            map.put("次数", item.getTimes());
            map.put("单位", item.getUnit());
            map.put("状态", item.getStatus());
            map.put("备注", item.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
