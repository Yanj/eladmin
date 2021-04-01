package me.zhengjie.modules.yy.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.yy.domain.UserReserveCount;
import me.zhengjie.modules.yy.repository.ReserveRepository;
import me.zhengjie.modules.yy.service.UserWorkCountService;
import me.zhengjie.modules.yy.service.dto.UserReserveCountCriteria;
import me.zhengjie.modules.yy.util.TimeUtil;
import me.zhengjie.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

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
