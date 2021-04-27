package me.zhengjie.modules.yy.service;

import me.zhengjie.modules.yy.domain.ReserveResource;
import me.zhengjie.modules.yy.service.dto.ReserveCountCriteria;
import me.zhengjie.modules.yy.service.dto.ReserveResourceCriteria;
import me.zhengjie.modules.yy.service.dto.ReserveResourceDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @date 2020-12-24 14:32
 */
public interface ReserveResourceService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(ReserveResourceCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<ReserveResourceDto>
     */
    List<ReserveResourceDto> queryAll(ReserveResourceCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return ReserveResourceDto
     */
    ReserveResourceDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return ReserveResourceDto
     */
    ReserveResourceDto create(ReserveResource resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(ReserveResource resources);

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
    void download(List<ReserveResourceDto> all, HttpServletResponse response) throws IOException;

    /**
     * 查询预约资源统计
     *
     * @param criteria .
     * @return .
     */
    List<Map<String, Object>> queryReserveCount(ReserveCountCriteria criteria) throws Exception;

    /**
     * 查询预约资源统计
     *
     * @param criteria .
     * @return .
     */
    Map<String, Object> queryReserveCount2(ReserveCountCriteria criteria) throws Exception;
}
