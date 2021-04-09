package me.zhengjie.modules.yy.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.yy.domain.UserReserveCount;
import me.zhengjie.modules.yy.repository.ReserveRepository;
import me.zhengjie.modules.yy.repository.TermRepository;
import me.zhengjie.modules.yy.service.UserWorkCountService;
import me.zhengjie.modules.yy.service.dto.TermSmallDto;
import me.zhengjie.modules.yy.service.dto.UserReserveCountCriteria;
import me.zhengjie.modules.yy.service.mapstruct.TermSmallMapper;
import me.zhengjie.modules.yy.util.TimeUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author yanjun
 * @date 2021-04-01 11:41
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserWorkCountServiceImpl implements UserWorkCountService {

    private final ReserveRepository repository;

    private final TermRepository termRepository;
    private final TermSmallMapper termSmallMapper;

    @Override
    public void download(List<UserReserveCount> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserReserveCount item : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户ID", item.getUserId());
            map.put("用户名称", item.getUserName());
            map.put("套餐ID", item.getTermId());
            map.put("套餐名称", item.getTermName());
            map.put("日期", item.getDate());
            map.put("数量", item.getCount());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void download(Map<String, Object> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();

        List<TermSmallDto> terms = (List<TermSmallDto>) all.get("terms");
        List<Map<String, Object>> users = (List<Map<String, Object>>) all.get("list");

        for (Map<String, Object> user : users) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户ID", user.get("userId"));
            map.put("用户名称", user.get("userName"));
            for (TermSmallDto term : terms) {
                map.put(term.getName(), user.get(term.getId().toString()));
            }
            map.put("合计", user.get("total"));
            list.add(map);
        }

        FileUtil.downloadExcel(list, response);
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
    public Map<String, Object> queryUserWorkCount(UserReserveCountCriteria criteria) {
        Long orgId = criteria.getOrgId();
        Long comId = criteria.getComId();

        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        if (!user.isAdmin()) {
            orgId = user.getOrgId();
            comId = user.getComId();
        }
        if (null == orgId) {
            throw new BadRequestException("orgId 不能为空");
        }
        if (null == comId) {
            throw new BadRequestException("comId 不能为空");
        }

        Map<String, Object> map = new LinkedHashMap<>();

        // 查询套餐列表
        List<TermSmallDto> termList = termSmallMapper.toDto(termRepository.findAllByComId(orgId, comId));
        if (termList.isEmpty()) {
            return new HashMap<>();
        }

        map.put("terms", termList);

        List<Map<String, Object>> list = new ArrayList<>();
        map.put("list", list);

        // 查询工作量统计
        List<UserReserveCount> reserveCountList = queryUserReserveCount(criteria);
        if (reserveCountList.isEmpty()) {
            return map;
        }

        Map<String, Map<String, Object>> userListMap = new LinkedHashMap<>();
        for (UserReserveCount reserveCount : reserveCountList) {
            if (null == reserveCount || null == reserveCount.getUserId()) {
                continue;
            }

            String userId = reserveCount.getUserId().toString();
            Map<String, Object> userMap = userListMap.get(userId);
            if (null == userMap) {
                userMap = new LinkedHashMap<>();
                userMap.put("userId", reserveCount.getUserId());
                userMap.put("userName", reserveCount.getUserName());
                for (TermSmallDto term : termList) {
                    if (null == term || null == term.getId()) {
                        continue;
                    }
                    userMap.put(term.getId().toString(), 0L);
                }
                userListMap.put(userId, userMap);
            }

            if (null == reserveCount.getTermId()) {
                continue;
            }
            String termId = reserveCount.getTermId().toString();
            Long count = (Long) userMap.get(termId);
            count += reserveCount.getCount();
            userMap.put(termId, count);
        }

        list.addAll(userListMap.values());

        for (Map<String, Object> userMap : list) {
            long total = 0;
            for (TermSmallDto term : termList) {
                if (null == term || null == term.getId()) {
                    continue;
                }
                Long count = (Long) userMap.get(term.getId().toString());
                if (null != count) {
                    total += count;
                }
            }
            userMap.put("total", total);
        }

        return map;
    }

}
