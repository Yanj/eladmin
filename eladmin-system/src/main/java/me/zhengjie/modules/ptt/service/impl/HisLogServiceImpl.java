package me.zhengjie.modules.ptt.service.impl;

import lombok.AllArgsConstructor;
import me.zhengjie.modules.ptt.domain.HisLog;
import me.zhengjie.modules.ptt.repository.HisLogRepository;
import me.zhengjie.modules.ptt.service.HisLogService;
import me.zhengjie.modules.ptt.service.dto.HisLogCriteria;
import me.zhengjie.modules.ptt.service.dto.HisLogDto;
import me.zhengjie.modules.ptt.service.mapstruct.HisLogMapper;
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
import java.util.*;

/**
 * HIS查询日志服务
 *
 * @author yanjun
 * @date 2020-11-28 11:17
 */
@Service
@AllArgsConstructor
public class HisLogServiceImpl implements HisLogService {

    private final HisLogRepository repository;
    private final HisLogMapper mapper;

    @Override
    public List<HisLogDto> createOrUpdate(List<HisCkItemDto> ckItemList) {
        if (null == ckItemList || ckItemList.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Map<Long, HisLog> logMap = new HashMap<>();
        for (HisCkItemDto ckItem : ckItemList) {
            // 忽略空数据
            if (null == ckItem || null == ckItem.getPatItemId()) continue;
            // 忽略已添加过的数据
            if (logMap.containsKey(ckItem.getPatItemId())) continue;

            HisLog logInfo = repository.findById(ckItem.getPatItemId()).orElseGet(HisLog::new);
            if (logInfo.getPatItemId() == null) {
                logInfo = HisLog.create(ckItem);
                logInfo = repository.save(logInfo);
            }
            logMap.put(logInfo.getPatItemId(), logInfo);
        }

        return mapper.toDto(new ArrayList<>(logMap.values()));
    }

    @Override
    public Map<String, Object> queryAll(HisLogCriteria criteria, Pageable pageable) {
        Page<HisLog> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder),
                pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<HisLogDto> queryAll(HisLogCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public HisLogDto findById(Long id) {
        HisLog instance = repository.findById(id).orElseGet(HisLog::new);
        ValidationUtil.isNull(instance.getPatItemId(), "HisLog", "patItemId", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public HisLogDto create(HisLog resources) {
        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(HisLog resources) {
        HisLog instance = repository.findById(resources.getPatItemId()).orElseGet(HisLog::new);
        ValidationUtil.isNull(instance.getPatItemId(), "HisLog", "patItemId", resources.getPatItemId());
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
    public void download(List<HisLogDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (HisLogDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
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
