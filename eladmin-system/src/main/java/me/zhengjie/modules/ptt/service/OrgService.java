package me.zhengjie.modules.ptt.service;

import me.zhengjie.modules.ptt.service.dto.OrgCriteria;
import me.zhengjie.modules.ptt.service.dto.OrgDto;
import me.zhengjie.modules.system.domain.Dept;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 医院服务
 *
 * @author yanjun
 * @date 2020-11-30 17:29
 */
public interface OrgService {

    /**
     * 根据 PID 查询
     *
     * @param pid .
     * @return .
     */
    List<OrgDto> getOrgs(Long pid);

    Dept findOne(Long id);

    /**
     * 获取所有子节点，包含自身ID
     *
     * @param deptList /
     * @param deptSet  /
     * @return /
     */
    Set<Dept> getChildMenus(List<Dept> deptList, Set<Dept> deptSet);

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(OrgCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<PatientCusDto>
     */
    List<OrgDto> queryAll(OrgCriteria criteria);

    /**
     * 查询所有数据
     *
     * @param criteria .
     * @return .
     */
    List<OrgDto> queryList(OrgCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return PatientCusDto
     */
    OrgDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return PatientCusDto
     */
    OrgDto create(Dept resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(Dept resources);

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
    void download(List<OrgDto> all, HttpServletResponse response) throws IOException;

}
