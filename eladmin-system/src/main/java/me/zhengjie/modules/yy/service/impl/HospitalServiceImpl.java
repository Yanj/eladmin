package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.repository.DeptRepository;
import me.zhengjie.modules.system.service.dto.DeptSmallDto;
import me.zhengjie.modules.yy.domain.Hospital;
import me.zhengjie.modules.yy.domain.ResourceReserveCount;
import me.zhengjie.modules.yy.repository.HospitalRepository;
import me.zhengjie.modules.yy.repository.ResourceReserveCountRepository;
import me.zhengjie.modules.yy.service.HospitalService;
import me.zhengjie.modules.yy.service.dto.HospitalCriteria;
import me.zhengjie.modules.yy.service.dto.HospitalDto;
import me.zhengjie.modules.yy.service.mapstruct.HospitalMapper;
import me.zhengjie.utils.*;
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
 * @date 2020-12-24 21:23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    private final HospitalRepository repository;
    private final HospitalMapper mapper;

    private final DeptRepository deptRepository;

    @Override
    public Map<String, Object> queryAll(HospitalCriteria criteria, Pageable pageable) {
        Page<Hospital> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder),
                pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<HospitalDto> querySelf(HospitalCriteria criteria) {
        // 查询可用的医院
        criteria.setEnabled(true);

        // 判断当前用户
        UserDetails currentUser = SecurityUtils.getCurrentUser();
        // 如果是管理员, 查询所有医院
        if (((JwtUserDto) currentUser).getRoles().contains("admin")) {
            return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
        }

        // 获取当前用户部门 id
        DeptSmallDto dept = ((JwtUserDto) currentUser).getUser().getDept();
        criteria.setId(dept.getId());
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public List<HospitalDto> queryAll(HospitalCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public HospitalDto findById(Long id) {
        Hospital instance = repository.findById(id).orElseGet(Hospital::new);
        ValidationUtil.isNull(instance.getId(), "Hospital", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public HospitalDto create(Hospital resources) {
        // 保存
        Dept dept = new Dept();
        dept.setName(resources.getName());
        dept.setEnabled(resources.getEnabled());
        dept.setPid(getPid());
        dept.setLevel(1);
        dept.setSubCount(0);
        dept.setDeptSort(999);
        dept = deptRepository.save(dept);

        // 查询
        resources = repository.findById(dept.getId()).orElseGet(Hospital::new);
        if (null == resources.getId()) {
            throw new RuntimeException("");
        }
        return mapper.toDto(resources);
    }

    // 获取根 id
    private Long getPid() {
        List<Dept> rootList = deptRepository.findByPidIsNull();
        if (null == rootList || rootList.isEmpty()) {
            return null;
        }
        return rootList.get(0).getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Hospital resources) {
        Dept dept = deptRepository.findById(resources.getId()).orElseGet(Dept::new);
        ValidationUtil.isNull(dept.getId(), "Hospital", "id", resources.getId());
        dept.setName(resources.getName());
        dept.setEnabled(resources.getEnabled());
        deptRepository.save(dept);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            deptRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<HospitalDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (HospitalDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", item.getId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
