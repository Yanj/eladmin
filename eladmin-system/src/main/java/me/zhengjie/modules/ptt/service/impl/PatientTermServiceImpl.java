package me.zhengjie.modules.ptt.service.impl;

import lombok.AllArgsConstructor;
import me.zhengjie.modules.ptt.domain.Patient;
import me.zhengjie.modules.ptt.domain.PatientTerm;
import me.zhengjie.modules.ptt.domain.PatientTermLog;
import me.zhengjie.modules.ptt.domain.Term;
import me.zhengjie.modules.ptt.repository.PatientTermRepository;
import me.zhengjie.modules.ptt.service.PatientTermLogService;
import me.zhengjie.modules.ptt.service.PatientTermService;
import me.zhengjie.modules.ptt.service.dto.PatientTermCriteria;
import me.zhengjie.modules.ptt.service.dto.PatientTermDto;
import me.zhengjie.modules.ptt.service.mapstruct.PatientTermMapper;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.service.dto.HisCkItemDto;
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
 * 患者套餐服务
 *
 * @author yanjun
 * @date 2020-11-28 11:17
 */
@Service
@AllArgsConstructor
public class PatientTermServiceImpl implements PatientTermService {

    private final PatientTermRepository repository;
    private final PatientTermMapper mapper;

    private final PatientTermLogService patientTermLogService;

    @Transactional
    @Override
    public void createOrUpdate(Dept userDept, Patient patient, Term term, HisCkItemDto ckItem) {
        PatientTerm patientTerm = repository.findFirstByPatientIdAndTermId(patient.getId(), term.getId()).orElseGet(PatientTerm::new);
        if (patientTerm.getId() == null) {
            patientTerm.setPatient(patient);
            patientTerm.setTerm(term);
            patientTerm.setTermCode(term.getCode());
            patientTerm.setTermName(term.getName());
            patientTerm.setTermDescription(term.getDescription());
            patientTerm.setTermDuration(term.getDuration());
            patientTerm.setTermPrice(term.getPrice());
            patientTerm.setTermAmount(term.getAmount());
            patientTerm.setTermTimes(term.getTimes());
            patientTerm.setTermUnit(term.getUnit());
            patientTerm.setTimes(term.getTimes());
            patientTerm.setAmount(ckItem.getActualCosts());
            patientTerm.setLastDept(userDept);
            patientTerm.setStatus("1");
            create(patientTerm);

            // 更新 log
            PatientTermLog log = new PatientTermLog();
            log.setPatientTerm(patientTerm);
            log.setBeforeTimes(0);
            log.setAfterTimes(patientTerm.getTimes());
            log.setContent("新增");
            log.setTerm(term);
            log.setStatus("1");
            patientTermLogService.create(log);
        }
    }

    @Override
    public Map<String, Object> queryAll(PatientTermCriteria criteria, Pageable pageable) {
        Page<PatientTerm> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<PatientTermDto> queryAll(PatientTermCriteria criteria) {
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
        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(PatientTerm resources) {
        PatientTerm instance = repository.findById(resources.getId()).orElseGet(PatientTerm::new);
        ValidationUtil.isNull(instance.getId(), "PatientTerm", "id", resources.getId());
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
    public void download(List<PatientTermDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PatientTermDto item : all) {
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
