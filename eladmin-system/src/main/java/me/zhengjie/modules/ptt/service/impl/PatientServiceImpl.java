package me.zhengjie.modules.ptt.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.vo.HisCkItemVo;
import me.zhengjie.modules.ptt.domain.*;
import me.zhengjie.modules.ptt.repository.*;
import me.zhengjie.modules.ptt.service.*;
import me.zhengjie.modules.ptt.service.dto.*;
import me.zhengjie.modules.ptt.service.mapstruct.CusMapper;
import me.zhengjie.modules.ptt.service.mapstruct.PatientMapper;
import me.zhengjie.modules.ptt.service.mapstruct.PatientWithCusMapper;
import me.zhengjie.modules.ptt.service.mapstruct.PatientWithDeptMapper;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.DictDetail;
import me.zhengjie.modules.system.repository.DeptRepository;
import me.zhengjie.modules.system.repository.DictDetailRepository;
import me.zhengjie.service.HisService;
import me.zhengjie.service.dto.HisCkItemDto;
import me.zhengjie.utils.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

/**
 * 患者信息服务
 *
 * @author yanjun
 * @date 2020-11-28 11:17
 */
@Service
@AllArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;
    private final PatientMapper mapper;
    private final me.zhengjie.modules.ptt.mapper.PatientCusMapper patientCusMapper1;

    private final HisService hisService;
    private final HisLogService hisLogService;
    private final TermService termService;
    private final PatientDeptService patientDeptService;
    private final PatientTermService patientTermService;

    private final PatientWithDeptRepository patientWithDeptRepository;
    private final PatientWithDeptMapper patientWithDeptMapper;

    private final CusRepository cusRepository;
    private final CusMapper cusMapper;
    private final PatientCusRepository patientCusRepository;
    private final me.zhengjie.modules.ptt.service.mapstruct.PatientCusMapper patientCusMapper;
    private final PatientWithCusRepository patientWithCusRepository;
    private final PatientWithCusMapper patientWithCusMapper;

    private final DictDetailRepository dictDetailRepository;
    private final DeptRepository deptRepository;

    @Override
    public Map<String, Object> findFull(PatientCriteria criteria, Pageable pageable) {
        Long patientId = criteria.getPatientId();
        Specification<PatientWithDept> specification = (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            list.add(cb.conjunction());
            if (null != patientId) {
                list.add(cb.equal(root.get("patientId"), patientId));
            }
            return cb.and(list.toArray(new Predicate[0]));
        };
        return PageUtil.toPage(patientWithDeptRepository.findAll(specification, pageable).map(patientWithDeptMapper::toDto));
    }

    @Override
    public Map<String, Object> findFullCols(PatientCriteria criteria, Pageable pageable) {
        final Long deptId = criteria.getDeptId();
        final Long patientId = criteria.getPatientId();

        // 根据部门查询所有自定义列
        Specification<Cus> specification = (root, query, cb) -> cb.equal(root.get("dept").get("id"), deptId);
        Page<Cus> page = cusRepository.findAll(specification, pageable);
        Page<CusDto> dtoPage = page.map(cusMapper::toDto);

        // 根据部门和患者查询患者自定义列的值
        List<PatientWithCus> list = patientWithCusRepository.findByDeptIdAndPatientId(deptId, patientId);
        List<PatientWithCusDto> dtoList = patientWithCusMapper.toDto(list);

        // 处理数据
        Page<PatientWithCusDto> res = dtoPage.map(cusDto -> {
            PatientWithCusDto item = new PatientWithCusDto();
            item.setCus(cusDto);
            item.setCusId(cusDto.getId());
            item.setDeptId(deptId);
            item.setType(cusDto.getType());

            for (int i = 0; i < dtoList.size(); i++) {
                PatientWithCusDto dto = dtoList.get(i);
                if (Objects.equals(dto.getCusId(), cusDto.getId())) { // 同一个列
                    item.setPatientId(patientId);
                    item.setValue(dto.getValue());
                    item.setDictDetail(dto.getDictDetail());
                    break;
                }
            }
            return item;
        });

        return PageUtil.toPage(res);
    }

    @Transactional
    @Override
    public void updateCol(PatientWithCus patientWithCus) {

        PatientCus pc = patientCusRepository.findByCusIdAndPatientId(patientWithCus.getCusId(), patientWithCus.getPatientId()).orElseGet(PatientCus::new);

        // 值
        pc.setValue(patientWithCus.getValue());
        pc.setType(patientWithCus.getType());

        // 如果是字典, 则保持字典
        if (Objects.equals("dict", pc.getType())) {
            Long dictDetailId = null;
            DictDetail dictDetail = null;
            if (null != patientWithCus.getDictDetail()) {
                dictDetailId = patientWithCus.getDictDetail().getId();
            }
            if (null != dictDetailId) {
                dictDetail = dictDetailRepository.findById(dictDetailId).orElseGet(DictDetail::new);
            }
            if (null == dictDetail || null == dictDetail.getId()) {
                throw new RuntimeException("字典不能为空");
            }
            pc.setDictDetail(dictDetail);
        }

        // 设置自定义信息
        if (null == pc.getCus()) {
            Cus cus = cusRepository.findById(patientWithCus.getCusId()).orElseGet(Cus::new);
            if (cus.getId() == null) {
                throw new RuntimeException("Cus 不能为空");
            }
            pc.setCus(cus);
        }

        // 设置部门
        if (null == pc.getDept()) {
            Dept dept = deptRepository.findById(patientWithCus.getDeptId()).orElseGet(Dept::new);
            if (null == dept.getId()) {
                throw new RuntimeException("部门不能为空");
            }
            pc.setDept(dept);
        }

        // 设置患者
        if (null == pc.getPatient()) {
            Patient patient = repository.findById(patientWithCus.getPatientId()).orElseGet(Patient::new);
            if (patient.getId() == null) {
                throw new RuntimeException("患者不能为空");
            }
            pc.setPatient(patient);
        }

        patientCusRepository.save(pc);
    }

    @Override
    public void updateDept(Patient resources, PatientDto patientDto) {
        Patient patient = mapper.toEntity(patientDto);
        patient.setDepts(resources.getDepts());
        repository.save(patient);
    }

    @Override
    public Map<String, Object> syncData(JwtUserDto currentUser, HisCkItemVo vo) throws Exception {
        log.debug("syncData: in: " + vo);

        // 查询HIS系统
        List<HisCkItemDto> ckItemList = hisService.asyncQueryCkItemList(vo);
        log.debug("syncData: his 查询结果: " + ckItemList);
        if (ckItemList == null || ckItemList.isEmpty()) {
            throw new RuntimeException("查询 HIS 无数据");
        }

        // 当前用户部门
        Long userDeptId = currentUser.getUser().getDept().getId();
        log.info("当前用户部门: " + userDeptId);
        Dept userDept = deptRepository.findById(userDeptId).orElseGet(Dept::new);
        if (null == userDept.getId()) {
            throw new RuntimeException("当前用户部门为空");
        }

        // 更新 his_log
        List<HisLogDto> logList = hisLogService.createOrUpdate(ckItemList);
        log.debug("syncData: 保存查询结果: " + logList);

        // 保存或更新套餐项目信息
        Map<String, Term> termMap = termService.createOrUpdate(userDept, ckItemList);
        if (null == termMap || termMap.isEmpty()) {
            throw new RuntimeException("保存预约项目信息失败");
        }

        // 更新 patient
        Map<String, Patient> patientMap = createOrUpdate(userDept, ckItemList);
        if (null == patientMap || patientMap.isEmpty()) {
            throw new RuntimeException("保存患者信息失败");
        }

        // 更新 patient_term, patient_term_log
        for (HisCkItemDto ckItem : ckItemList) {
            Term term = termMap.get(ckItem.getItemCode());
            Patient patient = patientMap.get(ckItem.getPatientId().toString());
            patientTermService.createOrUpdate(userDept, patient, term, ckItem);
        }

        List<PatientDto> list = mapper.toDto(new ArrayList<>(patientMap.values()));
        return PageUtil.toPage(list, list.size());
    }

    @Transactional
    @Override
    public Map<String, Patient> createOrUpdate(Dept userDept, List<HisCkItemDto> ckItemList) {
        if (null == ckItemList || ckItemList.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Map<String, Patient> userMap = new HashMap<>();
        for (HisCkItemDto ckItem : ckItemList) {
            if (null == ckItem || null == ckItem.getPatientId()) continue;

            // 忽略刚刚添加过的数据
            if (userMap.containsKey(ckItem.getPatientId())) {
                continue;
            }

            Patient user = repository.findFirstByPatientId(ckItem.getPatientId()).orElseGet(Patient::new);
            if (user.getId() == null) {
                user.setPatientId(ckItem.getPatientId());
                user.setName(ckItem.getName());
                user.setPhone(ckItem.getMobilePhone());
                user.setMrn(ckItem.getMrn().toString());

                user = repository.save(user);
            }

            // 关联部门
            Set<Dept> depts = user.getDepts();
            if (null == depts) {
                depts = new HashSet<>();
            }
            depts.add(userDept);
            user.setDepts(depts);
            repository.save(user);

//            patientDeptService.createOrUpdate(userDept, user);

            userMap.put(user.getPatientId().toString(), user);
        }

        return userMap;
    }

    @Override
    public Map<String, Object> queryAll(PatientCriteria criteria, Pageable pageable) {
        Page<Patient> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public Map<String, Object> queryCols(PatientCriteria criteria, Pageable pageable) {
        final Long deptId = criteria.getDeptId();
        if (null == deptId) {
            throw new RuntimeException("部门不能为空");
        }

        // 查询列表
        Page<Patient> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        Page<PatientDto> pageDto = page.map(mapper::toDto);
        // 查询自定义数据
        pageDto = pageDto.map(new Function<PatientDto, PatientDto>() {
            @Override
            public PatientDto apply(PatientDto patientDto) {
                List<PatientCusDto> cols = patientCusMapper.toDto(patientCusRepository.findAllByDeptIdAndPatientId(deptId, patientDto.getId()));
                Map<String, PatientCusDto> colsMap = new HashMap<>();
                if (null != cols) {
                    for (int i = 0; i < cols.size(); i++) {
                        colsMap.put(cols.get(i).getCus().getId().toString(), cols.get(i));
                    }
                }
                patientDto.setCols(colsMap);
                return patientDto;
            }
        });

        return PageUtil.toPage(pageDto);
    }

    @Transactional
    @Override
    public void updateCols(PatientDto resources) {
        if (null == resources || null == resources.getId()) {
            throw new RuntimeException("患者信息不能为空");
        }
        if (null == resources.getDept() || null == resources.getDept().getId()) {
            throw new RuntimeException("患者所属医院不能为空");
        }
        Patient patient = repository.findById(resources.getId()).orElseGet(Patient::new);
        if (null == patient.getId()) {
            throw new RuntimeException("患者信息不存在");
        }
        Dept dept = deptRepository.findById(resources.getDept().getId()).orElseGet(Dept::new);
        if (null == dept.getId()) {
            throw new RuntimeException("患者所属医院不存在");
        }

        // 移除之前的值
        patientCusRepository.deleteAllByDeptIdAndPatientId(dept.getId(), patient.getId());

        List<PatientCusDto> patientCusDtoList = resources.getColList();
        if (null == patientCusDtoList || patientCusDtoList.isEmpty()) {
            log.info("没有提交任何自定义数据");
            return;
        }

        List<PatientCus> patientCusList = new ArrayList<>(patientCusDtoList.size());
        for (PatientCusDto patientCusDto : patientCusDtoList) {
            if (null == patientCusDto) {
                continue;
            }
            if (null == patientCusDto.getCus() || null == patientCusDto.getCus().getId()) {
                throw new RuntimeException("自定义列信息不能为空");
            }
            Cus cus = cusRepository.findById(patientCusDto.getCus().getId()).orElseGet(Cus::new);
            if (null == cus.getId()) {
                throw new RuntimeException("自定义列信息不存在");
            }
            PatientCus patientCus = new PatientCus();
            patientCus.setCus(cus);
            patientCus.setPatient(patient);
            patientCus.setDept(dept);
            patientCus.setType(cus.getType());
            // 保存值
            patientCus.setValue(patientCusDto.getValue());
            // 保存字典值
            patientCus.setDictDetail(null);
            if ("dict".equalsIgnoreCase(cus.getType())) {
                if (null != patientCusDto.getDictDetail() && null != patientCusDto.getDictDetail().getId()) {
                    DictDetail dictDetail = dictDetailRepository.findById(patientCusDto.getDictDetail().getId()).orElseGet(DictDetail::new);
                    if (null == dictDetail.getId()) {
                        throw new RuntimeException("自定义列字典值不存在");
                    }
                    patientCus.setDictDetail(dictDetail);
                    patientCus.setValue(dictDetail.getLabel());
                }
            }
            patientCusList.add(patientCus);
        }
        patientCusRepository.saveAll(patientCusList);
    }

    @Override
    public Map<String, Object> queryWithDept(PatientCriteria criteria, Pageable pageable) {
        Page<List<Map<String, Object>>> page;
        if (StringUtils.isNotEmpty(criteria.getName())) {
            page = repository.findWithDeptByName(criteria.getName(), pageable);
        } else {
            page = repository.findWithDept(pageable);
        }
        return PageUtil.toPage(page);
    }

    @Override
    public Map<String, Object> queryCusByPatientId(Long deptId, Long patientId, Pageable pageable) {
        Page<List<Map<String, Object>>> page = repository.findCusByPatientId(deptId, patientId, pageable);
        return PageUtil.toPage(page);
    }

    @Override
    public List<PatientDto> queryByDeptId(Long deptId) {
        return mapper.toDto(repository.findByDeptId(deptId));
    }

    @Override
    public List<PatientDto> queryAll(PatientCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public PatientDto findById(Long id) {
        Patient patient = repository.findById(id).orElseGet(Patient::new);
        ValidationUtil.isNull(patient.getId(), "Patient", "id", id);
        return mapper.toDto(patient);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PatientDto create(Patient resources) {
        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Patient resources) {
        Patient patient = repository.findById(resources.getId()).orElseGet(Patient::new);
        ValidationUtil.isNull(patient.getId(), "Patient", "id", resources.getId());
        patient.copy(resources);
        repository.save(patient);
    }

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
            map.put("状态", item.getStatus());
            map.put("创建人", item.getCreateBy());
            map.put("修改人", item.getUpdatedBy());
            map.put("创建时间", item.getCreateTime());
            map.put("修改时间", item.getUpdateTime());
            map.put("备注", item.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
