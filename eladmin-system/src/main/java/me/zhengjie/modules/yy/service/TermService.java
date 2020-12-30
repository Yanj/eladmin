package me.zhengjie.modules.yy.service;

import me.zhengjie.modules.yy.domain.Term;
import me.zhengjie.modules.yy.service.dto.TermCriteria;
import me.zhengjie.modules.yy.service.dto.TermDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @date 2020-12-24 14:32
 */
public interface TermService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(TermCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<TermDto>
     */
    List<TermDto> queryAll(TermCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return TermDto
     */
    TermDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return TermDto
     */
    TermDto create(Term resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(Term resources);

    /**
     * 修改分组关联
     *
     * @param resources .
     * @param termDto .
     */
    void updateResourceCategory(Term resources, TermDto termDto);

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
    void download(List<TermDto> all, HttpServletResponse response) throws IOException;

}
