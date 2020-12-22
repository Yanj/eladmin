package me.zhengjie.modules.ptt.service.impl;

import lombok.AllArgsConstructor;
import me.zhengjie.modules.ptt.domain.Patient;
import me.zhengjie.modules.ptt.domain.PatientDept;
import me.zhengjie.modules.ptt.repository.PatientDeptRepository;
import me.zhengjie.modules.ptt.service.PatientDeptService;
import me.zhengjie.modules.ptt.service.dto.PatientDeptCriteria;
import me.zhengjie.modules.ptt.service.dto.PatientDeptDto;
import me.zhengjie.modules.ptt.service.mapstruct.PatientDeptMapper;
import me.zhengjie.modules.system.domain.Dept;
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
 * 患者医院信息服务
 *
 * @author yanjun
 * @date 2020-11-28 11:17
 */
@Service
@AllArgsConstructor
public class PatientDeptServiceImpl implements PatientDeptService {

    private final PatientDeptRepository repository;
    private final PatientDeptMapper mapper;

    @Override
    public PatientDeptDto createOrUpdate(Dept userDept, Patient patient) {
        List<PatientDept> depts = repository.findByDeptAndPatient(userDept, patient);
        if (depts == null || depts.isEmpty()) {
            PatientDept dept = new PatientDept();
            dept.setDept(userDept);
            dept.setPatient(patient);
            return create(dept);
        }
        return mapper.toDto(depts.get(0));
    }

    @Override
    public Map<String, Object> queryAll(PatientDeptCriteria criteria, Pageable pageable) {
        Page<PatientDept> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<PatientDeptDto> queryAll(PatientDeptCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public PatientDeptDto findById(PatientDept.PK id) {
        PatientDept instance = repository.findById(id).orElseGet(PatientDept::new);
//        ValidationUtil.isNull(instance.getId(), "PatientDept", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PatientDeptDto create(PatientDept resources) {
        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(PatientDept resources) {
//        PatientDept patient = repository.findById(resources.getId()).orElseGet(PatientDept::new);
//        ValidationUtil.isNull(patient.getId(), "PatientDept", "id", resources.getId());
//        patient.copy(resources);
//        repository.save(patient);
    }

    @Override
    public void deleteAll(PatientDept.PK[] ids) {
        for (PatientDept.PK id : ids) {
            repository.deleteById(id);
        }
    }

    @Override
    public void download(List<PatientDeptDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PatientDeptDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
