package me.zhengjie.modules.ptt.service;

import me.zhengjie.modules.ptt.domain.Patient;
import me.zhengjie.modules.ptt.domain.PatientDept;
import me.zhengjie.modules.ptt.service.dto.PatientDeptCriteria;
import me.zhengjie.modules.ptt.service.dto.PatientDeptDto;
import me.zhengjie.modules.system.domain.Dept;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 患者服务
 *
 * @author yanjun
 * @date 2020-11-28 11:08
 */
public interface PatientDeptService {

    /**
     * 更新
     *
     * @param userDept .
     * @param patient  .
     * @return .
     */
    PatientDeptDto createOrUpdate(Dept userDept, Patient patient);

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(PatientDeptCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<PatientDeptDto>
     */
    List<PatientDeptDto> queryAll(PatientDeptCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return PatientDeptDto
     */
    PatientDeptDto findById(PatientDept.PK id);

    /**
     * 创建
     *
     * @param resources /
     * @return PatientDeptDto
     */
    PatientDeptDto create(PatientDept resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(PatientDept resources);

    /**
     * 多选删除
     *
     * @param ids /
     */
    void deleteAll(PatientDept.PK[] ids);

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<PatientDeptDto> all, HttpServletResponse response) throws IOException;

}
