package me.zhengjie.modules.ptt.service.impl;

import lombok.AllArgsConstructor;
import me.zhengjie.modules.ptt.domain.Resource;
import me.zhengjie.modules.ptt.repository.ResourceRepository;
import me.zhengjie.modules.ptt.service.ResourceService;
import me.zhengjie.modules.ptt.service.dto.ResourceCriteria;
import me.zhengjie.modules.ptt.service.dto.ResourceDto;
import me.zhengjie.modules.ptt.service.mapstruct.ResourceMapper;
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

/**
 * 资源服务
 *
 * @author yanjun
 * @date 2020-11-28 11:17
 */
@Service
@AllArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository repository;
    private final ResourceMapper mapper;

    @Override
    public Map<String, List<ResourceDto>> queryByDeptId(ResourceCriteria criteria) {
        List<ResourceDto> list = mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria,
                criteriaBuilder)));
        Map<String, List<ResourceDto>> map = new HashMap<>();
        for (ResourceDto item : list) {
            if (null == item.getResourceType() || null == item.getResourceType().getId()) {
                continue;
            }

            String resourceTypeId = item.getResourceType().getId().toString();
            List<ResourceDto> tempList = map.computeIfAbsent(resourceTypeId, k -> new ArrayList<>());
            tempList.add(item);
        }
        return map;
    }

    @Override
    public Map<String, Object> queryAll(ResourceCriteria criteria, Pageable pageable) {
        Page<Resource> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<ResourceDto> queryAll(ResourceCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public ResourceDto findById(Long id) {
        Resource instance = repository.findById(id).orElseGet(Resource::new);
        ValidationUtil.isNull(instance.getId(), "Resource", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResourceDto create(Resource resources) {
        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Resource resources) {
        Resource instance = repository.findById(resources.getId()).orElseGet(Resource::new);
        ValidationUtil.isNull(instance.getId(), "Resource", "id", resources.getId());
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
    public void download(List<ResourceDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ResourceDto item : all) {
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
