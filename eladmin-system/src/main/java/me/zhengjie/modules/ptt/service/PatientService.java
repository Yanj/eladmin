package me.zhengjie.modules.ptt.service;

import me.zhengjie.domain.vo.HisCkItemVo;
import me.zhengjie.modules.ptt.domain.Patient;
import me.zhengjie.modules.ptt.domain.PatientCus;
import me.zhengjie.modules.ptt.domain.PatientFull;
import me.zhengjie.modules.ptt.domain.PatientWithCus;
import me.zhengjie.modules.ptt.service.dto.CusCriteria;
import me.zhengjie.modules.ptt.service.dto.PatientCriteria;
import me.zhengjie.modules.ptt.service.dto.PatientDto;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.service.dto.HisCkItemDto;
import org.springframework.data.domain.Page;
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
public interface PatientService {

    /**
     * 不同部门查询数据
     *
     * @param criteria .
     * @param pageable .
     * @return .
     */
    Map<String, Object> findFull(PatientCriteria criteria, Pageable pageable);

    /**
     * 不同部门查询 自定义数据
     * @param criteria .
     * @param pageable .
     * @return .
     */
    Map<String, Object> findFullCols(PatientCriteria criteria, Pageable pageable);

    void updateCol(PatientWithCus patientWithCus);

    void updateDept(Patient resources, PatientDto patientDto);

    /**
     * 同步数据
     *
     * @param vo          .
     * @param currentUser .
     * @throws Exception .
     */
    Map<String, Object> syncData(JwtUserDto currentUser, HisCkItemVo vo) throws Exception;

    /**
     * 新增或者更新
     *
     * @param userDept   .
     * @param ckItemList .
     * @return .
     */
    Map<String, Patient> createOrUpdate(Dept userDept, List<HisCkItemDto> ckItemList);

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(PatientCriteria criteria, Pageable pageable);

    /**
     * 查询患者 - 带有自定义信息
     *
     * @param criteria .
     * @param pageable .
     * @return .
     */
    Map<String, Object> queryCols(PatientCriteria criteria, Pageable pageable);

    /**
     * 更新患者自定义信息
     *
     * @param resources .
     */
    void updateCols(PatientDto resources);

    /**
     * 管理查询
     *
     * @param criteria .
     * @param pageable .
     * @return .
     */
    Map<String, Object> queryWithDept(PatientCriteria criteria, Pageable pageable);

    /**
     * 查询自定义信息
     *
     * @param deptId .
     * @param patientId .
     * @param pageable .
     * @return .
     */
    Map<String, Object> queryCusByPatientId(Long deptId, Long patientId, Pageable pageable);

    /**
     * 查询部门所有数据不分页
     *
     * @param deptId .
     * @return .
     */
    List<PatientDto> queryByDeptId(Long deptId);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<PatientDto>
     */
    List<PatientDto> queryAll(PatientCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return PatientDto
     */
    PatientDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return PatientDto
     */
    PatientDto create(Patient resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(Patient resources);

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
    void download(List<PatientDto> all, HttpServletResponse response) throws IOException;

}
