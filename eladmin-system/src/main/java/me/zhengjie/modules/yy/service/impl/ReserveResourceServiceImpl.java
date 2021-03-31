package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.yy.domain.ReserveResource;
import me.zhengjie.modules.yy.domain.ReserveResourceGroupCount;
import me.zhengjie.modules.yy.domain.ResourceGroupCount;
import me.zhengjie.modules.yy.domain.WorkTime;
import me.zhengjie.modules.yy.repository.ReserveResourceRepository;
import me.zhengjie.modules.yy.repository.ResourceGroupRepository;
import me.zhengjie.modules.yy.repository.WorkTimeRepository;
import me.zhengjie.modules.yy.service.ReserveResourceService;
import me.zhengjie.modules.yy.service.dto.ReserveCountCriteria;
import me.zhengjie.modules.yy.service.dto.ReserveResourceCriteria;
import me.zhengjie.modules.yy.service.dto.ReserveResourceDto;
import me.zhengjie.modules.yy.service.dto.WorkTimeCriteria;
import me.zhengjie.modules.yy.service.mapstruct.ReserveResourceMapper;
import me.zhengjie.modules.yy.util.TimeUtil;
import me.zhengjie.utils.*;
import me.zhengjie.utils.enums.YesNoEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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

    @Override
    public Map<String, Object> queryAll(ReserveResourceCriteria criteria, Pageable pageable) {
        criteria.setStatus(YesNoEnum.YES);
        Page<ReserveResource> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<ReserveResourceDto> queryAll(ReserveResourceCriteria criteria) {
        criteria.setStatus(YesNoEnum.YES);
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
        resources.setStatus(YesNoEnum.YES);
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
            ReserveResource instance = repository.findById(id).orElseGet(ReserveResource::new);
            ValidationUtil.isNull(instance.getId(), "ReserveResource", "id", id);
            repository.save(instance);
        }
    }

    @Override
    public void download(List<ReserveResourceDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReserveResourceDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("ID", item.getId());
            map.put("预约ID", item.getReserve().getId());
            map.put("日期", item.getDate());
            map.put("工作时段ID", item.getWorkTime().getId());
            map.put("资源分类ID", item.getResourceCategory().getId());
            map.put("资源分组ID", item.getResourceGroup().getId());
            map.put("资源ID", item.getResource().getId());
            map.put("状态", item.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public List<Map<String, Object>> queryReserveCount(ReserveCountCriteria criteria) throws Exception {
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        if (!user.isAdmin()) {
            criteria.setUser(user);
        }
        if (null == criteria.getComId()) {
            throw new BadRequestException("comId 不能为空");
        }

        // 查询工作时间段列表
        WorkTimeCriteria workTimeCriteria = new WorkTimeCriteria();
        workTimeCriteria.setOrgId(user.getOrgId());
        if (user.isAdmin()) {
            workTimeCriteria.setComId(criteria.getComId());
        } else {
            workTimeCriteria.setComId(user.getComId());
        }
        workTimeCriteria.setStatus(YesNoEnum.YES);
        List<WorkTime> workTimeList = workTimeRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,
                workTimeCriteria, criteriaBuilder));

        if (StringUtils.isEmpty(criteria.getBeginDate())) {
            criteria.setBeginDate(TimeUtil.getCurrentDate());
        }
        if (StringUtils.isEmpty(criteria.getEndDate())) {
            criteria.setEndDate(TimeUtil.getCurrentDate(7));
        }

        // 查询日期列表
        List<String> dateList = getReserveDateList(criteria.getBeginDate(), criteria.getEndDate());
        // 查询资源统计列表
        List<ReserveResourceGroupCount> reserveResourceGroupCountList = repository.findResourceGroupCountByComIdAndDate(criteria.getComId(),
                dateList.get(0),
                dateList.get(dateList.size() - 1));

        // 查询可用资源最小数量
        List<ResourceGroupCount> resourceGroupCountList = resourceGroupRepository.findCountByComId(criteria.getComId());

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
                        if (!StringUtils.equals(date, reserveResourceGroupCount.getDate())) {
                            continue;
                        }
                        if (!Objects.equals(workTime.getId(), reserveResourceGroupCount.getWorkTimeId())) {
                            continue;
                        }
                        if (!Objects.equals(resourceGroupCount.getResourceGroupId(), reserveResourceGroupCount.getResourceGroupId())) {
                            continue;
                        }
                        useCount = reserveResourceGroupCount;
                        break;
                    }
                    long usedCount = 0;
                    if (null != useCount) {
                        usedCount = useCount.getCount();
                    }
                    usedMap.put(resourceGroupCount.getResourceGroupId().toString(), usedCount);

                    long count = resourceGroupCount.getMin();
                    countMap.put(resourceGroupCount.getResourceGroupId().toString(), count);

                    leftMap.put(resourceGroupCount.getResourceGroupId().toString(), count - usedCount);
                }

                item.put("usedMap", usedMap);
                item.put("countMap", countMap);
                item.put("leftMap", leftMap);

                list.add(item);
            }
        }

        return list;
    }

    private List<String> getReserveDateList(String beginDate, String endDate) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(beginDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        List<String> dateList = new ArrayList<>();
        int i = 0;
        do {
            dateList.add(date.format(formatter));
            date = date.plus(1, ChronoUnit.DAYS);
            i++;
        } while(date.compareTo(end) < 0 && i < 14);
        return dateList;
    }

}
