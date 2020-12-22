package me.zhengjie.modules.ptt.service.impl;

import lombok.AllArgsConstructor;
import me.zhengjie.modules.ptt.domain.PatientCus;
import me.zhengjie.modules.ptt.repository.PatientCusRepository;
import me.zhengjie.modules.ptt.service.PatientCusService;
import me.zhengjie.modules.ptt.service.dto.PatientCusCriteria;
import me.zhengjie.modules.ptt.service.dto.PatientCusDto;
import me.zhengjie.modules.ptt.service.mapstruct.PatientCusMapper;
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
 * 患者自定义信息服务
 *
 * @author yanjun
 * @date 2020-11-28 11:17
 */
@Service
@AllArgsConstructor
public class PatientCusServiceImpl implements PatientCusService {

    private final PatientCusRepository repository;
    private final PatientCusMapper mapper;

    @Override
    public Map<String, Object> queryAll(PatientCusCriteria criteria, Pageable pageable) {
        Page<PatientCus> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<PatientCusDto> queryAll(PatientCusCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public PatientCusDto findById(Long id) {
        PatientCus instance = repository.findById(id).orElseGet(PatientCus::new);
        ValidationUtil.isNull(instance.getId(), "PatientCus", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PatientCusDto create(PatientCus resources) {
        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(PatientCus resources) {
        PatientCus instance = repository.findById(resources.getId()).orElseGet(PatientCus::new);
        ValidationUtil.isNull(instance.getId(), "PatientCus", "id", resources.getId());
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
    public void download(List<PatientCusDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PatientCusDto item : all) {
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
