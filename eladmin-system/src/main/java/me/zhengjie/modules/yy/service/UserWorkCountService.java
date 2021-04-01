package me.zhengjie.modules.yy.service;

import me.zhengjie.modules.yy.domain.UserReserveCount;
import me.zhengjie.modules.yy.service.dto.UserReserveCountCriteria;

import java.util.List;

/**
 * @author yanjun
 * @date 2021-04-01 11:40
 */
public interface UserWorkCountService {

    /**
     * 查询用户工作量
     *
     * @param criteria     .
     * @return .
     */
    List<UserReserveCount> queryUserReserveCount(UserReserveCountCriteria criteria);

}
