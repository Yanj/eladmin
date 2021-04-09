package me.zhengjie.modules.yy.service;

import me.zhengjie.modules.yy.domain.UserReserveCount;
import me.zhengjie.modules.yy.service.dto.UserReserveCountCriteria;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @date 2021-04-01 11:40
 */
public interface UserWorkCountService {

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<UserReserveCount> all, HttpServletResponse response) throws IOException;

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(Map<String, Object> all, HttpServletResponse response) throws IOException;

    /**
     * 查询用户工作量
     *
     * @param criteria .
     * @return .
     */
    List<UserReserveCount> queryUserReserveCount(UserReserveCountCriteria criteria);

    /**
     * 查询用户工作量
     *
     * @param criteria .
     * @return .
     */
    Map<String, Object> queryUserWorkCount(UserReserveCountCriteria criteria);

}
