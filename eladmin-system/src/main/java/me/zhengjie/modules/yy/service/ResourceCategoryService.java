package me.zhengjie.modules.yy.service;

import me.zhengjie.modules.yy.domain.ResourceCategory;
import me.zhengjie.modules.yy.service.dto.ResourceCategoryCriteria;
import me.zhengjie.modules.yy.service.dto.ResourceCategoryDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @date 2020-12-24 14:32
 */
public interface ResourceCategoryService {

    /**
     * 根据父 id 查询
     *
     * @param pid .
     * @return .
     */
    List<ResourceCategoryDto> getResourceCategories(Long pid, Long deptId);

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(ResourceCategoryCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<ResourceCategoryDto>
     */
    List<ResourceCategoryDto> queryAll(ResourceCategoryCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return ResourceCategoryDto
     */
    ResourceCategoryDto findById(Long id);

    /**
     * 创建
     *
     * @param resourceCategories /
     * @return ResourceCategoryDto
     */
    ResourceCategoryDto create(ResourceCategory resourceCategories);

    /**
     * 编辑
     *
     * @param resourceCategories /
     */
    void update(ResourceCategory resourceCategories);

    /**
     * 修改绑定的分组
     *
     * @param resources        .
     * @param resourceCategory .
     */
    void updateResourceGroup(ResourceCategory resources, ResourceCategoryDto resourceCategory);

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
    void download(List<ResourceCategoryDto> all, HttpServletResponse response) throws IOException;

}
