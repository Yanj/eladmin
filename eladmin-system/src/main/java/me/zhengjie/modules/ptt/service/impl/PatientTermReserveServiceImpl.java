package me.zhengjie.modules.ptt.service.impl;

import lombok.AllArgsConstructor;
import me.zhengjie.modules.ptt.domain.*;
import me.zhengjie.modules.ptt.repository.*;
import me.zhengjie.modules.ptt.service.PatientTermReserveService;
import me.zhengjie.modules.ptt.service.dto.PatientTermReserveCriteria;
import me.zhengjie.modules.ptt.service.dto.PatientTermReserveDto;
import me.zhengjie.modules.ptt.service.mapstruct.PatientTermReserveMapper;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.repository.DeptRepository;
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
import java.util.*;
import java.util.function.Supplier;

/**
 * 患者套餐预约服务
 *
 * @author yanjun
 * @date 2020-11-28 11:17
 */
@Service
@AllArgsConstructor
public class PatientTermReserveServiceImpl implements PatientTermReserveService {

    private final PatientTermReserveRepository repository;
    private final PatientTermReserveMapper mapper;

    private final PatientTermRepository patientTermRepository;
    private final PatientTermReserveLogRepository patientTermReserveLogRepository;

    private final DeptRepository deptRepository;
    private final ResourceRepository resourceRepository;
    private final PatientTermReserveResourceRepository patientTermReserveResourceRepository;
    private final TermRepository termRepository;
    private final PatientTermLogRepository patientTermLogRepository;

