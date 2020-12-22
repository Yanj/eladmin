package me.zhengjie.modules.ptt.service.impl;

import lombok.AllArgsConstructor;
import me.zhengjie.modules.ptt.domain.PatientFollow;
import me.zhengjie.modules.ptt.repository.PatientFollowRepository;
import me.zhengjie.modules.ptt.service.PatientFollowService;
import me.zhengjie.modules.ptt.service.dto.PatientFollowCriteria;
import me.zhengjie.modules.ptt.service.dto.PatientFollowDto;
import me.zhengjie.modules.ptt.service.mapstruct.PatientFollowMapper;
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
 * 患者跟进服务
 *
 * @author yanjun
 * @date 2020-11-28 11:17
 */
@Service
@AllArgsConstructor
public class PatientFollowServiceImpl implements PatientFollowService {

    private final PatientFollowRepository repository;
    private final PatientFollowMapper mapper;

    @Override
    public Map<String, Object> queryAll(PatientFollowCriteria criteria, Pageable pageable) {
        Page<PatientFollow> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<PatientFollowDto> queryAll(PatientFollowCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public PatientFollowDto findById(Long id) {
        PatientFollow instance = repository.findById(id).orElseGet(PatientFollow::new);
        ValidationUtil.isNull(instance.getId(), "PatientFollow", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PatientFollowDto create(PatientFollow resources) {
        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(PatientFollow resources) {
        PatientFollow instance = repository.findById(resources.getId()).orElseGet(PatientFollow::new);
        ValidationUtil.isNull(instance.getId(), "PatientFollow", "id", resources.getId());
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
    public void download(List<PatientFollowDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PatientFollowDto item : all) {
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
