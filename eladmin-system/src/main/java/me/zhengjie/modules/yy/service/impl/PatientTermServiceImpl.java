package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.modules.yy.domain.PatientTerm;
import me.zhengjie.modules.yy.domain.PatientTermTimesCount;
import me.zhengjie.modules.yy.domain.PatientTermType;
import me.zhengjie.modules.yy.repository.PatientTermRepository;
import me.zhengjie.modules.yy.service.PatientTermService;
import me.zhengjie.modules.yy.service.TermService;
import me.zhengjie.modules.yy.service.dto.*;
import me.zhengjie.modules.yy.service.mapstruct.PatientTermMapper;
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
public class PatientTermServiceImpl implements PatientTermService {

    private final PatientTermRepository repository;
    private final PatientTermMapper mapper;

    private final TermService termService;
    private final DeptService deptService;

    @Override
    public Map<String, Object> queryAll(PatientTermCriteria criteria, Pageable pageable) {
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        // 如果是根据患者来查询则不判断部门
        if (criteria.getPatientId() == null) {
            criteria.setUser(user);
        }
        // 非管理员, 只能看到"未删除"的数据
        if (!user.isAdmin()) {
            criteria.setStatus(YesNoEnum.YES);
        }
        Page<PatientTerm> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<PatientTermDto> queryAll(PatientTermCriteria criteria) {
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
    public PatientTermDto findById(Long id) {
        PatientTerm instance = repository.findById(id).orElseGet(PatientTerm::new);
        ValidationUtil.isNull(instance.getId(), "PatientTerm", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PatientTermDto create(PatientTerm resources) {
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

        // 如果是 his 类型
        if (PatientTermType.his == resources.getType()) {
            if (null == resources.getPatItemId() || StringUtils.isEmpty(resources.getPatItemId())) {
                throw new RuntimeException("外部系统ID 不能为空");
            }
            PatientTerm patientTerm = findByPatItemId(resources, resources.getPatItemId());
            if (null != patientTerm) {
                return mapper.toDto(patientTerm);
            }
        }
        // 免费类型
        else {
            resources.setType(PatientTermType.free);
            resources.setPatItemId(null);
        }

        // 如果提供了 套餐ID, 则查询套餐信息并设置
        if (null != resources.getTermId()) {
            TermDto term = termService.findById(resources.getTermId());
            setTermInfo(resources, term);
        }
        // 如果提供了 套餐CODE, 则查询套餐信息并设置
        else if (null != resources.getTermCode() && !StringUtils.isEmpty(resources.getTermCode())) {
            TermDto term = findTermByCode(resources, resources.getTermCode());
            if (null == term) {
                throw new BadRequestException("套餐不存在: " + resources.getTermCode());
            }
            setTermInfo(resources, term);
        }
        // 否则无法查询套餐信息, 报错
        else {
            throw new BadRequestException("套餐关键信息缺失");
        }

        // 状态
        if (null == resources.getStatus()) {
            resources.setStatus(YesNoEnum.YES);
        }

        return mapper.toDto(repository.save(resources));
    }

    private PatientTerm findByPatItemId(PatientTerm resources, String patItemId) {
        PatientTermCriteria criteria = new PatientTermCriteria();
        criteria.setOrgId(resources.getOrgId());
        criteria.setComId(resources.getComId());
        criteria.setDeptId(resources.getDeptId());
        criteria.setPatItemId(resources.getPatItemId());
        List<PatientTerm> list = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria,
                criteriaBuilder));
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    private TermDto findTermByCode(PatientTerm resources, String code) {
        TermCriteria criteria = new TermCriteria();
        criteria.setOrgId(resources.getOrgId());
        criteria.setComId(resources.getComId());
        criteria.setDeptId(resources.getDeptId());
        criteria.setCode(code);
        List<TermDto> list = termService.queryAll(criteria);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    private void setTermInfo(PatientTerm resources, TermDto term) {
        resources.setTermId(term.getId());
        resources.setTermCode(term.getCode());
        resources.setTermName(term.getName());
        resources.setTermPrice(term.getPrice());
        resources.setTermOriginalPrice(term.getOriginalPrice());
        resources.setTermTimes(term.getTimes());
        resources.setTermUnit(term.getUnit());
        resources.setTermDuration(term.getDuration());
        resources.setTermOperatorCount(term.getOperatorCount());
        resources.setDuration(term.getDuration());
        resources.setOperatorCount(term.getOperatorCount());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PatientTermDto createFreeOne(PatientTerm resources) {
        return createFree(resources, 1);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PatientTermDto createFreeTwo(PatientTerm resources) {
        return createFree(resources, 2);
    }

    @Transactional(rollbackFor = Exception.class)
    PatientTermDto createFree(PatientTerm resources, int times) {
        if (null == resources.getId()) {
            throw new RuntimeException("患者套餐不存在");
        }
        PatientTerm originalPatientTerm = repository.findById(resources.getId()).orElseGet(PatientTerm::new);
        if (null == originalPatientTerm.getId()) {
            throw new RuntimeException("患者套餐不存在");
        }
        if (originalPatientTerm.getPid() != null) {
            throw new RuntimeException("免费套餐不运行继续赠送");
        }

        if (null != originalPatientTerm.getFreeTimes() && originalPatientTerm.getFreeTimes() >= times) {
            throw new RuntimeException("该套餐已免费赠送免费次数: " + originalPatientTerm.getFreeTimes() + "次, 无法继续赠送");
        }

        PatientTerm patientTerm = new PatientTerm();
        patientTerm.setOrgId(originalPatientTerm.getOrgId());
        patientTerm.setComId(originalPatientTerm.getComId());
        patientTerm.setDeptId(originalPatientTerm.getDeptId());
        patientTerm.setPid(originalPatientTerm.getId());
        patientTerm.setType(PatientTermType.free);
        patientTerm.setPatient(originalPatientTerm.getPatient());
        patientTerm.setTermId(originalPatientTerm.getTermId());
        patientTerm.setTermCode(originalPatientTerm.getTermCode());
        patientTerm.setTermName(originalPatientTerm.getTermName());
        patientTerm.setTermOriginalPrice(originalPatientTerm.getTermOriginalPrice());
        patientTerm.setTermPrice(originalPatientTerm.getTermPrice());
        patientTerm.setTermTimes(originalPatientTerm.getTermTimes());
        patientTerm.setTermUnit(originalPatientTerm.getTermUnit());
        patientTerm.setTermDuration(originalPatientTerm.getTermDuration());
        patientTerm.setTermOperatorCount(originalPatientTerm.getTermOperatorCount());
        patientTerm.setPrice(0L);
        patientTerm.setTotalTimes(times);
        patientTerm.setTimes(times);
        patientTerm.setDuration(originalPatientTerm.getDuration());
        patientTerm.setOperatorCount(originalPatientTerm.getOperatorCount());
        patientTerm.setStatus(YesNoEnum.YES);
        patientTerm.setRemark("赠送");

        PatientTermDto res = mapper.toDto(repository.save(patientTerm));

        // 更新次数
        int freeTimes = null == originalPatientTerm.getFreeTimes() ? 0 : originalPatientTerm.getFreeTimes();
        originalPatientTerm.setFreeTimes(freeTimes + times);
        repository.save(originalPatientTerm);

        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(PatientTerm resources) {
        PatientTerm instance = repository.findById(resources.getId()).orElseGet(PatientTerm::new);
        ValidationUtil.isNull(instance.getId(), "PatientTerm", "id", resources.getId());
        instance.copy(resources);
        repository.save(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            PatientTerm instance = repository.findById(id).orElseGet(PatientTerm::new);
            ValidationUtil.isNull(instance.getId(), "PatientTerm", "id", id);
            instance.setStatus(YesNoEnum.NO);
            repository.save(instance);
        }
    }

    @Override
    public void download(List<PatientTermDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PatientTermDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("ID", item.getId());
            map.put("组织ID", item.getOrgId());
            map.put("公司ID", item.getComId());
            map.put("部门ID", item.getDeptId());
            map.put("外部系统ID", item.getPatItemId());
            map.put("类型", item.getType());
            map.put("赠送套餐ID", item.getPid());
            map.put("患者ID", item.getPatient().getId());
            map.put("套餐ID", item.getTermId());
            map.put("套餐外部系统ID", item.getTermCode());
            map.put("套餐名称", item.getTermName());
            map.put("套餐价格", item.getTermPrice());
            map.put("套餐原价", item.getTermOriginalPrice());
            map.put("套餐次数", item.getTermTimes());
            map.put("套餐单位", item.getTermUnit());
            map.put("购买价格", item.getPrice());
            map.put("总次数", item.getTotalTimes());
            map.put("剩余次数", item.getTimes());
            map.put("赠送次数", item.getFreeTimes());
            map.put("状态", item.getStatus());
            map.put("备注", item.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
