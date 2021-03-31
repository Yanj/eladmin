package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.modules.yy.domain.WorkTime;
import me.zhengjie.modules.yy.repository.WorkTimeRepository;
import me.zhengjie.modules.yy.service.WorkTimeService;
import me.zhengjie.modules.yy.service.dto.WorkTimeCriteria;
import me.zhengjie.modules.yy.service.dto.WorkTimeDto;
import me.zhengjie.modules.yy.service.mapstruct.WorkTimeMapper;
import me.zhengjie.utils.*;
import me.zhengjie.utils.enums.YesNoEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
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
public class WorkTimeServiceImpl implements WorkTimeService {

    private final WorkTimeRepository repository;
    private final WorkTimeMapper mapper;

    private final DeptService deptService;

    @Override
    public Map<String, Object> queryAll(WorkTimeCriteria criteria, Pageable pageable) {
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        criteria.setUser(user);
        // 非管理员, 只能看到"未删除"的数据
        if (!user.isAdmin()) {
            criteria.setStatus(YesNoEnum.YES);
        }
        Page<WorkTime> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<WorkTimeDto> queryAll(WorkTimeCriteria criteria) {
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        criteria.setUser(user);
        // 非管理员, 只能看到"未删除"的数据
        if (!user.isAdmin()) {
            criteria.setStatus(YesNoEnum.YES);
        }
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public WorkTimeDto findById(Long id) {
        WorkTime instance = repository.findById(id).orElseGet(WorkTime::new);
        ValidationUtil.isNull(instance.getId(), "WorkTime", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public WorkTimeDto create(WorkTime resources) {
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        // 如果不是管理员, 直接设置当前用户的部门
        if (!user.isAdmin()) {
            resources.setOrgId(user.getOrgId());
            resources.setComId(user.getComId());
//            resources.setDeptId(user.getDeptId());
        }

        // 检查组织 ID
        if (null == resources.getOrgId()) {
            throw new BadRequestException("orgId 不能为空");
        }
        deptService.findById(resources.getOrgId());

        // 检查医院 ID
        if (null == resources.getComId()) {
            throw new BadRequestException("comId 不能为空");
        }
        deptService.findById(resources.getComId());

        // 如果传入了部门 ID, 检查部门 ID
        if (null != resources.getDeptId()) {
            deptService.findById(resources.getDeptId());
        }

        // 计算时长
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime begin = LocalTime.parse(resources.getBeginTime(), formatter);
        LocalTime end = LocalTime.parse(resources.getEndTime(), formatter);
        long duration = end.get(ChronoField.MINUTE_OF_DAY) - begin.get(ChronoField.MINUTE_OF_DAY);
        if (duration <= 0) {
            throw new BadRequestException("开始时间结束时间错误, 时长为" + duration);
        }
        resources.setDuration(duration);

        // 设置状态
        resources.setStatus(YesNoEnum.YES);

        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(WorkTime resources) {
        WorkTime instance = repository.findById(resources.getId()).orElseGet(WorkTime::new);
        ValidationUtil.isNull(instance.getId(), "WorkTime", "id", resources.getId());
        instance.copy(resources);
        repository.save(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            WorkTime instance = repository.findById(id).orElseGet(WorkTime::new);
            ValidationUtil.isNull(instance.getId(), "WorkTime", "id", id);
            instance.setStatus(YesNoEnum.NO);
            repository.save(instance);
        }
    }

    @Override
    public void download(List<WorkTimeDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (WorkTimeDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("ID", item.getId());
            map.put("组织ID", item.getOrgId());
            map.put("公司ID", item.getComId());
            map.put("部门ID", item.getDeptId());
            map.put("开始时间", item.getBeginTime());
            map.put("结束时间", item.getEndTime());
            map.put("时长", item.getDuration());
            map.put("状态", item.getStatus());
            map.put("备注", item.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
