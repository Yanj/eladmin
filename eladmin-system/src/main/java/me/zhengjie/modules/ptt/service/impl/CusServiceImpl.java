package me.zhengjie.modules.ptt.service.impl;

import lombok.AllArgsConstructor;
import me.zhengjie.modules.ptt.domain.Cus;
import me.zhengjie.modules.ptt.repository.CusRepository;
import me.zhengjie.modules.ptt.service.CusService;
import me.zhengjie.modules.ptt.service.dto.CusCriteria;
import me.zhengjie.modules.ptt.service.dto.CusDto;
import me.zhengjie.modules.ptt.service.mapstruct.CusMapper;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.Dict;
import me.zhengjie.modules.system.repository.DeptRepository;
import me.zhengjie.modules.system.repository.DictRepository;
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
 * 自定义信息服务
 *
 * @author yanjun
 * @date 2020-11-28 11:17
 */
@Service
@AllArgsConstructor
public class CusServiceImpl implements CusService {

    private final CusRepository repository;
    private final CusMapper mapper;

    private final DeptRepository deptRepository;
    private final DictRepository dictRepository;

    @Override
    public Map<String, Object> queryAll(CusCriteria criteria, Pageable pageable) {
        Page<Cus> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder),
                pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<CusDto> queryAll(CusCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public CusDto findById(Long id) {
        Cus instance = repository.findById(id).orElseGet(Cus::new);
        ValidationUtil.isNull(instance.getId(), "Cus", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CusDto create(Cus resources) {
        // 获取部门
        Dept dept = resources.getDept();
        if (null == dept || null == dept.getId()) {
            throw new RuntimeException("部门不能为空");
        }
        dept = deptRepository.findById(dept.getId()).orElseGet(Dept::new);
        if (null == dept.getId()) {
            throw new RuntimeException("部门不存在");
        }

        Cus instance = new Cus();
        instance.copy(resources);
        instance.setDept(dept);

        if ("dict".equalsIgnoreCase(instance.getType())) {
            // 获取字典
            if (null == resources.getDict() || null == resources.getDict().getId()) {
                throw new RuntimeException("字典不能为空");
            }
            Dict dict = dictRepository.findById(resources.getDict().getId()).orElseGet(Dict::new);
            if (null == dict.getId()) {
                throw new RuntimeException("字典不存在");
            }
            instance.setDict(dict);
        } else {
            instance.setDict(null);
        }

        return mapper.toDto(repository.save(instance));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Cus resources) {
        Cus instance = repository.findById(resources.getId()).orElseGet(Cus::new);
        ValidationUtil.isNull(instance.getId(), "Cus", "id", resources.getId());
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
    public void download(List<CusDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CusDto item : all) {
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
