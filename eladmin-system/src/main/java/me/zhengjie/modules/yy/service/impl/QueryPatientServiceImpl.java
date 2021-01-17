package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.modules.system.service.mapstruct.DeptMapper;
import me.zhengjie.modules.yy.domain.Patient;
import me.zhengjie.modules.yy.domain.PatientCol;
import me.zhengjie.modules.yy.domain.QueryPatient;
import me.zhengjie.modules.yy.repository.QueryPatientRepository;
import me.zhengjie.modules.yy.service.PatientColService;
import me.zhengjie.modules.yy.service.PatientService;
import me.zhengjie.modules.yy.service.QueryPatientService;
import me.zhengjie.modules.yy.service.dto.QueryPatientCriteria;
import me.zhengjie.modules.yy.service.dto.QueryPatientDto;
import me.zhengjie.modules.yy.service.mapstruct.PatientColMapper;
import me.zhengjie.modules.yy.service.mapstruct.PatientMapper;
import me.zhengjie.modules.yy.service.mapstruct.QueryPatientMapper;
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
public class QueryPatientServiceImpl implements QueryPatientService {

    private final QueryPatientRepository repository;
    private final QueryPatientMapper mapper;

    private final PatientColService patientColService;
    private final PatientColMapper patientColMapper;

    private final PatientService patientService;
    private final PatientMapper patientMapper;

    private final DeptService deptService;
    private final DeptMapper deptMapper;

    @Override
    public Map<String, Object> queryAll(QueryPatientCriteria criteria, Pageable pageable) {
        Page<QueryPatient> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<QueryPatientDto> queryAll(QueryPatientCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public QueryPatientDto findById(Long id) {
        QueryPatient instance = repository.findById(id).orElseGet(QueryPatient::new);
        ValidationUtil.isNull(instance.getId(), "QueryPatient", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public QueryPatientDto create(QueryPatient resources) {
        // 新增患者信息
        Patient patient = new Patient();
        patient.setCode(resources.getCode());
        patient.setName(resources.getName());
        patient.setMrn(resources.getMrn());
        patient.setPhone(resources.getPhone());
        patient = patientMapper.toEntity(patientService.create(patient));

        // 查询医院
        Dept dept = deptMapper.toEntity(deptService.findById(resources.getDeptId()));

        // 新增自定义信息
        PatientCol patientCol = new PatientCol();
        patientCol.setPatient(patient);
        patientCol.setDept(dept);
        patientCol.setCol1(resources.getCol1());
        patientCol.setCol2(resources.getCol2());
        patientCol.setCol3(resources.getCol3());
        patientCol.setCol4(resources.getCol4());
        patientCol.setCol5(resources.getCol5());
        patientCol.setStatus(resources.getStatus());
        patientCol.setRemark(resources.getRemark());
        patientCol = patientColMapper.toEntity(patientColService.create(patientCol));

        QueryPatient instance = repository.findById(patientCol.getId()).orElseGet(QueryPatient::new);
        if (null == instance.getId()) {
            throw new RuntimeException("新增患者失败");
        }
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(QueryPatient resources) {
        // 更新自定义信息
        PatientCol patientCol = patientColMapper.toEntity(patientColService.findById(resources.getId()));
        patientCol.setCol1(resources.getCol1());
        patientCol.setCol2(resources.getCol2());
        patientCol.setCol3(resources.getCol3());
        patientCol.setCol4(resources.getCol4());
        patientCol.setCol5(resources.getCol5());
        patientCol.setStatus(resources.getStatus());
        patientCol.setRemark(resources.getRemark());
        patientColService.update(patientCol);

        // 更新患者信息
        Patient patient = patientMapper.toEntity(patientService.findById(resources.getPatientId()));
        patient.setName(resources.getName());
        patient.setMrn(resources.getMrn());
        patient.setPhone(resources.getPhone());
        patientService.update(patient);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAll(Long[] ids) {
        patientColService.deleteAll(ids);
    }

    @Override
    public void download(List<QueryPatientDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (QueryPatientDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", item.getId());
            map.put("状态", item.getStatus());
            map.put("备注", item.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
