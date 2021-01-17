package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.service.dto.DeptDto;
import me.zhengjie.modules.yy.domain.PatientCol;
import me.zhengjie.modules.yy.repository.PatientColRepository;
import me.zhengjie.modules.yy.service.HospitalService;
import me.zhengjie.modules.yy.service.PatientColService;
import me.zhengjie.modules.yy.service.dto.PatientColCriteria;
import me.zhengjie.modules.yy.service.dto.PatientColDto;
import me.zhengjie.modules.yy.service.mapstruct.PatientColMapper;
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
public class PatientColServiceImpl implements PatientColService {

    private final PatientColRepository repository;
    private final PatientColMapper mapper;

    private final HospitalService hospitalService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void syncData() {
        List<DeptDto> list = hospitalService.queryAll();
        for (DeptDto dept : list) {
            repository.insertByDeptId(dept.getId());
        }
    }

    @Override
    public Map<String, Object> queryAll(PatientColCriteria criteria, Pageable pageable) {
        Page<PatientCol> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<PatientColDto> queryAll(PatientColCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public PatientColDto findById(Long id) {
        PatientCol instance = repository.findById(id).orElseGet(PatientCol::new);
        ValidationUtil.isNull(instance.getId(), "PatientCol", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PatientColDto create(PatientCol resources) {
        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(PatientCol resources) {
        PatientCol instance = repository.findById(resources.getId()).orElseGet(PatientCol::new);
        ValidationUtil.isNull(instance.getId(), "PatientCol", "id", resources.getId());
        instance.copy(resources);
        repository.save(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            // 更新状态
//            repository.updateStatus(id, "0");
            repository.deleteById(id);
        }
    }

    @Override
    public void download(List<PatientColDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PatientColDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", item.getId());
            map.put("状态", item.getStatus());
            map.put("备注", item.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
