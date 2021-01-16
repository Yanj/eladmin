package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.yy.domain.*;
import me.zhengjie.modules.yy.repository.*;
import me.zhengjie.modules.yy.service.ReserveResourceService;
import me.zhengjie.modules.yy.service.dto.ReserveCountCriteria;
import me.zhengjie.modules.yy.service.dto.ReserveResourceCriteria;
import me.zhengjie.modules.yy.service.dto.ReserveResourceDto;
import me.zhengjie.modules.yy.service.mapstruct.ReserveResourceMapper;
import me.zhengjie.modules.yy.util.TimeUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yanjun
 * @date 2020-12-24 14:34
 */
@Service
@RequiredArgsConstructor
public class ReserveResourceServiceImpl implements ReserveResourceService {

    private final ReserveResourceRepository repository;
    private final ReserveResourceMapper mapper;

    private final WorkTimeRepository workTimeRepository;
    private final ResourceGroupRepository resourceGroupRepository;
    private final ReserveResourceGroupCountRepository reserveResourceGroupCountRepository;
    private final ResourceGroupCountRepository resourceGroupCountRepository;

    @Override
    public Map<String, Object> queryAll(ReserveResourceCriteria criteria, Pageable pageable) {
        Page<ReserveResource> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<ReserveResourceDto> queryAll(ReserveResourceCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public ReserveResourceDto findById(Long id) {
        ReserveResource instance = repository.findById(id).orElseGet(ReserveResource::new);
        ValidationUtil.isNull(instance.getId(), "ReserveResource", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReserveResourceDto create(ReserveResource resources) {
        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(ReserveResource resources) {
        ReserveResource instance = repository.findById(resources.getId()).orElseGet(ReserveResource::new);
        ValidationUtil.isNull(instance.getId(), "ReserveResource", "id", resources.getId());
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
    public void download(List<ReserveResourceDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReserveResourceDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", item.getId());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<Map<String, Object>> queryReserveCount(ReserveCountCriteria criteria) {
        final Long deptId = criteria.getDeptId();
        // 查询工作时间段列表
        List<WorkTime> workTimeList = workTimeRepository.findAllByDeptIdOrderByBeginTime(deptId);
        // 查询资源组列表
        List<ResourceGroup> resourceGroupList = resourceGroupRepository.findAllByDeptId(deptId);
        // 查询日期列表
        List<String> dateList = getReserveDateList();
        // 查询资源统计列表
        List<ReserveResourceGroupCount> reserveResourceGroupCountList = reserveResourceGroupCountRepository.findAllByPkDeptIdAndPkDate(deptId, dateList.get(0), dateList.get(dateList.size() - 1));
        // 查询可用资源最小数量
        List<ResourceGroupCount> resourceGroupCountList = resourceGroupCountRepository.findAllByDeptId(deptId);

        List<String> dateRange = null;
        if (StringUtils.isNotEmpty(criteria.getBeginDate()) && StringUtils.isNotEmpty(criteria.getEndDate())) {
            dateRange = TimeUtil.getDateRange(criteria.getBeginDate(), criteria.getEndDate());
        }

        List<Map<String, Object>> list = new ArrayList<>();
        // 遍历日期
        for (String date : dateList) {
            if (StringUtils.isNotEmpty(criteria.getBeginDate()) && date.compareTo(criteria.getBeginDate()) < 0) {
                continue;
            }
            if (StringUtils.isNotEmpty(criteria.getEndDate()) && date.compareTo(criteria.getEndDate()) > 0) {
                continue;
            }

            // 遍历工作时段
            for (WorkTime workTime : workTimeList) {
                if (StringUtils.isNotEmpty(criteria.getBeginTime()) && workTime.getBeginTime().compareTo(criteria.getBeginTime()) < 0) {
                    continue;
                }
                if (StringUtils.isNotEmpty(criteria.getEndTime()) && workTime.getEndTime().compareTo(criteria.getEndTime()) > 0) {
                    continue;
                }

                Map<String, Object> item = new HashMap<>();
                item.put("date", date);
                item.put("workTime", workTime);

                Map<String, Object> usedMap = new HashMap<>();

                Map<String, Object> countMap = new HashMap<>();

                Map<String, Object> leftMap = new HashMap<>();

                // 遍历资源组
                for (ResourceGroupCount resourceGroupCount : resourceGroupCountList) {
                    if (null == resourceGroupCount) {
                        continue;
                    }

                    // 遍历已预约数量
                    ReserveResourceGroupCount useCount = null;
                    for (ReserveResourceGroupCount reserveResourceGroupCount : reserveResourceGroupCountList) {
                        if (null == reserveResourceGroupCount) {
                            continue;
                        }
                        if (!Objects.equals(deptId, reserveResourceGroupCount.getPk().getDeptId())) {
                            continue;
                        }
                        if (!StringUtils.equals(date, reserveResourceGroupCount.getPk().getDate())) {
                            continue;
                        }
                        if (!Objects.equals(workTime.getId(), reserveResourceGroupCount.getPk().getWorkTimeId())) {
                            continue;
                        }
                        if (!Objects.equals(resourceGroupCount.getId(), reserveResourceGroupCount.getPk().getResourceGroupId())) {
                            continue;
                        }
                        useCount = reserveResourceGroupCount;
                        break;
                    }
                    int usedCount = 0;
                    if (null != useCount && null != useCount.getCount()) {
                        usedCount = useCount.getCount();
                    }
                    usedMap.put(resourceGroupCount.getId().toString(), usedCount);

                    int count = 0;
                    if (null != resourceGroupCount.getCount()) {
                        count = resourceGroupCount.getCount();
                    }
                    countMap.put(resourceGroupCount.getId().toString(), count);

                    leftMap.put(resourceGroupCount.getId().toString(), count - usedCount);
                }

                item.put("usedMap", usedMap);
                item.put("countMap", countMap);
                item.put("leftMap", leftMap);

                list.add(item);
            }
        }

        return list;
    }

    private List<String> getReserveDateList() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        List<String> dateList = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            dateList.add(dateFormat.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dateList;
    }

}