    @Override
    public Map<String, Object> queryAll(PatientTermReserveCriteria criteria, Pageable pageable) {
        Page<PatientTermReserve> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<PatientTermReserveDto> queryAll(PatientTermReserveCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public PatientTermReserveDto findById(Long id) {
        PatientTermReserve instance = repository.findById(id).orElseGet(PatientTermReserve::new);
        ValidationUtil.isNull(instance.getId(), "PatientTermReserve", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PatientTermReserveDto create(PatientTermReserve resources) {
        PatientTerm patientTerm = resources.getPatientTerm();
        if (null == patientTerm || null == patientTerm.getId()) {
            throw new RuntimeException("患者套餐不能为空");
        }
        patientTerm = patientTermRepository.findById(patientTerm.getId()).orElse(patientTerm);
        if (null == patientTerm.getId()) {
            throw new RuntimeException("患者套餐不存在");
        }
        if (patientTerm.getTerm() == null) {
            throw new RuntimeException("套餐不能为空");
        }

        Term term = termRepository.findById(patientTerm.getTerm().getId()).orElseGet(Term::new);
        if (null == term.getId()) {
            throw new RuntimeException("套餐不存在");
        }

        // 更新次数
        if (patientTerm.getTimes() <= 0) {
            throw new RuntimeException("次数已耗尽");
        }
        final int beforeTimes = patientTerm.getTimes();
        patientTerm.setTimes(beforeTimes - 1);
        patientTermRepository.save(patientTerm);

        Dept dept = resources.getDept();
        if (null != dept && null != dept.getId()) {
            dept = deptRepository.findById(dept.getId()).orElseGet(Dept::new);
        }
        if (null == dept.getId()) {
            throw new RuntimeException("医院不能为空");
        }
        resources.setDept(dept);
        resources.setTerm(patientTerm.getTerm());
        resources.setPatientTerm(patientTerm);
        resources.setStatus(PatientTermReserveDto.STATUS_INIT);

        PatientTermReserve reserve = repository.save(resources);

        // 保存日志
        PatientTermReserveLog log = new PatientTermReserveLog();
        log.setPatientTermReserve(reserve);
        log.setBeforeStatus(null);
        log.setAfterStatus(reserve.getStatus());
        log.setContent("新增");
        patientTermReserveLogRepository.save(log);

        // 更新次数日志
        PatientTermLog patientTermLog = new PatientTermLog();
        patientTermLog.setPatientTerm(patientTerm);
        patientTermLog.setTerm(term);
        patientTermLog.setPatientTermReserve(reserve);
        patientTermLog.setBeforeTimes(beforeTimes);
        patientTermLog.setAfterTimes(patientTerm.getTimes());
        patientTermLog.setContent("新增预约");
        patientTermLogRepository.save(patientTermLog);

        return mapper.toDto(reserve);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PatientTermReserveDto[] create(PatientTermReserve[] resources) {
        PatientTermReserveDto[] res = new PatientTermReserveDto[resources.length];
        for (int i = 0; i < resources.length; i++) {
            res[i] = create(resources[i]);
        }
        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(PatientTermReserve resources) {
        PatientTermReserve instance = repository.findById(resources.getId()).orElseGet(PatientTermReserve::new);
        ValidationUtil.isNull(instance.getId(), "PatientTermReserve", "id", resources.getId());
        instance.copy(resources);
        repository.save(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkIn(PatientTermReserve resources) {
        PatientTermReserve instance = repository.findById(resources.getId()).orElseGet(PatientTermReserve::new);
        ValidationUtil.isNull(instance.getId(), "PatientTermReserve", "id", resources.getId());

        if (!Objects.equals(PatientTermReserveDto.STATUS_INIT, instance.getStatus())) {
            throw new RuntimeException("状态不是初始状态");
        }

        // 修改状态
        String oldStatus = instance.getStatus();
        instance.setStatus(PatientTermReserveDto.STATUS_CHECKED);
        repository.save(instance);

        // log
        PatientTermReserveLog log = new PatientTermReserveLog();
        log.setPatientTermReserve(instance);
        log.setBeforeStatus(oldStatus);
        log.setAfterStatus(instance.getStatus());
        log.setContent("签到");
        patientTermReserveLogRepository.save(log);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancel(PatientTermReserve resources) {
        PatientTermReserve instance = repository.findById(resources.getId()).orElseGet(PatientTermReserve::new);
        ValidationUtil.isNull(instance.getId(), "PatientTermReserve", "id", resources.getId());

        if (!Objects.equals(PatientTermReserveDto.STATUS_INIT, instance.getStatus())) {
            throw new RuntimeException("状态不是初始状态");
        }

        PatientTerm patientTerm = instance.getPatientTerm();
        if (null == patientTerm || null == patientTerm.getId()) {
            throw new RuntimeException("患者套餐不能为空");
        }
        patientTerm = patientTermRepository.findById(patientTerm.getId()).orElseGet(PatientTerm::new);
        if (null == patientTerm.getId()) {
            throw new RuntimeException("患者套餐不存在");
        }

        Term term = patientTerm.getTerm();
        if (null == term || null == term.getId()) {
            throw new RuntimeException("套餐不能为空");
        }
        term = termRepository.findById(term.getId()).orElseGet(Term::new);
        if (null == term.getId()) {
            throw new RuntimeException("套餐不存在");
        }

        // 修改状态
        String oldStatus = instance.getStatus();
        instance.setStatus(PatientTermReserveDto.STATUS_CANCELED);
        repository.save(instance);

        // log
        PatientTermReserveLog log = new PatientTermReserveLog();
        log.setPatientTermReserve(instance);
        log.setBeforeStatus(oldStatus);
        log.setAfterStatus(instance.getStatus());
        log.setContent("取消");
        patientTermReserveLogRepository.save(log);

        // 修改次数
        int beforeTimes = patientTerm.getTimes();
        patientTerm.setTimes(beforeTimes + 1);
        patientTermRepository.save(patientTerm);

        // 更新次数日志
        PatientTermLog patientTermLog = new PatientTermLog();
        patientTermLog.setPatientTerm(patientTerm);
        patientTermLog.setTerm(term);
        patientTermLog.setPatientTermReserve(instance);
        patientTermLog.setBeforeTimes(beforeTimes);
        patientTermLog.setAfterTimes(patientTerm.getTimes());
        patientTermLog.setContent("新增预约");
        patientTermLogRepository.save(patientTermLog);
    }

    @Transactional
    @Override
    public void verify(PatientTermReserveResource[] resources) {
        if (null == resources || resources.length <= 0) {
            throw new IllegalArgumentException("resources 不能为空");
        }

        // 获取预约记录
        PatientTermReserve reserve = resources[0].getPatientTermReserve();
        if (null == reserve || null == reserve.getId()) {
            throw new IllegalStateException("PatientTermReserve 不能为空");
        }
        reserve = repository.findById(reserve.getId()).orElseGet(PatientTermReserve::new);
        if (null == reserve || null == reserve.getId()) {
            throw new IllegalStateException("PatientTermReserve 不存在");
        }

        // 资源列表
        List<PatientTermReserveResource> resourceList = new ArrayList<>(resources.length);
        for (int i = 0; i < resources.length; i++) {
            Resource resource = resources[i].getResource();
            if (null == resource || null == resource.getId()) {
                throw new IllegalStateException("Resource 不能为空");
            }

            resource = resourceRepository.findById(resource.getId()).orElseGet(Resource::new);
            if (null == resource || null == resource.getId()) {
                throw new IllegalStateException("Resource 不存在");
            }

            PatientTermReserveResource resourceItem = new PatientTermReserveResource();
            resourceItem.setPatientTermReserve(reserve);
            resourceItem.setResource(resource);
            resourceList.add(resourceItem);
        }
        // 保存资源列表
        patientTermReserveResourceRepository.saveAll(resourceList);

        // 修改状态
        String oldStatus = reserve.getStatus();
        reserve.setStatus(PatientTermReserveDto.STATUS_USED);
        repository.save(reserve);

        // log
        PatientTermReserveLog log = new PatientTermReserveLog();
        log.setPatientTermReserve(reserve);
        log.setBeforeStatus(oldStatus);
        log.setAfterStatus(reserve.getStatus());
        log.setContent("核销");
        patientTermReserveLogRepository.save(log);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            repository.deleteById(id);
        }
    }

    @Override
    public void download(List<PatientTermReserveDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PatientTermReserveDto item : all) {
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
