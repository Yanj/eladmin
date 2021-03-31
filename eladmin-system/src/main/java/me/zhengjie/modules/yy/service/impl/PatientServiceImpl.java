package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.vo.HisCkItemVo;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.modules.yy.domain.Patient;
import me.zhengjie.modules.yy.domain.PatientSourceEnum;
import me.zhengjie.modules.yy.domain.PatientTerm;
import me.zhengjie.modules.yy.domain.PatientTermType;
import me.zhengjie.modules.yy.repository.PatientRepository;
import me.zhengjie.modules.yy.service.HisLogService;
import me.zhengjie.modules.yy.service.PatientService;
import me.zhengjie.modules.yy.service.PatientTermService;
import me.zhengjie.modules.yy.service.dto.PatientCriteria;
import me.zhengjie.modules.yy.service.dto.PatientDto;
import me.zhengjie.modules.yy.service.dto.PatientSync;
import me.zhengjie.modules.yy.service.mapstruct.PatientMapper;
import me.zhengjie.service.HisService;
import me.zhengjie.service.dto.HisCkItemDto;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;
    private final PatientMapper mapper;

    private final DeptService deptService;

    private final HisService hisService;
    private final HisLogService hisLogService;
    private final PatientTermService patientTermService;

    @Override
    public PatientDto syncLocal(PatientSync patientSync) {
        PatientCriteria criteria = new PatientCriteria();
        if (!criteria.setPatientSync(patientSync)) {
            throw new BadRequestException("查询参数错误");
        }
        criteria.setUser(SecurityUtils.getCurrentUser());
        criteria.setStatus(YesNoEnum.YES);
        List<Patient> list = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        if (list.isEmpty()) {
            throw new BadRequestException("患者未找到");
        }
        if (list.size() > 1) {
            throw new BadRequestException("患者数据重复, 请联系管理员");
        }
        return mapper.toDto(list.get(0));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PatientDto sync(PatientCriteria criteria) throws Exception {
        // 同步HIS查询患者信息
        HisCkItemVo hisCkItemVo = new HisCkItemVo();
        if (StringUtils.isNotEmpty(criteria.getMrn())) {
            hisCkItemVo.setInfoType(HisCkInfoTypeEnum.MRN);
            hisCkItemVo.setPatientInfo(criteria.getMrn());
        } else if (StringUtils.isNotEmpty(criteria.getPhone())) {
            hisCkItemVo.setInfoType(HisCkInfoTypeEnum.PHONE);
            hisCkItemVo.setPatientInfo(criteria.getPhone());
        } else if (StringUtils.isNotEmpty(criteria.getName())) {
            hisCkItemVo.setInfoType(HisCkInfoTypeEnum.NAME);
            hisCkItemVo.setPatientInfo(criteria.getName());
        } else {
            throw new BadRequestException("查询类型错误: ");
        }
        List<HisCkItemDto> ckItemList = hisService.asyncQueryCkItemList(hisCkItemVo);

        log.debug("syncData: his 查询结果: " + ckItemList);
        if (ckItemList == null || ckItemList.isEmpty()) {
            throw new RuntimeException("查询 HIS 无数据");
        }

        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        if (null == user) {
            throw new RuntimeException("获取用户失败");
        }

        Long orgId = criteria.getOrgId();
        Long comId = criteria.getComId();
        Long deptId = criteria.getDeptId();
        if (!user.isAdmin()) {
            orgId = user.getOrgId();
            comId = user.getComId();
            deptId = user.getDeptId();
        }
        if (null == orgId) {
            throw new BadRequestException("orgId 不能为空");
        }
        if (null == comId) {
            throw new BadRequestException("comId 不能为空");
        }

        // 保存查询日志
        hisLogService.create(ckItemList);

        // 同步信息
        Patient patient = null;
        for (HisCkItemDto ckItem : ckItemList) {
            // 新增患者
            patient = new Patient();
            patient.setOrgId(orgId);
            patient.setComId(comId);
            patient.setDeptId(deptId);
            patient.setSource(PatientSourceEnum.HIS);
            patient.setCode(ckItem.getPatientId().toString());
            patient.setMrn(ckItem.getMrn());
            patient.setName(ckItem.getName());
            patient.setPhone(ckItem.getMobilePhone());
            patient = mapper.toEntity(create(patient));

            // 新增患者套餐
            PatientTerm patientTerm = new PatientTerm();
            patientTerm.setOrgId(orgId);
            patientTerm.setComId(comId);
            patientTerm.setDeptId(deptId);
            patientTerm.setType(PatientTermType.his);
            patientTerm.setPatItemId(ckItem.getPatItemId().toString());
            patientTerm.setPatient(patient);
            patientTerm.setTermCode(ckItem.getItemCode());
            patientTerm.setPrice(ckItem.getActualCosts());
            patientTerm.setTotalTimes(ckItem.getAmount().intValue());
            patientTerm.setTimes(ckItem.getAmount().intValue());
            patientTermService.create(patientTerm);
        }
        if (null == patient) {
            throw new BadRequestException("患者同步失败");
        }

        return mapper.toDto(patient);
    }

    @Override
    public Map<String, Object> queryAll(PatientCriteria criteria, Pageable pageable) {
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        criteria.setUser(user);
        // 非管理员, 只能看到"未删除"的数据
        if (!user.isAdmin()) {
            criteria.setStatus(YesNoEnum.YES);
        }
        Page<Patient> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<PatientDto> queryAll(PatientCriteria criteria) {
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
    public PatientDto findById(Long id) {
        Patient instance = repository.findById(id).orElseGet(Patient::new);
        ValidationUtil.isNull(instance.getId(), "Patient", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PatientDto create(Patient resources) {
        if (StringUtils.isEmpty(resources.getName())) {
            throw new BadRequestException("患者名称不能为空");
        }
        if (StringUtils.isEmpty(resources.getPhone())) {
            throw new BadRequestException("患者电话不能为空");
        }

        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        // 如果不是管理员, 直接设置当前用户的部门
        if (!user.isAdmin()) {
            resources.setOrgId(user.getOrgId());
            resources.setComId(user.getComId());
            resources.setDeptId(user.getDeptId());
        }

        // 检查组织 ID
        if (null == resources.getOrgId()) {
            throw new BadRequestException("orgId 不能为空");
        }
        deptService.findById(resources.getOrgId());

        // 检查医院 ID
        if (null == resources.getComId()) {
            throw new BadRequestException("comId 不能为空");
        }
        deptService.findById(resources.getComId());

        // 如果传入了部门 ID, 检查部门 ID
        if (null != resources.getDeptId()) {
            deptService.findById(resources.getDeptId());
        }

        // 设置患者来源
        if (null == resources.getSource()) {
            resources.setSource(PatientSourceEnum.HIS);
        }

        if (resources.getSource() == PatientSourceEnum.HIS) {
            if (StringUtils.isEmpty(resources.getCode())) {
                throw new BadRequestException("患者外部ID不能为空");
            }
            if (StringUtils.isEmpty(resources.getMrn())) {
                throw new BadRequestException("患者档案编号不能为空");
            }

            // 查询患者是否已存在
            Patient patient = findByCodeAndMrn(resources, resources.getCode(), resources.getMrn());
            if (null != patient) {
                return mapper.toDto(patient);
            }

            // 检查外部系统 ID
            checkCode(resources, resources.getCode());

            // 检查 MRN
            checkMrn(resources, resources.getMrn());
        }

        // 检查患者手机号
        checkPhone(resources, resources.getPhone());

        // 设置患者状态
        if (resources.getStatus() == null) {
            resources.setStatus(YesNoEnum.YES);
        }

        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Patient resources) {
        Patient instance = repository.findById(resources.getId()).orElseGet(Patient::new);
        ValidationUtil.isNull(instance.getId(), "Patient", "id", resources.getId());

        // 手机号被修改, 重复检查
        if (!StringUtils.equals(instance.getPhone(), resources.getPhone())) {
            checkPhone(instance, resources.getPhone());
        }
        instance.copy(resources);
        repository.save(instance);
    }

    private Patient findByCodeAndMrn(Patient instance, String code, String mrn) {
        PatientCriteria criteria = new PatientCriteria();
        criteria.setOrgId(instance.getOrgId());
        criteria.setComId(instance.getComId());
        criteria.setDeptId(instance.getDeptId());
        criteria.setSource(PatientSourceEnum.HIS);
        criteria.setCode(code);
        criteria.setMrn(mrn);
        List<Patient> list = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    private void checkCode(Patient instance, String code) {
        PatientCriteria criteria = new PatientCriteria();
        criteria.setOrgId(instance.getOrgId());
        criteria.setComId(instance.getComId());
        criteria.setDeptId(instance.getDeptId());
        criteria.setCode(code);
        List<Patient> list = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));

        // 患者已存在
        if (!list.isEmpty()) {
            throw new BadRequestException("患者外部系统ID:" + code + ", 已存在");
        }
    }

    private void checkMrn(Patient instance, String mrn) {
        PatientCriteria criteria = new PatientCriteria();
        criteria.setOrgId(instance.getOrgId());
        criteria.setComId(instance.getComId());
        criteria.setDeptId(instance.getDeptId());
        criteria.setMrn(mrn);
        List<Patient> list = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));

        // 患者已存在
        if (!list.isEmpty()) {
            throw new BadRequestException("患者档案号:" + mrn + ", 已存在");
        }
    }

    private void checkPhone(Patient instance, String phone) {
        PatientCriteria criteria = new PatientCriteria();
        criteria.setOrgId(instance.getOrgId());
        criteria.setComId(instance.getComId());
        criteria.setDeptId(instance.getDeptId());
        criteria.setPhone(phone);
        List<Patient> list = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));

        // 患者已存在
        if (!list.isEmpty()) {
            throw new BadRequestException("患者电话:" + phone + ", 已存在");
        }
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
                Patient instance = repository.findById(id).orElseGet(Patient::new);
                ValidationUtil.isNull(instance.getId(), "Patient", "id", id);
                instance.setStatus(YesNoEnum.NO);
                repository.save(instance);
            }
        }
    }

    @Override
    public void download(List<PatientDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PatientDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("ID", item.getId());
            map.put("来源", item.getSource());
            map.put("外部系统ID", item.getCode());
            map.put("档案号", item.getMrn());
            map.put("姓名", item.getName());
            map.put("电话", item.getPhone());
            map.put("自定义1", item.getCol1());
            map.put("自定义2", item.getCol2());
            map.put("自定义3", item.getCol3());
            map.put("自定义4", item.getCol4());
            map.put("自定义5", item.getCol5());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
