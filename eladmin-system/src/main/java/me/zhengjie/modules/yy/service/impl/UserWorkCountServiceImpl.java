package me.zhengjie.modules.yy.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.yy.domain.UserReserveCount;
import me.zhengjie.modules.yy.repository.ReserveRepository;
import me.zhengjie.modules.yy.service.UserWorkCountService;
import me.zhengjie.modules.yy.service.dto.UserReserveCountCriteria;
import me.zhengjie.modules.yy.service.dto.WorkTimeDto;
import me.zhengjie.modules.yy.util.TimeUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @date 2021-04-01 11:41
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserWorkCountServiceImpl implements UserWorkCountService {

    private final ReserveRepository repository;

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

}
