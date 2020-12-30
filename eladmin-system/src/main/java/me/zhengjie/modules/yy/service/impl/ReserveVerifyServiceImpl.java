package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.yy.domain.ReserveVerify;
import me.zhengjie.modules.yy.repository.ReserveVerifyRepository;
import me.zhengjie.modules.yy.service.ReserveVerifyService;
import me.zhengjie.modules.yy.service.dto.ReserveVerifyCriteria;
import me.zhengjie.modules.yy.service.dto.ReserveVerifyDto;
import me.zhengjie.modules.yy.service.mapstruct.ReserveVerifyMapper;
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
public class ReserveVerifyServiceImpl implements ReserveVerifyService {

    private final ReserveVerifyRepository repository;
    private final ReserveVerifyMapper mapper;

    @Override
    public Map<String, Object> queryAll(ReserveVerifyCriteria criteria, Pageable pageable) {
        Page<ReserveVerify> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<ReserveVerifyDto> queryAll(ReserveVerifyCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public ReserveVerifyDto findById(Long id) {
        ReserveVerify instance = repository.findById(id).orElseGet(ReserveVerify::new);
        ValidationUtil.isNull(instance.getId(), "ReserveVerify", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReserveVerifyDto create(ReserveVerify resources) {
        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(ReserveVerify resources) {
        ReserveVerify instance = repository.findById(resources.getId()).orElseGet(ReserveVerify::new);
        ValidationUtil.isNull(instance.getId(), "ReserveVerify", "id", resources.getId());
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
    public void download(List<ReserveVerifyDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReserveVerifyDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", item.getId());
            map.put("状态", item.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

}
