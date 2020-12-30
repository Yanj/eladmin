package me.zhengjie.modules.yy.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.yy.domain.*;
import me.zhengjie.modules.yy.repository.*;
import me.zhengjie.modules.yy.service.ReserveService;
import me.zhengjie.modules.yy.service.dto.*;
import me.zhengjie.modules.yy.service.mapstruct.*;
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
import java.math.BigInteger;
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
    private final ReserveLogRepository reserveLogRepository;
    private final ReserveVerifyRepository reserveVerifyRepository;
    private final SmsRepository smsRepository;
    private final WorkTimeRepository workTimeRepository;

    private final ResourceSmallMapper resourceSmallMapper;
    private final WorkTimeSmallMapper workTimeSmallMapper;
    private final TermSmallMapper termSmallMapper;

    @Override
    public Map<String, Object> queryAll(ReserveCriteria criteria, Pageable pageable) {
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
    public ReserveDto create(Reserve resources) {
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
        // 获取可用资源数量
        List<Map<String,Object>> categoryCountMap = repository.queryCategoryCount();
        // 获取已用资源数量
        List<Map<String, Object>> categoryUseCountMap = repository.queryCategoryCount(resources.getDept().getId(), resources.getDate(), resources.getWorkTime().getId());

        long available = Long.MAX_VALUE;
        for (ResourceCategory category : resourceGroup.getResourceCategories()) {
            Long count = 0L;

            for (Map<String, Object> categoryCount : categoryCountMap) {
                if (Objects.equals(categoryCount.get("resource_category_id").toString(), category.getId().toString())) {
                    if (null != categoryCount.get("count")) {
                        count = Long.parseLong(categoryCount.get("count").toString());
                    }
                    break;
                }
            }

            Long useCount = 0L;
            for (Map<String, Object> categoryUseCount : categoryUseCountMap) {
                if (Objects.equals(categoryUseCount.get("resource_category_id").toString(), category.getId().toString())) {
                    if (null != categoryUseCount.get("count")) {
                        useCount = Long.parseLong(categoryUseCount.get("count").toString());
                    }
                    break;
                }
            }
            long last = count - useCount;
            last = last < 0 ? 0: last;
            available = Math.min(last, available);
        }
        if (available <= 0) {
            throw new RuntimeException("可用资源不足");
        }

        // 扣次数
        final int oldTimes = patientTerm.getTimes();
        patientTerm.setTimes(oldTimes - 1);
        patientTermRepository.save(patientTerm);

        // 新增
        resources.setStatus("init");
        ReserveDto res = mapper.toDto(repository.save(resources));

        // 发送短信
        if (!StringUtils.isEmpty(patient.getPhone())) {
            Sms sms = new Sms();
            sms.setBusType("reserve");
            sms.setBusId(res.getId());
            sms.setMobile(patient.getPhone());
            String content = String.format("您购买的套餐[%s], 已预约成功, 请于%s %s前到我院使用, 感谢您的信任, %s",
                    patientTerm.getTermName(), resources.getDate(), resources.getBeginTime(), resources.getDept().getName());
            sms.setContent(content);
            sms.setStatus("init");
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

        return res;
    }

    @Transactional
    @Override
    public ReserveDto verify(ReserveVerify resources) {
        if (null != resources.getId()) {
            throw new RuntimeException("不允许修改");
        }
        if (null == resources.getReserve() || null == resources.getReserve().getId()) {
            throw new RuntimeException("预约不能为空");
        }
        if (null == resources.getResourceGroup()) {
            throw new RuntimeException("资源组不能为空");
        }

        Reserve reserve = repository.getReserveForUpdate(resources.getReserve().getId());
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

        // 判断资源
        Set<Resource> resourceSet = new HashSet<>();
        if (null != resourceGroup.getResourceCategories() && null != resources.getResources()) {
            for (Resource resource : resources.getResources()) {
                if (null == resource || null == resource.getId()) {
                    throw new RuntimeException("资源不能为空");
                }
                resource = resourceRepository.findById(resource.getId()).orElseGet(Resource::new);
                if (null == resource.getId()) {
                    throw new RuntimeException("资源不存在");
                }

                for (ResourceCategory resourceCategory : resourceGroup.getResourceCategories()) {
                    if (null == resourceCategory || null == resourceCategory.getId()) {
                        continue;
                    }

                    if (Objects.equals(resource.getResourceCategory().getId(), resourceCategory.getId())) {
                        resourceSet.add(resource);
                    }
                }
            }
        }
        resources.setResources(resourceSet);

        // 保存
        reserveVerifyRepository.save(resources);

        // 更新状态
        final String oldStatus = reserve.getStatus();
        reserve.setStatus("verified");
        repository.save(reserve);

        // 发送短信
        if (!StringUtils.isEmpty(patient.getPhone())) {
            Sms sms = new Sms();
            sms.setBusType("reserve");
            sms.setBusId(reserve.getId());
            sms.setMobile(patient.getPhone());
            String content = String.format("您预约的套餐[%s], 预约时间 %s %s, 已使用成功, 感谢您的信任, %s",
                    patientTerm.getTermName(), reserve.getDate(), reserve.getBeginTime(), reserve.getDept().getName());
            sms.setContent(content);
            sms.setStatus("init");
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
    public ReserveDto cancel(Reserve resources) {
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

        // TODO 已核销的作废, 检查权限, 更新相关数据
        if ("verified".equalsIgnoreCase(reserve.getStatus())) {

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

        // 发送短信
        if (!StringUtils.isEmpty(patient.getPhone())) {
            Sms sms = new Sms();
            sms.setBusType("reserve");
            sms.setBusId(reserve.getId());
            sms.setMobile(patient.getPhone());
            String content = String.format("您预约的套餐[%s], 预约时间%s %s, 已签到成功, %s",
                    patientTerm.getTermName(), reserve.getDate(), reserve.getBeginTime(), reserve.getDept().getName());
            sms.setContent(content);
            sms.setStatus("init");
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

        // 发送短信
        if (!StringUtils.isEmpty(patient.getPhone())) {
            Sms sms = new Sms();
            sms.setBusType("reserve");
            sms.setBusId(reserve.getId());
            sms.setMobile(patient.getPhone());
            String content = String.format("您预约的套餐[%s], 预约时间%s %s, 已签到成功, %s",
                    patientTerm.getTermName(), reserve.getDate(), reserve.getBeginTime(), reserve.getDept().getName());
            sms.setContent(content);
            sms.setStatus("init");
            smsRepository.save(sms);
        }

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
}
