package me.zhengjie.modules.yy.service;

import me.zhengjie.modules.yy.domain.ResourceGroup;
import me.zhengjie.modules.yy.service.dto.ResourceGroupCriteria;
import me.zhengjie.modules.yy.service.dto.ResourceGroupDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @date 2020-12-24 14:32
 */
public interface ResourceGroupService {

    /**
     * 修改分类
     *
     * @param resources        .
     * @param resourceGroupDto .
     */
    void updateResourceCategory(ResourceGroup resources, ResourceGroupDto resourceGroupDto);

    /**
     * 根据父 id 查询
     *
     * @param pid .
     * @return .
     */
    List<ResourceGroupDto> getResourceGroups(Long pid, Long deptId);

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(ResourceGroupCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<ResourceGroupDto>
     */
    List<ResourceGroupDto> queryAll(ResourceGroupCriteria criteria);

    /**
     * 查询套餐对应的资源分组
     *
     * @param deptId .
     * @param termId .
     * @return .
     */
    List<ResourceGroupDto> queryByDeptIdAndTermId(Long deptId, Long termId);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return ResourceGroupDto
     */
    ResourceGroupDto findById(Long id);

    /**
     * 创建
     *
     * @param ResourceGroups /
     * @return ResourceGroupDto
     */
    ResourceGroupDto create(ResourceGroup ResourceGroups);

    /**
     * 编辑
     *
     * @param ResourceGroups /
     */
    void update(ResourceGroup ResourceGroups);

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
    void download(List<ResourceGroupDto> all, HttpServletResponse response) throws IOException;

}
