package me.zhengjie.modules.ptt.service.impl;

import lombok.AllArgsConstructor;
import me.zhengjie.modules.ptt.domain.PatientTermReserve;
import me.zhengjie.modules.ptt.domain.Term;
import me.zhengjie.modules.ptt.domain.TermResourceType;
import me.zhengjie.modules.ptt.repository.PatientTermReserveRepository;
import me.zhengjie.modules.ptt.repository.ResourceRepository;
import me.zhengjie.modules.ptt.repository.TermRepository;
import me.zhengjie.modules.ptt.repository.TermResourceTypeRepository;
import me.zhengjie.modules.ptt.service.TermResourceTypeService;
import me.zhengjie.modules.ptt.service.TermService;
import me.zhengjie.modules.ptt.service.dto.*;
import me.zhengjie.modules.ptt.service.mapstruct.PatientTermReserveMapper;
import me.zhengjie.modules.ptt.service.mapstruct.TermMapper;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.service.dto.HisCkItemDto;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.ValidationUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 套餐信息服务
 *
 * @author yanjun
 * @date 2020-11-28 11:17
 */
@Service
@AllArgsConstructor
public class TermServiceImpl implements TermService {

    private final TermRepository repository;
    private final TermMapper mapper;

    private final TermResourceTypeService termResourceTypeService;
    private final TermResourceTypeRepository termResourceTypeRepository;
    private final ResourceRepository resourceRepository;
    private final PatientTermReserveRepository patientTermReserveRepository;

    private final PatientTermReserveMapper patientTermReserveMapper;

