package me.zhengjie.modules.yy.service;

import me.zhengjie.modules.yy.domain.Resource;
import me.zhengjie.modules.yy.service.dto.ResourceCriteria;
import me.zhengjie.modules.yy.service.dto.ResourceDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @date 2020-12-24 14:32
 */
public interface ResourceService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(ResourceCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<ResourceDto>
     */
    List<ResourceDto> queryAll(ResourceCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return ResourceDto
     */
    ResourceDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return ResourceDto
     */
    ResourceDto create(Resource resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(Resource resources);

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
    void download(List<ResourceDto> all, HttpServletResponse response) throws IOException;

}
