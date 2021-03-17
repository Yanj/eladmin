package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.vo.SmsVo;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.modules.system.service.dto.DeptDto;
import me.zhengjie.modules.system.service.dto.RoleSmallDto;
import me.zhengjie.modules.system.service.mapstruct.DeptMapper;
import me.zhengjie.modules.yy.domain.*;
import me.zhengjie.modules.yy.repository.*;
import me.zhengjie.modules.yy.service.ReserveService;
import me.zhengjie.modules.yy.service.dto.*;
import me.zhengjie.modules.yy.service.mapstruct.ReserveMapper;
import me.zhengjie.modules.yy.service.mapstruct.ResourceSmallMapper;
import me.zhengjie.modules.yy.service.mapstruct.TermSmallMapper;
import me.zhengjie.modules.yy.service.mapstruct.WorkTimeSmallMapper;
import me.zhengjie.modules.yy.util.TimeUtil;
import me.zhengjie.service.SmsChannelService;
import me.zhengjie.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author yanjun
 * @date 2020-12-24 14:34
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReserveServiceImpl implements ReserveService {

    private final ReserveRepository repository;
    private final ReserveMapper mapper;

    private final PatientRepository patientRepository;
    private final PatientTermRepository patientTermRepository;
    private final PatientTermLogRepository patientTermLogRepository;
    private final TermRepository termRepository;
    private final ResourceGroupRepository resourceGroupRepository;
    private final ResourceCategoryRepository resourceCategoryRepository;
    private final ResourceRepository resourceRepository;
    private final ReserveResourceRepository reserveResourceRepository;
    private final ReserveLogRepository reserveLogRepository;
    private final SmsRepository smsRepository;
    private final WorkTimeRepository workTimeRepository;

    private final ResourceSmallMapper resourceSmallMapper;
    private final WorkTimeSmallMapper workTimeSmallMapper;
    private final TermSmallMapper termSmallMapper;

    private final SmsChannelService smsChannelService;

    private final DeptService deptService;
    private final DeptMapper deptMapper;

    @Override
    public List<Map<String, Object>> queryTermCount(Long deptId, String _date, int days) {
        if (null == deptId) {
            throw new RuntimeException("部门ID不能为空");
        }
        if (days < 0) {
            throw new RuntimeException("days必须大于等于0");
        }
        String beginDate = TimeUtil.getDate(_date, 0);
        String endDate = TimeUtil.getDate(_date, days);

        // 查询所有套餐
        List<Term> termList = termRepository.findAllByDeptId(deptId);
        // 查询套餐预约统计
        List<Map<String, Object>> termCountList = repository.queryTermCount(deptId, beginDate, endDate);
        // 日期范围
        List<String> dateList = TimeUtil.getDateRange(beginDate, endDate);

        List<Map<String, Object>> resList = new ArrayList<>();
        for (Term term : termList) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", term.getName());
            List<Long> data = new ArrayList<>();
            for (String date : dateList) {
                long count = 0;
                for (Map<String, Object> termCount : termCountList) {
                    if (Objects.equals(termCount.get("term_id").toString(), term.getId().toString()) && StringUtils.equals(termCount.get("date").toString(),
                            date)) {
                        count = Long.parseLong(termCount.get("count").toString());
                        break;
                    }
                }
                data.add(count);
            }
            map.put("data", data);
            resList.add(map);
        }
        return resList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void automaticCancel() {
        String date = TimeUtil.getCurrentDate();
        List<Reserve> list = repository.findByStatusAndDateLessThan("init", date);
        if (null != list && !list.isEmpty()) {
            for (Reserve reserve : list) {
                try {
                    cancel(reserve, true);
                } catch (Exception e) {
                    log.error("自动取消任务失败:", e);
                }
            }
        }
    }

    @Override
    public Map<String, Object> queryAll(ReserveCriteria criteria, Pageable pageable) {
//        if (criteria.getDeptId() != null) {
//            JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
//            DeptDto dept;
//            // 只有管理员才允许切换不同的部门进行查询, 其余用户则只允许查询用户所属部门. 换句话说就是用户不能属于总部
//            // 如果当前用户是管理员
//            if (user.getUser().getIsAdmin()) {
//                // 查询要查询的部门级别
//                dept = deptService.findById(criteria.getDeptId());
//            } else {
//                // 查询当前用户的部门级别
//                dept = deptService.findById(user.getUser().getDept().getId());
//            }
//            // 如果部门级别大于等于2, 也就是子部门
//            if (dept.getLevel() >= 2) {
//                // 仅查询当前部门
//                criteria.setDeptId(dept.getId());
//            }
//            // 否则查询医院和子部门数据
//            else {
//                List<Dept> queryList = new ArrayList<>(1);
//                queryList.add(deptMapper.toEntity(dept));
//                List<Long> deptIds = deptService.getDeptChildren(queryList);
//                criteria.setDeptId(null);
//                criteria.setDeptIds(new HashSet<>(deptIds));
//            }
//        }

        Page<Reserve> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<ReserveDto> queryAll(ReserveCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public ReserveCountDto queryReserveCount(ReserveCountCriteria criteria) {
        if (null == criteria.getDeptId()) {
            throw new RuntimeException("部门ID不能为空");
        }
        if (StringUtils.isEmpty(criteria.getDate())) {
            throw new RuntimeException("日期不能为空");
        }
        // 查询时间
        List<WorkTime> workTimeList = workTimeRepository.findAllByDeptIdOrderByBeginTime(criteria.getDeptId());

        // 查询套餐
        List<Term> termList = termRepository.findAllByDeptId(criteria.getDeptId());

        // 统计
        List<Map<String, Object>> countList;
        if (!StringUtils.isEmpty(criteria.getDate()) && null != criteria.getTermId() && null != criteria.getResourceGroupId()) {
            countList = repository.queryReserveCountByDateAndTermIdAndResourceGroupId(criteria.getDeptId(), criteria.getDate(), criteria.getTermId(), criteria.getResourceGroupId());
        } else if (!StringUtils.isEmpty(criteria.getDate()) && null != criteria.getTermId()) {
            countList = repository.queryReserveCountByDateAndTermId(criteria.getDeptId(), criteria.getDate(), criteria.getTermId());
        } else if (!StringUtils.isEmpty(criteria.getDate())) {
            countList = repository.queryReserveCountByDate(criteria.getDeptId(), criteria.getDate());
        } else {
            countList = repository.queryReserveCount(criteria.getDeptId());
        }

        Set<String> dateSet = new HashSet<>();
        for (Map<String, Object> countItem : countList) {
            String date = (String) countItem.get("date");
            if (StringUtils.isEmpty(date)) {
                continue;
            }
            dateSet.add(date);
        }

        ReserveCountDto res = new ReserveCountDto();
        res.setDates(new ArrayList<>(dateSet));
        if (res.getDates().isEmpty()) {
            res.getDates().add(criteria.getDate());
        }
        res.setWorkTimes(workTimeSmallMapper.toDto(workTimeList));
        res.setTerms(termSmallMapper.toDto(termList));
        res.setItems(new ArrayList<>());

        for (String date : res.getDates()) {
            for (WorkTimeSmallDto workTime : res.getWorkTimes()) {
                ReserveCountItemDto resItem = new ReserveCountItemDto();
                resItem.setDate(date);
                resItem.setWorkTime(workTime);
                Map<String, Long> termCountMap = new HashMap<>();
                for (TermSmallDto term : res.getTerms()) {
                    // 查找统计结果
                    Long count = null;
                    for (Map<String, Object> countItem : countList) {
                        String countItemDate = (String)countItem.get("date");
                        BigInteger countItemWorkTimeId = (BigInteger)countItem.get("work_time_id");
                        BigInteger countItemTermId = (BigInteger)countItem.get("term_id");
                        BigInteger countItemCount = (BigInteger)countItem.get("count");
                        if (Objects.equals(date, countItemDate) &&
                                Objects.equals(workTime.getId().toString(), countItemWorkTimeId.toString()) &&
                                Objects.equals(term.getId().toString(), countItemTermId.toString())) {
                            count = countItemCount.longValue();
                            break;
                        }
                    }
                    if (null == count) {
                        count = 0L;
                    }
                    termCountMap.put(term.getId().toString(), count);
                }
                resItem.setTerms(termCountMap);
                res.getItems().add(resItem);
            }
        }

        return res;
    }

    @Override
    public ResourceGroupForReserveDto queryResources(Long id) {
        if (null == id) {
            throw new IllegalArgumentException("id 不能为空");
        }
        Reserve reserve = repository.findById(id).orElseGet(Reserve::new);
        if (null == reserve.getId()) {
            throw new RuntimeException("预约不存在");
        }
        ResourceGroup resourceGroup = reserve.getResourceGroup();

        final Long deptId = resourceGroup.getDept().getId();
        final Long groupId = resourceGroup.getId();

        ResourceGroupForReserveDto res = new ResourceGroupForReserveDto();
        res.setId(groupId);
        res.setName(resourceGroup.getName());

        // 查询分类列表
        List<ResourceCategory> resourceCategoryList = resourceCategoryRepository.findAllByDeptIdAndGroupId(deptId, groupId);
        // 循环
        List<ResourceCategoryForReserveDto> dtoList = new ArrayList<>(resourceCategoryList.size());
        for (ResourceCategory resourceCategory : resourceCategoryList) {
            // 查询分类对应的资源列表
            List<Resource> resourceList = resourceRepository.findAllByDeptIdAndResourceCategoryId(deptId, resourceCategory.getId());

            ResourceCategoryForReserveDto categoryDto = new ResourceCategoryForReserveDto();
            categoryDto.setId(resourceCategory.getId());
            categoryDto.setName(resourceCategory.getName());
            categoryDto.setResources(resourceSmallMapper.toDto(resourceList));
            dtoList.add(categoryDto);
        }
        res.setResourceCategories(dtoList);

        return res;
    }

    @Transactional
    @Override
    public ReserveDto findById(Long id) {
        Reserve instance = repository.findById(id).orElseGet(Reserve::new);
        ValidationUtil.isNull(instance.getId(), "Reserve", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReserveDto create(Reserve resources) throws Exception {
        return create(resources, true);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<ReserveDto> create(Reserve[] resources) throws Exception {
        List<ReserveDto> res = new ArrayList<>();
        for (Reserve reserve : resources) {
            res.add(create(reserve, false));
        }

        if (res.size() == 0) {
            return res;
        }

        // 寻找时间段
        String minDate = null;
        String maxDate = null;
        String minBeginTime = null;
        String maxEndTime = null;
        for (ReserveDto reserve : res) {
            if (null == minDate || minDate.compareTo(reserve.getDate()) > 0) {
                minDate = reserve.getDate();
            }
            if (null == maxDate || maxDate.compareTo(reserve.getDate()) < 0) {
                maxDate = reserve.getDate();
            }
        }
        for (ReserveDto reserve : res) {
            if (minDate.equals(reserve.getDate()) && (null == minBeginTime || minBeginTime.compareTo(reserve.getBeginTime()) > 0)) {
                minBeginTime = reserve.getBeginTime();
            }
            if (maxDate.equals(reserve.getDate()) && (null == maxEndTime || maxEndTime.compareTo(reserve.getEndTime()) < 0)) {
                maxEndTime = reserve.getEndTime();
            }
        }

        ReserveDto reserve = res.get(0);
        PatientDto patient = reserve.getPatient();

        // 发送短信
        Sms sms = new Sms();
        sms.setBusType("reserves");
        sms.setBusId(reserve.getId());
        sms.setMobile(patient.getPhone());
        String content = String.format(
                "尊敬的%s女士您好！您已预约了%s %s-%s %s，如若因故未能按时到院就诊，请提前电话取消预约028-65311659， 感谢您的配合！ 地址：成都市青羊区包家巷77号.退订回T",
                patient.getName(), minDate, minBeginTime, maxDate, maxEndTime);
        sms.setContent(content);

        // 发送短信
        SmsVo smsVo = new SmsVo(sms.getMobile(), sms.getContent());
        String sendSmsResult = smsChannelService.sendSms(smsVo);
        sms.setStatus(sendSmsResult);
        sms.setSendTime(Timestamp.valueOf(LocalDateTime.now()));

        smsRepository.save(sms);

        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    public ReserveDto create(Reserve resources, boolean needSms) throws Exception {
        // 判断部门
        if (null == resources.getDept() || null == resources.getDept().getId()) {
            throw new RuntimeException("部门不能为空");
        }
        // 判断日期
        if (StringUtils.isEmpty(resources.getDate())) {
            throw new RuntimeException("日期不能为空");
        }
        // 判断时段
        if (null == resources.getWorkTime() || null == resources.getWorkTime().getId()) {
            throw new RuntimeException("时段不能为空");
        }
        WorkTime workTime = workTimeRepository.findById(resources.getWorkTime().getId()).orElseGet(WorkTime::new);
        if (null == workTime.getId()) {
            throw new RuntimeException("时段不存在");
        }
        if (StringUtils.isEmpty(resources.getBeginTime())) {
            resources.setBeginTime(workTime.getBeginTime());
        }
        if (StringUtils.isEmpty(resources.getEndTime())) {
            resources.setEndTime(workTime.getEndTime());
        }
        // 获取患者套餐
        PatientTerm patientTerm = resources.getPatientTerm();
        if (null == patientTerm || null == patientTerm.getId()) {
            throw new RuntimeException("患者套餐不能为空");
        }
        patientTerm = patientTermRepository.getPatientTermForUpdate(patientTerm.getId());
        if (null == patientTerm) {
            throw new RuntimeException("患者套餐不存在");
        }
        if (patientTerm.getTimes() <= 0) {
            throw new RuntimeException("剩余次数不足");
        }

        // 获取患者信息
        if (null == patientTerm.getPatient() || null == patientTerm.getPatient().getId()) {
            throw new RuntimeException("患者不能为空");
        }
        Patient patient = patientRepository.findById(patientTerm.getPatient().getId()).orElseGet(Patient::new);
        if (null == patient.getId()) {
            throw new RuntimeException("患者不存在");
        }
        resources.setPatient(patient);

        // 获取套餐
        String code = patientTerm.getTermCode();
        Term term = termRepository.findFirstByCode(code).orElseGet(Term::new);
        if (null == term.getId()) {
            throw new RuntimeException("套餐不存在");
        }
        resources.setTerm(term);

        // 获取资源组
        if (null == resources.getResourceGroup() || null == resources.getResourceGroup().getId()) {
            throw new RuntimeException("资源组不能为空");
        }
        ResourceGroup resourceGroup = resourceGroupRepository.findById(resources.getResourceGroup().getId()).orElseGet(ResourceGroup::new);
        if (null == resourceGroup.getId()) {
            throw new RuntimeException("资源组不存在");
        }

        // 判断是否在该时段内已经有预约
        Long reservedCount = repository.countByPatientTerm(resources.getDept().getId(), patient.getId(), patientTerm.getId(), resources.getDate(), resources.getWorkTime().getId());
        if (null != reservedCount && reservedCount > 0) {
            throw new RuntimeException("该时段已预约, 请勿重复预约");
        }

        // 查询资源在时间段内的使用数量
        List<Map<String, Object>> categoryUseCountMap = repository.queryReserveResourceCount(resources.getDept().getId(), resources.getDate(),
                resources.getWorkTime().getId());
        // 查询资源组下的所有分类
        Set<ResourceCategory> resourceCategories = resourceGroup.getResourceCategories();
        if (null != resourceCategories && !resourceCategories.isEmpty()) {
            for (ResourceCategory resourceCategory : resourceCategories) {
                long useCount = 0;
                for (Map<String, Object> categoryUseCount : categoryUseCountMap) {
                    if (Objects.equals(categoryUseCount.get("resource_category_id").toString(), resourceCategory.getId().toString())) {
                        useCount = Long.parseLong(categoryUseCount.get("count").toString());
                        break;
                    }
                }
                long count = null == resourceCategory.getCount() ? 0 : resourceCategory.getCount();
                if (count - useCount <= 0) {
                    throw new RuntimeException("可用资源不足: " + resourceCategory.getName());
                }
            }
        }

        // 扣次数
        final int oldTimes = patientTerm.getTimes();
        patientTerm.setTimes(oldTimes - 1);
        patientTermRepository.save(patientTerm);

        // 新增
        resources.setStatus("init");
        Reserve reserve = repository.save(resources);

        // 新增预约资源
        if (null != resourceCategories && !resourceCategories.isEmpty()) {
            List<ReserveResource> resourceList = new ArrayList<>();
            for (ResourceCategory resourceCategory : resourceCategories) {
                ReserveResource resource = new ReserveResource();
                resource.setReserve(reserve);
                resource.setResourceGroup(resourceGroup);
                resource.setResourceCategory(resourceCategory);
                resource.setResource(null);
                resourceList.add(resource);
            }
            reserveResourceRepository.saveAll(resourceList);
        }

        // 发送短信
        if (needSms && !StringUtils.isEmpty(patient.getPhone())) {
            Sms sms = new Sms();
            sms.setBusType("reserve");
            sms.setBusId(reserve.getId());
            sms.setMobile(patient.getPhone());
            String content = String.format(
                    "尊敬的%s女士您好！您预约了%s %s-%s（%s）%s项目，如若因故未能按时到院就诊，请提前电话取消预约028-65311659， 感谢您的配合！ 地址：成都市青羊区包家巷77号.退订回T",
                    patient.getName(),
                    resources.getDate(), resources.getBeginTime(), resources.getEndTime(), TimeUtil.getWeekDayText(resources.getDate()),
                    patientTerm.getTermName());
            sms.setContent(content);

            // 发送短信
            SmsVo smsVo = new SmsVo(sms.getMobile(), sms.getContent());
            String res = smsChannelService.sendSms(smsVo);
            sms.setStatus(res);
            sms.setSendTime(Timestamp.valueOf(LocalDateTime.now()));

            smsRepository.save(sms);
        }

        // 新增次数日志
        PatientTermLog patientTermLog = new PatientTermLog();
        patientTermLog.setType("times");
        patientTermLog.setBefore(String.valueOf(oldTimes));
        patientTermLog.setAfter(String.valueOf(patientTerm.getTimes()));
        patientTermLog.setPatientTerm(patientTerm);
        patientTermLog.setContent("预约新增");
        patientTermLogRepository.save(patientTermLog);

        return mapper.toDto(reserve);
    }

    @Transactional
    @Override
    public ReserveDto verify(Reserve resources) throws Exception {
        if (null == resources.getResourceGroup()) {
            throw new RuntimeException("资源组不能为空");
        }
        if (null == resources.getReserveResources()) {
            throw new RuntimeException("资源不能为空");
        }
        for (ReserveResource resource : resources.getReserveResources()) {
            if (null == resource || null == resource.getId() || null == resource.getResource() || null == resource.getResource().getId()) {
                String name = null;
                if (null != resource && null != resource.getResourceCategory()) {
                    name = resource.getResourceCategory().getName();
                }
                throw new RuntimeException("资源不能为空: " + name);
            }
        }

        // 判断预约的状态
        Reserve reserve = repository.getReserveForUpdate(resources.getId());
        if (null == reserve) {
            throw new RuntimeException("预约不存在");
        }
        if (!"check_in".equalsIgnoreCase(reserve.getStatus())) {
            throw new RuntimeException("预约状态错误: " + reserve.getStatus());
        }

        // 获取患者套餐
        PatientTerm patientTerm = reserve.getPatientTerm();
        if (null == patientTerm || null == patientTerm.getId()) {
            throw new RuntimeException("患者套餐不能为空");
        }
        patientTerm = patientTermRepository.findById(patientTerm.getId()).orElseGet(PatientTerm::new);
        if (null == patientTerm.getId()) {
            throw new RuntimeException("患者套餐不存在");
        }

        // 获取患者信息
        if (null == patientTerm.getPatient() || null == patientTerm.getPatient().getId()) {
            throw new RuntimeException("患者不能为空");
        }
        Patient patient = patientRepository.findById(patientTerm.getPatient().getId()).orElseGet(Patient::new);
        if (null == patient.getId()) {
            throw new RuntimeException("患者不存在");
        }

        // 获取资源组
        ResourceGroup resourceGroup = resourceGroupRepository.findById(resources.getResourceGroup().getId()).orElseGet(ResourceGroup::new);
        if (null == resourceGroup.getId()) {
            throw new RuntimeException("资源组不存在");
        }

        // 更新预约资源
        for (ReserveResource resource : resources.getReserveResources()) {
            ReserveResource reserveResource = reserveResourceRepository.findById(resource.getId()).orElseGet(ReserveResource::new);
            if (null == reserveResource.getId()) {
                throw new RuntimeException("预约资源不存在: " + resource.getId());
            }
            reserveResource.copy(resource);
            reserveResourceRepository.save(reserveResource);
        }

        // 更新预约状态
        final String oldStatus = reserve.getStatus();
        reserve.setStatus("verified");
        // 更新操作员
        reserve.setOperator(resources.getOperator());
        repository.save(reserve);

        // 发送短信
        if (!StringUtils.isEmpty(patient.getPhone())) {
            Sms sms = new Sms();
            sms.setBusType("reserve");
            sms.setBusId(reserve.getId());
            sms.setMobile(patient.getPhone());
            String content = String.format(
                    "尊敬的%s女士您好！您预约了%s %s-%s（%s）%s项目(已使用核销)，如有问题请电话咨询028-65311659， 感谢您的配合！ 地址：成都市青羊区包家巷77号.退订回T",
                    patient.getName(),
                    reserve.getDate(), reserve.getBeginTime(), reserve.getEndTime(), TimeUtil.getWeekDayText(reserve.getDate()),
                    patientTerm.getTermName());
            sms.setContent(content);

            // 发送短信
            SmsVo smsVo = new SmsVo(sms.getMobile(), sms.getContent());
            String res = smsChannelService.sendSms(smsVo);
            sms.setStatus(res);
            sms.setSendTime(Timestamp.valueOf(LocalDateTime.now()));

            smsRepository.save(sms);
        }

        // 新增日志
        ReserveLog reserveLog = new ReserveLog();
        reserveLog.setType("status");
        reserveLog.setBefore(oldStatus);
        reserveLog.setAfter(reserve.getStatus());
        reserveLog.setReserve(reserve);
        reserveLog.setContent("预约核销");
        reserveLogRepository.save(reserveLog);

        return mapper.toDto(reserve);
    }

    @Transactional
    @Override
    public ReserveDto cancel(Reserve resources) throws Exception {
        return cancel(resources, false);
    }

    public ReserveDto cancel(Reserve resources, boolean bySystem) throws Exception {
        if (null == resources || null == resources.getId()) {
            throw new RuntimeException("预约不能为空");
        }
        Reserve reserve = repository.getReserveForUpdate(resources.getId());
        if (null == reserve) {
            throw new RuntimeException("预约不存在");
        }
        if (null == reserve.getPatientTerm() || null == reserve.getPatientTerm().getId()) {
            throw new RuntimeException("患者套餐不能为空");
        }
        // 预约已取消, 直接返回
        if ("canceled".equalsIgnoreCase(reserve.getStatus())) {
            return mapper.toDto(reserve);
        }

        // 已核销的作废, 检查权限, 更新相关数据
        if ("verified".equalsIgnoreCase(reserve.getStatus())) {
            boolean hasRole = false;
            JwtUserDto currentUser = (JwtUserDto) SecurityUtils.getCurrentUser();
            for (RoleSmallDto role : currentUser.getUser().getRoles()) {
                if (role.getLevel() < 6) {
                    hasRole = true;
                    break;
                }
            }
            if (!hasRole) {
                throw new RuntimeException("抱歉, 您无权进行此操作, 请联系上级或者管理员进行操作");
            }
        }

        // 获取患者套餐
        PatientTerm patientTerm = patientTermRepository.getPatientTermForUpdate(reserve.getPatientTerm().getId());
        if (null == patientTerm) {
            throw new RuntimeException("患者套餐不存在");
        }

        // 获取患者信息
        if (null == patientTerm.getPatient() || null == patientTerm.getPatient().getId()) {
            throw new RuntimeException("患者不能为空");
        }
        Patient patient = patientRepository.findById(patientTerm.getPatient().getId()).orElseGet(Patient::new);
        if (null == patient.getId()) {
            throw new RuntimeException("患者不存在");
        }

        // 更新次数
        int oldTimes = patientTerm.getTimes();
        patientTerm.setTimes(oldTimes + 1);
        patientTermRepository.save(patientTerm);

        // 更新状态
        final String oldStatus = reserve.getStatus();
        reserve.setStatus("canceled");
        repository.save(reserve);

        // 删除资源
        reserveResourceRepository.deleteByReserveId(reserve.getId());

        // 发送短信
        if (!StringUtils.isEmpty(patient.getPhone()) && !bySystem) {
            Sms sms = new Sms();
            sms.setBusType("reserve");
            sms.setBusId(reserve.getId());
            sms.setMobile(patient.getPhone());
            String content = String.format(
                    "尊敬的%s女士您好！您预约了%s %s-%s（%s）%s项目(已取消)，如有问题请电话咨询028-65311659， 感谢您的配合！ 地址：成都市青羊区包家巷77号.退订回T",
                    patient.getName(),
                    resources.getDate(), resources.getBeginTime(), resources.getEndTime(), TimeUtil.getWeekDayText(resources.getDate()),
                    patientTerm.getTermName());
            sms.setContent(content);

            // 发送短信
            SmsVo smsVo = new SmsVo(sms.getMobile(), sms.getContent());
            String res = smsChannelService.sendSms(smsVo);
            sms.setStatus(res);
            sms.setSendTime(Timestamp.valueOf(LocalDateTime.now()));

            smsRepository.save(sms);
        }

        // 新增日志
        ReserveLog reserveLog = new ReserveLog();
        reserveLog.setType("status");
        reserveLog.setBefore(oldStatus);
        reserveLog.setAfter(reserve.getStatus());
        reserveLog.setReserve(reserve);
        reserveLog.setContent("预约取消");
        reserveLogRepository.save(reserveLog);

        // 新增次数日志
        PatientTermLog patientTermLog = new PatientTermLog();
        patientTermLog.setType("times");
        patientTermLog.setBefore(String.valueOf(oldTimes));
        patientTermLog.setAfter(String.valueOf(patientTerm.getTimes()));
        patientTermLog.setPatientTerm(patientTerm);
        patientTermLog.setContent("预约取消");
        patientTermLogRepository.save(patientTermLog);

        return mapper.toDto(reserve);
    }

    @Transactional
    @Override
    public ReserveDto checkIn(Reserve resources) {
        if (null == resources || null == resources.getId()) {
            throw new RuntimeException("预约不能为空");
        }
        Reserve reserve = repository.getReserveForUpdate(resources.getId());
        if (null == reserve) {
            throw new RuntimeException("预约不存在");
        }
        if (!"init".equalsIgnoreCase(reserve.getStatus())) {
            throw new RuntimeException("只有预约状态才能签到");
        }

        // 获取患者套餐
        PatientTerm patientTerm = patientTermRepository.findById(reserve.getPatientTerm().getId()).orElseGet(PatientTerm::new);
        if (null == patientTerm.getId()) {
            throw new RuntimeException("患者套餐不存在");
        }

        // 获取患者信息
        if (null == patientTerm.getPatient() || null == patientTerm.getPatient().getId()) {
            throw new RuntimeException("患者不能为空");
        }
        Patient patient = patientRepository.findById(patientTerm.getPatient().getId()).orElseGet(Patient::new);
        if (null == patient.getId()) {
            throw new RuntimeException("患者不存在");
        }

        // 更新状态
        final String oldStatus = reserve.getStatus();
        reserve.setStatus("check_in");
        repository.save(reserve);

//        // 发送短信
//        if (!StringUtils.isEmpty(patient.getPhone())) {
//            Sms sms = new Sms();
//            sms.setBusType("reserve");
//            sms.setBusId(reserve.getId());
//            sms.setMobile(patient.getPhone());
//            String content = String.format("您预约的套餐[%s], 预约时间%s %s, 已签到成功, %s",
//                    patientTerm.getTermName(), reserve.getDate(), reserve.getBeginTime(), reserve.getDept().getName());
//            sms.setContent(content);
//            sms.setStatus("init");
//            sms.setSendTime(Timestamp.valueOf(LocalDateTime.now()));
//            smsRepository.save(sms);
//        }

        // 新增日志
        ReserveLog reserveLog = new ReserveLog();
        reserveLog.setType("status");
        reserveLog.setBefore(oldStatus);
        reserveLog.setAfter(reserve.getStatus());
        reserveLog.setReserve(reserve);
        reserveLog.setContent("预约签到");
        reserveLogRepository.save(reserveLog);

        return mapper.toDto(reserve);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Reserve resources) {
        Reserve instance = repository.findById(resources.getId()).orElseGet(Reserve::new);
        ValidationUtil.isNull(instance.getId(), "Reserve", "id", resources.getId());
        instance.copy(resources);
        repository.save(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            repository.deleteById(id);
            reserveResourceRepository.deleteByReserveId(id);
        }
    }

    @Override
    public void download(List<ReserveDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReserveDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", item.getId());
            map.put("状态", item.getStatus());
            map.put("备注", item.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public TodayReserveCountDto queryTodayCount(Long deptId) {
        TodayReserveCountDto res = new TodayReserveCountDto();
        String date = TimeUtil.getCurrentDate();
        List<Map<String, Object>> list = repository.queryTodayCount(deptId, date);
        for (Map<String, Object> item : list) {
            String status = item.get("status").toString();
            int count = Integer.parseInt(item.get("count").toString());
            if ("all".equalsIgnoreCase(status)) {
                res.setTotalCount(count);
            } else if ("init".equalsIgnoreCase(status)) {
                res.setPreprocessCount(count);
            } else if ("check_in".equalsIgnoreCase(status)) {
                res.setProcessingCount(count);
            } else if ("verified".equalsIgnoreCase(status)) {
                res.setProcessedCount(count);
            }
        }
        return res;
    }

    @Override
    public List<WeekReserveCountDto> queryWeekCount(Long deptId) {
        List<WeekReserveCountDto> resList = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            List<String> dateList = TimeUtil.getWeekDateRange(-i);
            String endDate = dateList.get(dateList.size() - 1);
            String beginDate = dateList.get(0);

            WeekReserveCountDto weekReserveCount = new WeekReserveCountDto();
            // all
            List<Map<String, Object>> list = repository.queryRangeCountByStatusNotEquals(deptId, beginDate, endDate, "canceled");
            List<Long> allList = processDateListCount(dateList, list);
            weekReserveCount.setAll(allList);

            // init
            list = repository.queryRangeCountByStatusEquals(deptId, beginDate, endDate, "init");
            List<Long> initList = processDateListCount(dateList, list);
            weekReserveCount.setInit(initList);

            // check in
            list = repository.queryRangeCountByStatusEquals(deptId, beginDate, endDate, "check_in");
            List<Long> checkInList = processDateListCount(dateList, list);
            weekReserveCount.setCheckIn(checkInList);

            // verified
            list = repository.queryRangeCountByStatusEquals(deptId, beginDate, endDate, "verified");
            List<Long> verifiedList = processDateListCount(dateList, list);
            weekReserveCount.setVerified(verifiedList);

            resList.add(weekReserveCount);
        }

        return resList;
    }

    private List<Long> processDateListCount(List<String> dateList, List<Map<String, Object>> list) {
        List<Long> resList = new ArrayList<>();
        for (String date : dateList) {
            long count = 0L;
            for (Map<String, Object> item : list) {
                if (date.equals(item.get("date").toString())) {
                    count = Long.parseLong(item.get("count").toString());
                    break;
                }
            }
            resList.add(count);
        }
        return resList;
    }

    @Override
    public TodayWorkTimeReserveCountDto queryTodayCountGroupByWorkTime(Long deptId) {
        TodayWorkTimeReserveCountDto todayWorkTimeReserveCount = new TodayWorkTimeReserveCountDto();
        List<String> workTimes = new ArrayList<>();

        // 查询所有时段
        List<WorkTime> workTimeList = workTimeRepository.findAllByDeptIdOrderByBeginTime(deptId);

        // 处理时间数据
        for (WorkTime workTime : workTimeList) {
            workTimes.add(workTime.getBeginTime());
        }
        todayWorkTimeReserveCount.setWorkTimes(workTimes);

        // 查询今日预约数量统计
        String todayDate = TimeUtil.getCurrentDate();
        List<Map<String, Object>> todayList = repository.queryCountGroupByWorkTimeId(deptId, todayDate);

        todayWorkTimeReserveCount.setCounts(processCountList(todayList, workTimeList));

        // 查询昨日预约数量统计
        String prevDate = TimeUtil.getCurrentDate(-1);
        List<Map<String, Object>> prevList = repository.queryCountGroupByWorkTimeId(deptId, prevDate);

        todayWorkTimeReserveCount.setPrevCounts(processCountList(prevList, workTimeList));

        return todayWorkTimeReserveCount;
    }

    private List<Long> processCountList(List<Map<String, Object>> list, List<WorkTime> workTimeList) {
        List<Long> counts = new ArrayList<>();
        // 处理数据
        for (WorkTime workTime : workTimeList) {
            long count = 0;
            for (Map<String, Object> item : list) {
                if (workTime.getId().toString().equals(item.get("work_time_id").toString())) {
                    count = Long.parseLong(item.get("count").toString());
                    break;
                }
            }
            counts.add(count);
        }
        return counts;
    }

    @Override
    public List<List<Long>> queryTodayCountGroupByWorkTimeAndResourceGroup(Long deptId) {
        List<List<Long>> resList = new ArrayList<>();

        // 根据工作时段和资源组进行统计
        String date = TimeUtil.getCurrentDate();
        List<Map<String, Object>> list = repository.queryCountGroupByWorkTimeAndResourceGroup(deptId, date);

        // 查询所有时段
        List<WorkTime> workTimeList = workTimeRepository.findAllByDeptIdOrderByBeginTime(deptId);

        // 查询所有资源
        List<ResourceGroup> resourceGroupList = resourceGroupRepository.findAllByDeptId(deptId);

        // 循环工作时间段列表
        for (WorkTime workTime : workTimeList) {
            String workTimeId = workTime.getId().toString();
            List<Long> workTimeCountList = new ArrayList<>();
            // 循环资源组列表
            for (ResourceGroup resourceGroup : resourceGroupList) {
                String resourceGroupId = resourceGroup.getId().toString();
                long count = 0;
                // 循环数据
                for (Map<String, Object> item : list) {
                    if (item.get("work_time_id").toString().equals(workTimeId) && item.get("resource_group_id").toString().equals(resourceGroupId)) {
                        count = Long.parseLong(item.get("count").toString());
                        break;
                    }
                }
                workTimeCountList.add(count);
            }
            resList.add(workTimeCountList);
        }

        return resList;
    }

}
