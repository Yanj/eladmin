package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.yy.domain.PatientTerm;
import me.zhengjie.modules.yy.repository.PatientTermRepository;
import me.zhengjie.modules.yy.service.PatientTermService;
import me.zhengjie.modules.yy.service.dto.PatientTermCriteria;
import me.zhengjie.modules.yy.service.dto.PatientTermDto;
import me.zhengjie.modules.yy.service.mapstruct.PatientTermMapper;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
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
        if (originalPatientTerm.getParent() != null) {
            throw new RuntimeException("免费套餐不运行继续赠送");
        }

        if (null != originalPatientTerm.getFreeTimes() && originalPatientTerm.getFreeTimes() >= times) {
            throw new RuntimeException("该套餐已免费赠送免费次数: " + originalPatientTerm.getFreeTimes() + "次, 无法继续赠送");
        }

        PatientTerm patientTerm = new PatientTerm();
        patientTerm.setParent(originalPatientTerm);
        patientTerm.setPatient(originalPatientTerm.getPatient());
        patientTerm.setTermCode(originalPatientTerm.getTermCode());
        patientTerm.setTermName(originalPatientTerm.getTermName());
        patientTerm.setTermOriginalPrice(originalPatientTerm.getTermOriginalPrice());
        patientTerm.setTermPrice(originalPatientTerm.getTermPrice());
        patientTerm.setTermTimes(originalPatientTerm.getTermTimes());
        patientTerm.setTermUnit(originalPatientTerm.getTermUnit());
        patientTerm.setPrice(0L);
        patientTerm.setTimes(times);
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
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
