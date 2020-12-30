package me.zhengjie.modules.yy.service;

import me.zhengjie.modules.yy.domain.WorkTime;
import me.zhengjie.modules.yy.service.dto.WorkTimeCriteria;
import me.zhengjie.modules.yy.service.dto.WorkTimeDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @date 2020-12-24 14:32
 */
public interface WorkTimeService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(WorkTimeCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<WorkTimeDto>
     */
    List<WorkTimeDto> queryAll(WorkTimeCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return WorkTimeDto
     */
    WorkTimeDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return WorkTimeDto
     */
    WorkTimeDto create(WorkTime resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(WorkTime resources);

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
    void download(List<WorkTimeDto> all, HttpServletResponse response) throws IOException;

}
