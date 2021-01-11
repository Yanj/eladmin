package me.zhengjie.modules.yy.service;

import me.zhengjie.modules.yy.domain.Reserve;
import me.zhengjie.modules.yy.domain.ReserveVerify;
import me.zhengjie.modules.yy.service.dto.*;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @date 2020-12-24 14:32
 */
public interface ReserveService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(ReserveCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<ReserveDto>
     */
    List<ReserveDto> queryAll(ReserveCriteria criteria);

    /**
     * 查询预约统计
     *
     * @param criteria .
     * @return .
     */
    ReserveCountDto queryReserveCount(ReserveCountCriteria criteria);

    /**
     * 查询预约选择的资源分组
     *
     * @param id .
     * @return .
     */
    ResourceGroupForReserveDto queryResources(Long id);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return ReserveDto
     */
    ReserveDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return ReserveDto
     */
    ReserveDto create(Reserve resources) throws Exception;

    /**
     * 创建
     *
     * @param resources /
     * @return ReserveDto
     */
    List<ReserveDto> create(Reserve[] resources) throws Exception;

    /**
     * 核销
     *
     * @param resources /
     */
    ReserveDto verify(Reserve resources);

    /**
     * 取消
     *
     * @param resources /
     */
    ReserveDto cancel(Reserve resources);

    /**
     * 签到
     *
     * @param resources /
     */
    ReserveDto checkIn(Reserve resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(Reserve resources);

    /**
     * 多选删除
     *
     * @param ids /
     */
    void deleteAll(Long[] ids);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<ReserveDto> all, HttpServletResponse response) throws IOException;


}
