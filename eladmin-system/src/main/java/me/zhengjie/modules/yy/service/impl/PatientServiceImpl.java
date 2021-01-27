package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.vo.HisCkItemVo;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.repository.DeptRepository;
import me.zhengjie.modules.system.service.mapstruct.DeptMapper;
import me.zhengjie.modules.yy.domain.Patient;
import me.zhengjie.modules.yy.domain.PatientCol;
import me.zhengjie.modules.yy.domain.PatientTerm;
import me.zhengjie.modules.yy.domain.Term;
import me.zhengjie.modules.yy.repository.PatientRepository;
import me.zhengjie.modules.yy.repository.PatientTermRepository;
import me.zhengjie.modules.yy.repository.TermRepository;
import me.zhengjie.modules.yy.service.HisLogService;
import me.zhengjie.modules.yy.service.HospitalService;
import me.zhengjie.modules.yy.service.PatientColService;
import me.zhengjie.modules.yy.service.PatientService;
import me.zhengjie.modules.yy.service.dto.PatientCriteria;
import me.zhengjie.modules.yy.service.dto.PatientDto;
import me.zhengjie.modules.yy.service.mapstruct.PatientMapper;
import me.zhengjie.service.HisService;
import me.zhengjie.service.dto.HisCkItemDto;
import me.zhengjie.utils.*;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;
    private final PatientMapper mapper;

    private final TermRepository termRepository;
    private final PatientTermRepository patientTermRepository;

    private final HospitalService hospitalService;
    private final DeptMapper deptMapper;

    private final PatientColService patientColService;

    private final HisService hisService;

    private final HisLogService hisLogService;

    @Override
    public Map<String, Object> querySync(PatientCriteria criteria, Pageable pageable) {
        if (StringUtils.isEmpty(criteria.getInfoType())) {
            throw new IllegalArgumentException("infoType 不能为空");
        }
        if (StringUtils.isEmpty(criteria.getPatientInfo())) {
            throw new IllegalArgumentException("patientInfo 不能为空");
        }
        try {
            // 查询HIS系统
            HisCkItemVo hisCkItemVo = new HisCkItemVo();
            hisCkItemVo.setInfoType(HisCkInfoTypeEnum.valueOf(criteria.getInfoType()));
            hisCkItemVo.setPatientInfo(criteria.getPatientInfo());
            sync(hisCkItemVo);
        } catch (Exception e) {
            log.error("查询 HIS 失败", e);
        }

        // 设置查询条件
        if ("mrn".equalsIgnoreCase(criteria.getInfoType())) {
            criteria.setMrn(criteria.getPatientInfo());
            criteria.setName(null);
            criteria.setPhone(null);
        } else if ("name".equalsIgnoreCase(criteria.getInfoType())) {
            criteria.setMrn(null);
            criteria.setName(criteria.getPatientInfo());
            criteria.setPhone(null);
        } else if ("phone".equalsIgnoreCase(criteria.getInfoType())) {
            criteria.setMrn(null);
            criteria.setName(null);
            criteria.setPhone(criteria.getPatientInfo());
        }

        return queryAll(criteria, pageable);
    }

    @Override
    public List<PatientDto> query(PatientCriteria criteria) {
        HisCkInfoTypeEnum infoType = HisCkInfoTypeEnum.valueOf(criteria.getInfoType());
        if (infoType == HisCkInfoTypeEnum.PHONE) {
            return mapper.toDto(repository.findAllByPhone(criteria.getPatientInfo()));
        }
        if (infoType == HisCkInfoTypeEnum.MRN) {
            return mapper.toDto(repository.findAllByMrn(criteria.getPatientInfo()));
        }
        if (infoType == HisCkInfoTypeEnum.NAME) {
            return mapper.toDto(repository.findAllByName(criteria.getPatientInfo()));
        }
        return new ArrayList<>(0);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> sync(HisCkItemVo hisCkItemVo) throws Exception {
        List<HisCkItemDto> ckItemList = hisService.asyncQueryCkItemList(hisCkItemVo);

        log.debug("syncData: his 查询结果: " + ckItemList);
        if (ckItemList == null || ckItemList.isEmpty()) {
            throw new RuntimeException("查询 HIS 无数据");
        }

        // 保存查询日志
        hisLogService.create(ckItemList);

        // 不存在的套餐
        Map<String, Object> noExistMap = new HashMap<>();

        // 同步患者信息
        // ... 过滤数据
        Map<String, Patient> patientMap = new HashMap<>();
        for (HisCkItemDto ckItem : ckItemList) {
            if (patientMap.containsKey(ckItem.getPatientId().toString())) {
                continue;
            }
            Patient patient = new Patient();
            patient.setCode(ckItem.getPatientId().toString());
            patient.setName(ckItem.getName());
            patient.setPhone(ckItem.getMobilePhone());
            patient.setMrn(ckItem.getMrn().toString());
            patientMap.put(ckItem.getPatientId().toString(), patient);
        }
        // ... 更新数据库
        for (String code : patientMap.keySet()) {
            Patient patient = repository.findByCode(code);
            if (null == patient) {
                patient = repository.save(patientMap.get(code));
            }
            patientMap.put(code, patient);
        }

        // 同步患者附加数据
        patientColService.syncData();

        // 同步患者套餐信息
        // ... 过滤数据
        Map<String, PatientTerm> patientTermMap = new HashMap<>();
        for (HisCkItemDto ckItem : ckItemList) {
            String patItemId = ckItem.getPatItemId().toString();
            if (patientTermMap.containsKey(patItemId)) {
                continue;
            }

            PatientTerm patientTerm = new PatientTerm();
            patientTerm.setPatItemId(patItemId);
            patientTerm.setPatient(patientMap.get(ckItem.getPatientId().toString()));
            patientTerm.setTermCode(ckItem.getItemCode());
            patientTerm.setPrice(ckItem.getActualCosts());
            patientTerm.setTimes(ckItem.getAmount().intValue());

            Term term = termRepository.findFirstByCode(ckItem.getItemCode()).orElseGet(Term::new);
            if (null == term.getId()) {
                log.info("套餐不存在, 请配置");
                noExistMap.put(ckItem.getItemCode(), ckItem.getItemName());

                patientTerm.setTermName(ckItem.getItemName());
                patientTerm.setTermPrice(ckItem.getActualCosts());
                patientTerm.setTermOriginalPrice(ckItem.getCosts());
                patientTerm.setTermTimes(ckItem.getAmount().intValue());
                patientTerm.setTermUnit(ckItem.getUnit());
            } else {
                patientTerm.setTermName(term.getName());
                patientTerm.setTermPrice(term.getPrice());
                patientTerm.setTermOriginalPrice(term.getOriginalPrice());
                patientTerm.setTermTimes(term.getTimes());
                patientTerm.setTermUnit(term.getUnit());
            }
            patientTermMap.put(patItemId, patientTerm);
        }
        // .. 更新数据库
        for (String patItemId : patientTermMap.keySet()) {
            PatientTerm patientTerm = patientTermRepository.findByPatItemId(patItemId);
            if (null == patientTerm) {
                patientTermRepository.save(patientTermMap.get(patItemId));
            }
        }

        Map<String, Object> res = new HashMap<>();
        res.put("notExists", noExistMap);
        if (!patientMap.isEmpty()) {
            res.put("patient", mapper.toDto(patientMap.values().iterator().next()));
        }
        return res;
    }

    @Override
    public Map<String, Object> queryAll(PatientCriteria criteria, Pageable pageable) {
        Page<Patient> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<PatientDto> queryAll(PatientCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public PatientDto findById(Long id) {
        Patient instance = repository.findById(id).orElseGet(Patient::new);
        ValidationUtil.isNull(instance.getId(), "Patient", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PatientDto create(Patient resources) {
        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Patient resources) {
        Patient instance = repository.findById(resources.getId()).orElseGet(Patient::new);
        ValidationUtil.isNull(instance.getId(), "Patient", "id", resources.getId());
        instance.copy(resources);
        repository.save(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            repository.deleteById(id);
        }
    }

    @Override
    public void download(List<PatientDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PatientDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", item.getId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
