package me.zhengjie.modules.ptt.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.ptt.service.OrgService;
import me.zhengjie.modules.ptt.service.dto.OrgCriteria;
import me.zhengjie.modules.ptt.service.dto.OrgDto;
import me.zhengjie.modules.ptt.service.mapstruct.OrgMapper;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.repository.DeptRepository;
import me.zhengjie.modules.system.service.dto.DeptSmallDto;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 医院服务
 *
 * @author yanjun
 * @date 2020-11-30 17:58
 */
@Service
@AllArgsConstructor
@Slf4j
public class OrgServiceImpl implements OrgService {

    private final DeptRepository repository;
    private final OrgMapper mapper;

    @Override
    public List<OrgDto> getOrgs(Long pid) {
        List<Dept> depts;
        if (pid == null) {
            depts = repository.findByPidIsNull();
        } else {
            depts = repository.findByPid(pid);
        }
        return mapper.toDto(depts);
    }

    @Override
    public Dept findOne(Long id) {
        Dept dept = repository.findById(id).orElseGet(Dept::new);
        ValidationUtil.isNull(dept.getId(), "Dept", "id", id);
        return dept;
    }

    @Override
    public Set<Dept> getChildMenus(List<Dept> deptList, Set<Dept> deptSet) {
        for (Dept dept : deptList) {
            deptSet.add(dept);
            List<Dept> depts = repository.findByPid(dept.getId());
            if (depts != null && depts.size() != 0) {
                getChildMenus(depts, deptSet);
            }
        }
        return deptSet;
    }

    @Override
    public Map<String, Object> queryAll(OrgCriteria criteria, Pageable pageable) {
        if (criteria.getPid() == null) {
            criteria.setPid(getRootId());
        }
        Page<Dept> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder),
                pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<OrgDto> queryAll(OrgCriteria criteria) {
        if (criteria.getPid() == null) {
            criteria.setPid(getRootId());
        }
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public List<OrgDto> queryList(OrgCriteria criteria) {
        // 当前用户
        UserDetails currentUser = SecurityUtils.getCurrentUser();
        // 如果是管理员, 查询所有医院
        if (((JwtUserDto) currentUser).getRoles().contains("admin")) {
            criteria.setPid(getRootId());
            return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
        }
        // 获取当前用户部门 id
        DeptSmallDto dept = ((JwtUserDto) currentUser).getUser().getDept();
        criteria.setId(dept.getId());
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    // 获取根节点 id
    private Long getRootId() {
        List<Dept> list = repository.findByPidIsNull();
        if (list != null && !list.isEmpty()) { // 查询
            return list.get(0).getId();
        }
        return null;
    }

    @Transactional
    @Override
    public OrgDto findById(Long id) {
        Dept instance = repository.findById(id).orElseGet(Dept::new);
        ValidationUtil.isNull(instance.getId(), "Org", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrgDto create(Dept resources) {
        // 跟节点 id
        resources.setPid(getRootId());

        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Dept resources) {
        Dept instance = repository.findById(resources.getId()).orElseGet(Dept::new);
        ValidationUtil.isNull(instance.getId(), "Org", "id", resources.getId());
        instance.copy(resources);
        repository.save(instance);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            repository.deleteById(id);
        }
    }

    @Override
    public void download(List<OrgDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (OrgDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", item.getId());
            map.put("创建人", item.getCreateBy());
            map.put("修改人", item.getUpdatedBy());
            map.put("创建时间", item.getCreateTime());
            map.put("修改时间", item.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