    @Override
    public Map<String, List<TermReserveTimeDto>> queryReserveTime(TermCriteria criteria) {
        final Long deptId = criteria.getDeptId();
        final Long termId = criteria.getTermId();
        if (null == deptId) {
            throw new RuntimeException("部门 id 不能为空");
        }
        if (null == termId) {
            throw new RuntimeException("套餐 id 不能为空");
        }

        Map<String, List<TermReserveTimeDto>> res = new LinkedHashMap<>();

        // 传入指定日期
        if (null != criteria.getDate()) {
            String date = criteria.getDate();
            res.put(date, getReserveTimeList(deptId, termId, date));
            return res;
        }

        // 生成最近几天的预约情况
        int dayCount = 7;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < dayCount; i++) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            String date = dateFormat.format(calendar.getTime());
            res.put(date, getReserveTimeList(deptId, termId, date));
        }

        return res;
    }

    private List<TermReserveTimeDto> getReserveTimeList(Long deptId, Long termId, String date) {
        if (null == deptId) {
            throw new RuntimeException("部门 id 不能为空");
        }
        if (null == termId) {
            throw new RuntimeException("套餐 id 不能为空");
        }
        if (StringUtils.isEmpty(date)) {
            throw new RuntimeException("预约日期不能为空");
        }

        // 查询医院工作时段
        List<Pair<Calendar, Calendar>> timeRangeList = getWorkTime(date);

        // 休息间隔: 默认 0
        int deptRestTime = 0;

        // 查询套餐信息
        Term term = repository.findById(termId).orElseGet(Term::new);
        if (null == term.getId()) {
            throw new RuntimeException("未查询到相应的套餐");
        }

        // 套餐使用时长
        int termDuration = 0;
        if (null != term.getDuration()) {
            termDuration = term.getDuration().intValue();
        }
        // 未设置套餐时长, 获取默认时长配置
        if (termDuration <= 0) {
            termDuration = 30; // 默认 30 分钟
        }

        // 最大可预约数量
        int countTotal = 99; // 默认最大预约数量 99
        List<PatientTermReserve> reserveList;
        // 获取套餐资源必须类型
        TermResourceType resourceType = termResourceTypeRepository.findFirstByTermIdAndDeptIdAndNullable(termId, deptId, false);
        if (null != resourceType) {
            // 查询资源总数
            Long resourceCount = resourceRepository.findCountByDeptIdAndTermId(resourceType.getId(), deptId);
            if (null != resourceCount) {
                countTotal = resourceCount.intValue();
            }
            // 查询已预约数量
            reserveList = patientTermReserveRepository.findByDeptAndResourceType(deptId, resourceType.getId(), date);
        } else {
            // 查询已预约数量
            reserveList = patientTermReserveRepository.findByDeptAndTerm(deptId, termId, date);
        }

        // 查询已预约的列表
        List<PatientTermReserve> patientTermReserveList = patientTermReserveRepository.findAllByDeptIdAndTermIdAndDateAndStatus(deptId, termId,
                date, "0");

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        // 根据时长生成列表
        List<TermReserveTimeDto> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (Pair<Calendar, Calendar> timeRange : timeRangeList) {
            calendar.setTime(timeRange.getFirst().getTime());
            while (true) {
                TermReserveTimeDto item = new TermReserveTimeDto();
                item.setDeptId(deptId);
                item.setTermId(termId);
                item.setDate(date);

                // 开始时间
                String beginTime = timeFormat.format(calendar.getTime());
                item.setBeginTime(beginTime);

                // 增加时间
                calendar.add(Calendar.MINUTE, termDuration);

                // 结束时间
                String endTime = timeFormat.format(calendar.getTime());
                item.setEndTime(endTime);

                // 计数
                int count = 0;
                if (null != reserveList && !reserveList.isEmpty()) {
                    for (int i = 0; i < reserveList.size(); i++) {
                        PatientTermReserve patientTermReserve = reserveList.get(i);
                        if (null == patientTermReserve) {
                            continue;
                        }

                        if (1 == timeInRange(patientTermReserve, beginTime, endTime)) {
                            count++;
                        }
                    }
                }
                item.setAvailable(countTotal - count);
                item.setCount(countTotal);

                // 已预约列表
                List<PatientTermReserveDto> termReserveList = new ArrayList<>();
                if (null != patientTermReserveList) {
                    for (int i = 0; i < patientTermReserveList.size(); i++) {
                        PatientTermReserve patientTermReserve = patientTermReserveList.get(i);
                        if (timeInRange(patientTermReserve, beginTime, endTime) == 1) {
                            termReserveList.add(patientTermReserveMapper.toDto(patientTermReserve));
                        }
                    }
                }
                item.setPatientTermReserveList(termReserveList);

                list.add(item);

                // 增加时间
                calendar.add(Calendar.MINUTE, deptRestTime);

                // 超过工作时间, 结束
                if (calendar.compareTo(timeRange.getSecond()) >= 0) {
                    break;
                }
            }
        }

        return list;
    }

    private List<Pair<Calendar, Calendar>> getWorkTime(String date) {
        List<Pair<Calendar, Calendar>> list = new ArrayList<>();

        // ... 开始工作时间
        Calendar calendar = Calendar.getInstance();
        Calendar beginTime1 = Calendar.getInstance();
        // ... 默认 08:00
        beginTime1.set(Calendar.MILLISECOND, 0);
        beginTime1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 8, 0, 0);

        // ... 结束工作时间
        Calendar endTime1 = Calendar.getInstance();
        // ... 默认 11:30
        endTime1.set(Calendar.MILLISECOND, 0);
        endTime1.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 11, 30, 0);

        list.add(Pair.of(beginTime1, endTime1));

        // ... 开始工作时间
        Calendar beginTime2 = Calendar.getInstance();
        // ... 默认 13:30
        beginTime2.set(Calendar.MILLISECOND, 0);
        beginTime2.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 13, 30, 0);

        // ... 结束工作时间
        Calendar endTime2 = Calendar.getInstance();
        // ... 默认 17:30
        endTime2.set(Calendar.MILLISECOND, 0);
        endTime2.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 17, 30, 0);

        list.add(Pair.of(beginTime2, endTime2));

        return list;
    }

    private int timeInRange(PatientTermReserve reserve, String beginTime, String endTime) {
        if (null == reserve || StringUtils.isEmpty(beginTime) || StringUtils.isEmpty(endTime)) {
            return -1;
        }

        if (StringUtils.isEmpty(reserve.getBeginTime()) || StringUtils.isEmpty(reserve.getEndTime())) {
            return -1;
        }

        if (reserve.getBeginTime().compareTo(beginTime) >= 0 && reserve.getEndTime().compareTo(endTime) <= 0) {
            return 1;
        }

        return 0;
    }

    @Override
    public List<TermReserveDto> queryReserveList(TermCriteria criteria) {
        final Long deptId = criteria.getDeptId();
        final Long termId = criteria.getTermId();
        if (null == deptId) {
            throw new RuntimeException("部门 id 不能为空");
        }
        if (null == termId) {
            throw new RuntimeException("套餐 id 不能为空");
        }

        // 日期
        final String date;
        if (null != criteria.getDate()) {
            date = criteria.getDate();
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            date = dateFormat.format(new Date());
        }

        // 查询医院工作时段
        List<Pair<Calendar, Calendar>> timeRangeList = getWorkTime(date);

        // 休息间隔: 默认 0
        int deptRestTime = 0;

        // 查询套餐信息
        Term term = repository.findById(termId).orElseGet(Term::new);
        if (null == term.getId()) {
            throw new RuntimeException("未查询到相应的套餐");
        }

        // 套餐使用时长
        int termDuration = 0;
        if (null != term.getDuration()) {
            termDuration = term.getDuration().intValue();
        }
        // 未设置套餐时长, 获取默认时长配置
        if (termDuration <= 0) {
            termDuration = 30; // 默认 30 分钟
        }

        // 查询已预约的列表
        List<PatientTermReserve> patientTermReserveList = patientTermReserveRepository.findAllByDeptIdAndTermIdAndDateAndStatus(deptId, termId,
                date, "1");

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        // 根据时长生成列表
        List<TermReserveDto> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (Pair<Calendar, Calendar> timeRange : timeRangeList) {
            calendar.setTime(timeRange.getFirst().getTime());
            while (true) {
                TermReserveDto item = new TermReserveDto();
                item.setDeptId(deptId);
                item.setTermId(termId);
                item.setDate(date);

                // 开始时间
                String beginTime = timeFormat.format(calendar.getTime());
                item.setBeginTime(beginTime);

                // 增加时间
                calendar.add(Calendar.MINUTE, termDuration);

                // 结束时间
                String endTime = timeFormat.format(calendar.getTime());
                item.setEndTime(endTime);

                // 已预约列表
                List<PatientTermReserveDto> reserveList = new ArrayList<>();
                if (null != patientTermReserveList) {
                    for (int i = 0; i < patientTermReserveList.size(); i++) {
                        PatientTermReserve patientTermReserve = patientTermReserveList.get(i);
                        if (timeInRange(patientTermReserve, beginTime, endTime) == 1) {
                            reserveList.add(patientTermReserveMapper.toDto(patientTermReserve));
                        }
                    }
                }
                item.setPatientTermReserveList(reserveList);

                list.add(item);

                // 增加时间
                calendar.add(Calendar.MINUTE, deptRestTime);

                // 超过工作时间, 结束
                if (calendar.compareTo(timeRange.getSecond()) >= 0) {
                    break;
                }
            }
        }

        return list;
    }

    @Override
    public Map<String, Term> createOrUpdate(Dept userDept, List<HisCkItemDto> ckItemList) {
        if (null == ckItemList || ckItemList.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Map<String, Term> itemDtoMap = new HashMap<>();
        for (HisCkItemDto ckItem : ckItemList) {
            // 忽略空数据
            if (null == ckItem || StringUtils.isEmpty(ckItem.getItemCode())) continue;
            // 忽略已添加的数据
            if (itemDtoMap.containsKey(ckItem.getItemCode())) continue;

            Term item = repository.findFirstByCode(ckItem.getItemCode()).orElseGet(Term::new);
            if (item.getId() == null) {
                item.setCode(ckItem.getItemCode());
                item.setName(ckItem.getItemName());
                item.setPrice(ckItem.getPrice());
                item.setTimes(ckItem.getAmount().intValue());
                item.setUnit(ckItem.getUnit());
                item.setAmount(ckItem.getCosts());
                item.setDuration(0L); // 时长
                item = repository.save(item);

                // 更新套餐资源类型
                TermResourceType rcvItemResourceType = new TermResourceType();
                rcvItemResourceType.setTerm(item);
                termResourceTypeService.create(rcvItemResourceType);
            }
            itemDtoMap.put(item.getCode(), item);
        }

        return itemDtoMap;
    }

    @Override
    public Map<String, Object> queryAll(TermCriteria criteria, Pageable pageable) {
        Page<Term> page = repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(mapper::toDto));
    }

    @Override
    public List<TermDto> queryAll(TermCriteria criteria) {
        return mapper.toDto(repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Transactional
    @Override
    public TermDto findById(Long id) {
        Term instance = repository.findById(id).orElseGet(Term::new);
        ValidationUtil.isNull(instance.getId(), "Term", "id", id);
        return mapper.toDto(instance);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TermDto create(Term resources) {
        return mapper.toDto(repository.save(resources));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Term resources) {
        Term instance = repository.findById(resources.getId()).orElseGet(Term::new);
        ValidationUtil.isNull(instance.getId(), "Term", "id", resources.getId());
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
    public void download(List<TermDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (TermDto item : all) {
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
