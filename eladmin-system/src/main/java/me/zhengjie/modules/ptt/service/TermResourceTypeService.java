package me.zhengjie.modules.ptt.service;

import me.zhengjie.modules.ptt.domain.TermResourceType;
import me.zhengjie.modules.ptt.service.dto.TermResourceTypeCriteria;
import me.zhengjie.modules.ptt.service.dto.TermResourceTypeDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 套餐资源类型服务
 *
 * @author yanjun
 * @date 2020-11-28 11:08
 */
public interface TermResourceTypeService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(TermResourceTypeCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<TermResourceTypeDto>
     */
    List<TermResourceTypeDto> queryAll(TermResourceTypeCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return TermResourceTypeDto
     */
    TermResourceTypeDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return TermResourceTypeDto
     */
    TermResourceTypeDto create(TermResourceType resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(TermResourceType resources);

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
    void download(List<TermResourceTypeDto> all, HttpServletResponse response) throws IOException;

}
