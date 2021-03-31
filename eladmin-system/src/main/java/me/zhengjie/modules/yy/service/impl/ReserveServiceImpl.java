package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.domain.vo.SmsVo;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.service.DeptService;
import me.zhengjie.modules.system.service.dto.RoleSmallDto;
import me.zhengjie.modules.yy.domain.*;
import me.zhengjie.modules.yy.repository.*;
import me.zhengjie.modules.yy.service.PatientService;
import me.zhengjie.modules.yy.service.PatientTermService;
import me.zhengjie.modules.yy.service.ReserveService;
import me.zhengjie.modules.yy.service.ResourceCategoryService;
import me.zhengjie.modules.yy.service.dto.*;
import me.zhengjie.modules.yy.service.mapstruct.*;
import me.zhengjie.modules.yy.util.TimeUtil;
import me.zhengjie.service.SmsChannelService;
import me.zhengjie.utils.*;
import me.zhengjie.utils.enums.YesNoEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    private final PatientService patientService;
    private final PatientMapper patientMapper;

    private final PatientTermRepository patientTermRepository;
    private final PatientTermService patientTermService;
    private final PatientTermMapper patientTermMapper;

    private final PatientTermLogRepository patientTermLogRepository;
    private final TermRepository termRepository;
    private final ResourceGroupRepository resourceGroupRepository;
    private final ResourceCategoryService resourceCategoryService;
    private final ResourceCategoryRepository resourceCategoryRepository;
    private final ResourceRepository resourceRepository;
    private final ReserveResourceRepository reserveResourceRepository;
    private final ReserveLogRepository reserveLogRepository;
    private final SmsRepository smsRepository;
    private final WorkTimeRepository workTimeRepository;

    private final ResourceSmallMapper resourceSmallMapper;
    private final WorkTimeSmallMapper workTimeSmallMapper;
    private final TermSmallMapper termSmallMapper;

    private final DeptService deptService;
    private final SmsChannelService smsChannelService;

    @Override
    public List<Map<String, Object>> queryTermCount(Long _comId, String _date, int days) {
        if (days < 0) {
            throw new RuntimeException("days必须大于等于0");
        }
        String beginDate = TimeUtil.getDate(_date, 0);
        String endDate = TimeUtil.getDate(_date, days);

        Long orgId;
        Long comId;

        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        orgId = user.getOrgId();
        if (user.isAdmin()) {
            comId = _comId;
        } else {
            comId = user.getComId();
        }

        // 查询所有套餐
        TermCriteria termCriteria = new TermCriteria();
        termCriteria.setOrgId(orgId);
        termCriteria.setComId(comId);
        termCriteria.setStatus(YesNoEnum.YES);
        // ... 查询
        List<Term> termList = termRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, termCriteria,
                criteriaBuilder));

        // 查询套餐预约统计
        List<ReserveTermCount> termCountList;
        if (null != comId) {
            termCountList = repository.queryTermCountByComId(comId, beginDate, endDate);
        } else {
            termCountList = repository.queryTermCountByOrgId(orgId, beginDate, endDate);
        }
        // 日期范围
        List<String> dateList = TimeUtil.getDateRange(beginDate, endDate);

        List<Map<String, Object>> resList = new ArrayList<>();
        for (Term term : termList) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", term.getName());
            List<Long> data = new ArrayList<>();
            for (String date : dateList) {
                long count = 0;
                for (ReserveTermCount termCount : termCountList) {
                    if (term.getId().equals(termCount.getTermId()) && StringUtils.equals(date, termCount.getDate())) {
                        count = termCount.getCount();
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
        List<Reserve> list = repository.findByStatusAndDate(ReserveVerifyStatus.init, date);
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
    public List<UserReserveCount> queryUserReserveCount(UserReserveCountCriteria criteria) {
        Long comId = criteria.getComId();
        String beginDate = criteria.getBeginDate();
        String endDate = criteria.getEndDate();

        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        if (!user.isAdmin()) {
            comId = user.getComId();
        }
        if (null == comId) {
            throw new BadRequestException("comId 不能为空");
        }
        if (StringUtils.isEmpty(beginDate)) {
            beginDate = TimeUtil.getCurrentDate();
        }
        if (StringUtils.isEmpty(endDate)) {
            endDate = TimeUtil.getCurrentDate();
        }
        return repository.queryUserReserveCount(comId, beginDate, endDate);
    }

    @Override
    public Map<String, Object> queryAll(ReserveCriteria criteria, Pageable pageable) {
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        criteria.setUser(user);
        if (!user.isAdmin()) {
            criteria.setStatus(YesNoEnum.YES);
        }
        Page<Reserve> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<ReserveDto> queryAll(ReserveCriteria criteria) {
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        criteria.setUser(user);
        if (!user.isAdmin()) {
            criteria.setStatus(YesNoEnum.YES);
        }
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    public ReserveCountDto queryReserveCount(ReserveCountCriteria criteria) {
        Long orgId;
        Long comId;

        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        orgId = user.getOrgId();
        comId = user.getComId();
        if (user.isAdmin()) {
            orgId = criteria.getOrgId();
            comId = criteria.getComId();
        }
        if (null == orgId) {
            orgId = user.getOrgId();
        }
        if (null == comId) {
            comId = user.getComId();
        }
        if (null == comId) {
            throw new BadRequestException("公司 ID 不能为空");
        }

        if (StringUtils.isEmpty(criteria.getDate())) {
            throw new RuntimeException("日期不能为空");
        }
        // 查询时间
        List<WorkTime> workTimeList = workTimeRepository.findByComId(comId);

        // 查询套餐
        List<Term> termList = termRepository.findAllByComId(orgId, comId);

        // 统计
        List<ReserveCount> countList;
        if (!StringUtils.isEmpty(criteria.getDate()) && null != criteria.getTermId() && null != criteria.getResourceGroupId()) {
            countList = repository.queryReserveCount(comId, criteria.getDate(), criteria.getTermId(), criteria.getResourceGroupId());
        } else if (!StringUtils.isEmpty(criteria.getDate()) && null != criteria.getTermId()) {
            countList = repository.queryReserveCount(comId, criteria.getDate(), criteria.getTermId());
        } else if (!StringUtils.isEmpty(criteria.getDate())) {
            countList = repository.queryReserveCount(comId, criteria.getDate());
        } else {
            countList = repository.queryReserveCount(comId);
        }

        Set<String> dateSet = new HashSet<>();
        for (ReserveCount countItem : countList) {
            String date = countItem.getDate();
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
                    for (ReserveCount countItem : countList) {
                        String countItemDate = countItem.getDate();
                        Long countItemWorkTimeId = countItem.getWorkTimeId();
                        Long countItemTermId = countItem.getTermId();
                        Long countItemCount = countItem.getCount();
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

        final Long groupId = resourceGroup.getId();

        ResourceGroupForReserveDto res = new ResourceGroupForReserveDto();
        res.setId(groupId);
        res.setName(resourceGroup.getName());

        // 查询分类列表
        List<ResourceCategory> resourceCategoryList = resourceCategoryRepository.findAllByGroupId(groupId);
        // 循环
        List<ResourceCategoryForReserveDto> dtoList = new ArrayList<>(resourceCategoryList.size());
        for (ResourceCategory resourceCategory : resourceCategoryList) {
            // 查询分类对应的资源列表
            List<Resource> resourceList = resourceRepository.findAllByResourceCategoryId(resourceCategory.getId());

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
        PatientDto patient = patientService.findById(reserve.getPatient().getId());

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
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        if (null == user) {
            throw new BadRequestException("获取用户失败");
        }
        if (!user.isAdmin()) {
            resources.setOrgId(user.getOrgId());
            resources.setComId(user.getComId());
            resources.setDeptId(user.getDeptId());
        }
        if (resources.getOrgId() == null) {
            throw new BadRequestException("组织Id不能为空");
        }
        deptService.findById(resources.getOrgId());

        if (resources.getComId() == null) {
            throw new BadRequestException("公司Id不能为空");
        }
        deptService.findById(resources.getComId());

        // 如果传入了部门 ID, 检查部门 ID
        if (null != resources.getDeptId()) {
            deptService.findById(resources.getDeptId());
        }

        // 获取患者套餐
        PatientTerm patientTerm = resources.getPatientTerm();
        if (null == patientTerm || null == patientTerm.getId()) {
            // 判断是否传入套餐
            Term term = resources.getTerm();
            if (null == term || null == term.getId()) {
                throw new BadRequestException("套餐不能为空");
            }
            term = termRepository.findById(term.getId()).orElseGet(Term::new);
            if (null == term.getId()) {
                throw new BadRequestException("套餐不存在");
            }
            resources.setTerm(term);

            // 新增外部患者
            Patient patient = resources.getPatient();
            if (null == patient) {
                throw new BadRequestException("患者信息不能为空");
            }
            if (StringUtils.isEmpty(patient.getName())) {
                throw new BadRequestException("患者姓名不能为空");
            }
            if (StringUtils.isEmpty(patient.getPhone())) {
                throw new BadRequestException("患者电话不能为空");
            }
            PatientCriteria patientCriteria = new PatientCriteria();
            patientCriteria.setOrgId(resources.getOrgId());
            patientCriteria.setComId(resources.getComId());
            patientCriteria.setDeptId(resources.getDeptId());
            patientCriteria.setPhone(patient.getPhone());
            List<PatientDto> patientList = patientService.queryAll(patientCriteria);
            if (patientList.isEmpty()) {
                Patient patientInstance = new Patient();
                patientInstance.setOrgId(resources.getOrgId());
                patientInstance.setComId(resources.getComId());
                patientInstance.setDeptId(resources.getDeptId());
                patientInstance.setSource(PatientSourceEnum.MEITUAN);
                patientInstance.setName(patient.getName());
                patientInstance.setPhone(patient.getPhone());
                patientInstance.setStatus(YesNoEnum.YES);
                patient = patientMapper.toEntity(patientService.create(patientInstance));
            } else {
                patient = patientMapper.toEntity(patientList.get(0));
            }
            if (null == patient || null == patient.getId()) {
                throw new BadRequestException("患者信息异常");
            }
            resources.setPatient(patient);

            // 新增免费套餐
            PatientTerm patientTermInstance = new PatientTerm();
            patientTermInstance.setOrgId(resources.getOrgId());
            patientTermInstance.setComId(resources.getComId());
            patientTermInstance.setDeptId(resources.getDeptId());
            patientTermInstance.setType(PatientTermType.free);
            patientTermInstance.setPatient(patient);
            patientTermInstance.setTermId(term.getId());
            patientTermInstance.setTermCode(term.getCode());
            patientTermInstance.setTermName(term.getName());
            patientTermInstance.setTermPrice(term.getPrice());
            patientTermInstance.setTermOriginalPrice(term.getOriginalPrice());
            patientTermInstance.setTermTimes(term.getTimes());
            patientTermInstance.setTermUnit(term.getUnit());
            patientTermInstance.setTermDuration(term.getDuration());
            patientTermInstance.setTermOperatorCount(term.getOperatorCount());
            patientTermInstance.setPrice(0L);
            patientTermInstance.setTotalTimes(1);
            patientTermInstance.setTimes(1);
            patientTermInstance.setDuration(term.getDuration());
            patientTermInstance.setOperatorCount(term.getOperatorCount());
            patientTermInstance.setStatus(YesNoEnum.YES);
            patientTerm = patientTermMapper.toEntity(patientTermService.create(patientTermInstance));
            if (null == patientTerm || null == patientTerm.getId()) {
                throw new BadRequestException("患者套餐不能为空");
            }
            resources.setPatientTerm(patientTerm);
        }
        patientTerm = patientTermRepository.getPatientTermForUpdate(patientTerm.getId());
        if (null == patientTerm) {
            throw new RuntimeException("患者套餐不存在");
        }
        if (patientTerm.getTimes() <= 0) {
            throw new RuntimeException("剩余次数不足");
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
        resources.setWorkTime(workTime);
        if (StringUtils.isEmpty(resources.getBeginTime())) {
            resources.setBeginTime(workTime.getBeginTime());
        }
        if (StringUtils.isEmpty(resources.getEndTime())) {
            if (patientTerm.getDuration() != null) {
                String endTime = TimeUtil.timeAdd(resources.getBeginTime(), patientTerm.getDuration());
                resources.setEndTime(endTime);
            } else if (patientTerm.getTermDuration() != null) {
                String endTime = TimeUtil.timeAdd(resources.getBeginTime(), patientTerm.getTermDuration());
                resources.setEndTime(endTime);
            } else {
                resources.setEndTime(workTime.getEndTime());
            }
        }
        if (StringUtils.isEmpty(resources.getBeginTime()) || StringUtils.isEmpty(resources.getEndTime())) {
            throw new BadRequestException("开始时间和结束时间不能为空");
        }
        List<WorkTime> workTimeList = workTimeRepository.findByComIdAndTime(resources.getComId(), resources.getBeginTime(), resources.getEndTime());
        if (null == workTimeList || workTimeList.isEmpty()) {
            throw new BadRequestException(resources.getBeginTime() + "至" + resources.getEndTime() + "内无可用工作时间段");
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
        Term term = termRepository.findById(patientTerm.getTermId()).orElseGet(Term::new);
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
        Long reservedCount = repository.countByPatientTerm(patient.getId(), patientTerm.getId(), resources.getDate(), resources.getWorkTime().getId());
        if (null != reservedCount && reservedCount > 0) {
            throw new RuntimeException("该时段已预约, 请勿重复预约");
        }

        // 查询资源在时间段内的使用数量
        List<ReserveResourceCategoryCount> categoryUseCountList = repository.queryReserveResourceCount(resources.getComId(), resources.getDate());
        // 查询资源组下的所有分类
        Set<ResourceCategory> resourceCategories = resourceGroup.getResourceCategories();
        if (null != resourceCategories && !resourceCategories.isEmpty()) {
            for (ResourceCategory resourceCategory : resourceCategories) {
                for (ReserveResourceCategoryCount categoryCount : categoryUseCountList) {
                    if (!resourceCategory.getId().equals(categoryCount.getResourceCategoryId())) {
                        continue;
                    }

                    long count = null == resourceCategory.getCount() ? 0 : resourceCategory.getCount();
                    long useCount = null == categoryCount.getCount() ? 0 : categoryCount.getCount();
                    if (useCount >= count) {
                        throw new BadRequestException("可用资源不足:" + resourceCategory.getName());
                    }
                }
            }
        }

        // 扣次数
        final int oldTimes = patientTerm.getTimes();
        patientTerm.setTimes(oldTimes - 1);
        patientTermRepository.save(patientTerm);

        // 新增
        resources.setVerifyStatus(ReserveVerifyStatus.init);
        resources.setStatus(YesNoEnum.YES);
        Reserve reserve = repository.save(resources);

        // 新增预约资源
        if (null != resourceCategories && !resourceCategories.isEmpty()) {
            List<ReserveResource> resourceList = new ArrayList<>();
            for (WorkTime workTimeItem : workTimeList) {
                for (ResourceCategory resourceCategory : resourceCategories) {
                    ReserveResource resource = new ReserveResource();
                    resource.setReserve(reserve);
                    resource.setDate(reserve.getDate());
                    resource.setWorkTime(workTimeItem);
                    resource.setResourceGroup(resourceGroup);
                    resource.setResourceCategory(resourceCategory);
                    resource.setResource(null);
                    resource.setStatus(YesNoEnum.YES);
                    resourceList.add(resource);
                }
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

    @Transactional(rollbackFor = Exception.class)
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
                if (null != resource && null != resource.getResourceCategory() && null != resource.getResourceCategory().getId()) {
                    ResourceCategoryDto resourceCategory = resourceCategoryService.findById(resource.getResourceCategory().getId());
                    name = resourceCategory.getName();
                }
                throw new RuntimeException("资源不能为空: " + name);
            }
        }

        // 判断预约的状态
        Reserve reserve = repository.getReserveForUpdate(resources.getId());
        if (null == reserve) {
            throw new RuntimeException("预约不存在");
        }
        if (ReserveVerifyStatus.check_in != reserve.getVerifyStatus()) {
            throw new RuntimeException("预约状态错误: " + reserve.getVerifyStatus());
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
        final ReserveVerifyStatus oldStatus = reserve.getVerifyStatus();
        reserve.setVerifyStatus(ReserveVerifyStatus.verified);
        // 更新操作员
        reserve.setOperators(resources.getOperators());
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
        reserveLog.setBefore(oldStatus.toString());
        reserveLog.setAfter(reserve.getVerifyStatus().toString());
        reserveLog.setReserve(reserve);
        reserveLog.setContent("预约核销");
        reserveLogRepository.save(reserveLog);

        return mapper.toDto(reserve);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReserveDto cancel(Reserve resources) throws Exception {
        return cancel(resources, false);
    }

    @Transactional(rollbackFor = Exception.class)
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
        if (ReserveVerifyStatus.canceled == reserve.getVerifyStatus()) {
            return mapper.toDto(reserve);
        }

        // 已核销的作废, 检查权限, 更新相关数据
        if (ReserveVerifyStatus.verified == reserve.getVerifyStatus()) {
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
        final ReserveVerifyStatus oldStatus = reserve.getVerifyStatus();
        reserve.setVerifyStatus(ReserveVerifyStatus.canceled);
        repository.save(reserve);

        // 删除资源
        // 2021-03-29: 因为统计已检查过预约的状态, 所以此处删除资源暂时可注释掉
//        reserveResourceRepository.deleteByReserveId(reserve.getId());

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
        reserveLog.setBefore(oldStatus.toString());
        reserveLog.setAfter(reserve.getVerifyStatus().toString());
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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReserveDto checkIn(Reserve resources) {
        if (null == resources || null == resources.getId()) {
            throw new RuntimeException("预约不能为空");
        }
        Reserve reserve = repository.getReserveForUpdate(resources.getId());
        if (null == reserve) {
            throw new RuntimeException("预约不存在");
        }

        if (ReserveVerifyStatus.init != reserve.getVerifyStatus()) {
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
        final ReserveVerifyStatus oldStatus = reserve.getVerifyStatus();
        reserve.setVerifyStatus(ReserveVerifyStatus.check_in);
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
        reserveLog.setBefore(oldStatus.toString());
        reserveLog.setAfter(reserve.getVerifyStatus().toString());
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
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        if (user.isAdmin()) {
            for (Long id : ids) {
                repository.deleteById(id);
                reserveResourceRepository.deleteByReserveId(id);
            }
        } else {
            for (Long id : ids) {
                repository.updateStatus(id, YesNoEnum.NO);
                reserveResourceRepository.updateStatusByReserveId(id, YesNoEnum.NO);
            }
        }
    }

    @Override
    public void download(List<ReserveDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ReserveDto item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("ID", item.getId());
            map.put("组织ID", item.getOrgId());
            map.put("公司ID", item.getComId());
            map.put("部门ID", item.getDeptId());
            map.put("患者", item.getPatient().getName());
            map.put("套餐", item.getTerm().getName());
//            map.put("患者套餐", item.getPatientTerm());
//            map.put("资源分组", item.getResourceGroup());
            map.put("预约日期", item.getDate());
            map.put("工作时段", item.getWorkTime().getId());
            map.put("开始时间", item.getBeginTime());
            map.put("结束时间", item.getEndTime());
//            map.put("操作员", item.getOperators());
            map.put("核销状态", item.getVerifyStatus());
            map.put("状态", item.getStatus());
            map.put("备注", item.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public WeekReserveCountDto queryTodayCount(ReserveCriteria criteria) {
        String beginDate = TimeUtil.getCurrentDate();
        String endDate = TimeUtil.getDate(beginDate, 1);

        return queryDateCount(criteria, beginDate, endDate);
    }

    @Override
    public List<WeekReserveCountDto> queryWeekCount(ReserveCriteria criteria) {
        List<WeekReserveCountDto> res = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            List<String> dateList = TimeUtil.getWeekDateRange(-i);
            String endDate = dateList.get(dateList.size() - 1);
            String beginDate = dateList.get(0);
            WeekReserveCountDto countDto = queryDateCount(criteria, beginDate, endDate);
            res.add(countDto);
        }
        return res;
    }

    private WeekReserveCountDto queryDateCount(ReserveCriteria criteria, String beginDate, String endDate) {
        Long orgId = null;
        Long comId = null;

        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        orgId = user.getOrgId();
        // 管理员可以查看不同的公司统计
        if (user.isAdmin()) {
            comId = criteria.getComId();
        }
        if (null == comId) {
            comId = user.getComId();
        }
        if (null == orgId && null == comId) {
            throw new BadRequestException("组织 ID 或者 公司 ID 不能为空");
        }

        WeekReserveCountDto res = new WeekReserveCountDto();
        res.setDates(TimeUtil.getDateRange(beginDate, endDate));
        if (res.getDates() == null || res.getDates().isEmpty()) {
            throw new BadRequestException("日期范围错误: " + beginDate + "至" + endDate);
        }

        res.setAll(new ArrayList<>(res.getDates().size()));
        res.setInit(new ArrayList<>(res.getDates().size()));
        res.setCheckIn(new ArrayList<>(res.getDates().size()));
        res.setVerified(new ArrayList<>(res.getDates().size()));

        if (null != comId) {
            // 已预约状态统计
            List<Long> initList = queryDateCountByComId(comId, ReserveVerifyStatus.init, beginDate, endDate, res.getDates());
            res.setInit(initList);

            // 已签到状态统计
            List<Long> checkInList = queryDateCountByComId(comId, ReserveVerifyStatus.check_in, beginDate, endDate, res.getDates());
            res.setCheckIn(checkInList);

            // 已核销状态统计
            List<Long> verifiedList = queryDateCountByComId(comId, ReserveVerifyStatus.verified, beginDate, endDate, res.getDates());
            res.setVerified(verifiedList);

            // 总计
            res.setAll(processDateCountTotal(initList, checkInList, verifiedList));
        } else {
            // 已预约状态统计
            List<Long> initList = queryDateCountByOrgId(orgId, ReserveVerifyStatus.init, beginDate, endDate, res.getDates());
            res.setInit(initList);

            // 已签到状态统计
            List<Long> checkInList = queryDateCountByOrgId(orgId, ReserveVerifyStatus.check_in, beginDate, endDate, res.getDates());
            res.setCheckIn(checkInList);

            // 已核销状态统计
            List<Long> verifiedList = queryDateCountByOrgId(orgId, ReserveVerifyStatus.verified, beginDate, endDate, res.getDates());
            res.setVerified(verifiedList);

            // 总计
            res.setAll(processDateCountTotal(initList, checkInList, verifiedList));
        }

        return res;
    }

    private List<Long> queryDateCountByComId(Long comId, ReserveVerifyStatus verifyStatus, String beginDate, String endDate, List<String> dateList) {
        List<ReserveDateCount> list = repository.queryDateCountByComId(comId, verifyStatus, beginDate, endDate);
        return processDateCountToList(list, dateList);
    }

    private List<Long> queryDateCountByOrgId(Long orgId, ReserveVerifyStatus verifyStatus, String beginDate, String endDate, List<String> dateList) {
        List<ReserveDateCount> list = repository.queryDateCountByOrgId(orgId, verifyStatus, beginDate, endDate);
        return processDateCountToList(list, dateList);
    }

    private List<Long> processDateCountToList(List<ReserveDateCount> list, List<String> dateList) {
        List<Long> res = new ArrayList<>(dateList.size());

        // 统计
        for (String date : dateList) {
            ReserveDateCount dateCount = null;
            for (ReserveDateCount listItem : list) {
                if (StringUtils.equals(date, listItem.getDate())) {
                    dateCount = listItem;
                    break;
                }
            }
            if (null == dateCount) {
                res.add(0L);
            } else {
                res.add(dateCount.getCount());
            }
        }

        return res;
    }

    private List<Long> processDateCountTotal(List<Long> initList, List<Long> checkInList, List<Long> verifiedList) {
        List<Long> res = new ArrayList<>();
        for (int i = 0; i < initList.size() && i < checkInList.size() && i < verifiedList.size(); i++) {
            res.add(initList.get(i) + checkInList.get(i) + verifiedList.get(i));
        }
        return res;
    }

    @Override
    public TodayWorkTimeReserveCountDto queryTodayCountGroupByWorkTime(Long comId) {
        TodayWorkTimeReserveCountDto todayWorkTimeReserveCount = new TodayWorkTimeReserveCountDto();
        List<String> workTimes = new ArrayList<>();

        // 查询所有时段
        List<WorkTime> workTimeList = workTimeRepository.findByComId(comId);

        // 处理时间数据
        for (WorkTime workTime : workTimeList) {
            workTimes.add(workTime.getBeginTime());
        }
        todayWorkTimeReserveCount.setWorkTimes(workTimes);

        // 查询今日预约数量统计
        String todayDate = TimeUtil.getCurrentDate();
        List<ReserveWorkTimeCount> todayList = repository.queryCountGroupByWorkTimeId(comId, todayDate);
        List<List<Long>> todayCount = processCountList(todayList, workTimeList);
        todayWorkTimeReserveCount.setCounts(todayCount.get(0));
        todayWorkTimeReserveCount.setInitCounts(todayCount.get(1));
        todayWorkTimeReserveCount.setCheckInCounts(todayCount.get(2));
        todayWorkTimeReserveCount.setVerifiedCounts(todayCount.get(3));

        // 查询昨日预约数量统计
        String prevDate = TimeUtil.getCurrentDate(-1);
        List<ReserveWorkTimeCount> prevList = repository.queryCountGroupByWorkTimeId(comId, prevDate);
        List<List<Long>> prevCount = processCountList(prevList, workTimeList);
        todayWorkTimeReserveCount.setPrevCounts(prevCount.get(0));
        todayWorkTimeReserveCount.setPrevInitCounts(prevCount.get(1));
        todayWorkTimeReserveCount.setPrevCheckInCounts(prevCount.get(2));
        todayWorkTimeReserveCount.setPrevVerifiedCounts(prevCount.get(3));

        return todayWorkTimeReserveCount;
    }

    private List<List<Long>> processCountList(List<ReserveWorkTimeCount> list, List<WorkTime> workTimeList) {
        List<List<Long>> res = new ArrayList<>();
        List<Long> countList = new ArrayList<>();
        List<Long> initList = new ArrayList<>();
        List<Long> checkInList = new ArrayList<>();
        List<Long> verifiedList = new ArrayList<>();
        // 处理数据
        for (WorkTime workTime : workTimeList) {
            long init = 0;
            for (ReserveWorkTimeCount item : list) {
                if (workTime.getId().equals(item.getWorkTimeId()) && item.getVerifyStatus() == ReserveVerifyStatus.init) {
                    init = item.getCount();
                    break;
                }
            }
            initList.add(init);

            long checkIn = 0;
            for (ReserveWorkTimeCount item : list) {
                if (workTime.getId().equals(item.getWorkTimeId()) && item.getVerifyStatus() == ReserveVerifyStatus.check_in) {
                    checkIn = item.getCount();
                    break;
                }
            }
            checkInList.add(checkIn);

            long verified = 0;
            for (ReserveWorkTimeCount item : list) {
                if (workTime.getId().equals(item.getWorkTimeId()) && item.getVerifyStatus() == ReserveVerifyStatus.verified) {
                    verified = item.getCount();
                    break;
                }
            }
            verifiedList.add(verified);

            countList.add(init + checkIn + verified);
        }
        res.add(countList);
        res.add(initList);
        res.add(checkInList);
        res.add(verifiedList);
        return res;
    }

}
