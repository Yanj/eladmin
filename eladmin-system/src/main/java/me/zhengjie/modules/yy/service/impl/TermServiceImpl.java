package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.yy.domain.Term;
import me.zhengjie.modules.yy.repository.TermRepository;
import me.zhengjie.modules.yy.service.TermService;
import me.zhengjie.modules.yy.service.dto.TermCriteria;
import me.zhengjie.modules.yy.service.dto.TermDto;
import me.zhengjie.modules.yy.service.mapstruct.TermMapper;
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
public class TermServiceImpl implements TermService {

    private final TermRepository repository;
    private final TermMapper mapper;

    @Override
    public Map<String, Object> queryAll(TermCriteria criteria, Pageable pageable) {
        Page<Term> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<TermDto> queryAll(TermCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public TermDto findById(Long id) {
        Term instance = repository.findById(id).orElseGet(Term::new);
        ValidationUtil.isNull(instance.getId(), "Term", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TermDto create(Term resources) {
//        // 金额处理
//        resources.setPrice(resources.getPrice() * 100);
//        resources.setOriginalPrice(resources.getOriginalPrice() * 100);
        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Term resources) {
        Term instance = repository.findById(resources.getId()).orElseGet(Term::new);
        ValidationUtil.isNull(instance.getId(), "Term", "id", resources.getId());
        instance.copy(resources);
//        // 金额处理
//        instance.setPrice(resources.getPrice() * 100);
//        instance.setOriginalPrice(resources.getOriginalPrice() * 100);
        repository.save(instance);
    }

    @Transactional
    @Override
    public void updateResourceCategory(Term resources, TermDto resourceGroupDto) {
        Term term = mapper.toEntity(resourceGroupDto);
        term.setResourceGroups(resources.getResourceGroups());
        repository.save(term);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            repository.deleteById(id);
        }
    }

    @Override
    public void download(List<TermDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TermDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", item.getId());
            map.put("状态", item.getStatus());
            map.put("备注", item.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
