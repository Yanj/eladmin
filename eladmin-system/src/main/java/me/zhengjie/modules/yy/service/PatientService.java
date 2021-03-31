package me.zhengjie.modules.yy.service;

import me.zhengjie.domain.vo.HisCkItemVo;
import me.zhengjie.modules.yy.domain.Patient;
import me.zhengjie.modules.yy.service.dto.PatientCriteria;
import me.zhengjie.modules.yy.service.dto.PatientDto;
import me.zhengjie.modules.yy.service.dto.PatientSync;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @date 2020-12-24 14:32
 */
public interface PatientService {

    /**
     * 本地同步患者数据
     *
     * @param patientSync .
     * @return .
     */
    PatientDto syncLocal(PatientSync patientSync);

    /**
     * 同步患者数据
     *
     * @param criteria .
     */
    PatientDto sync(PatientCriteria criteria) throws Exception;

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(PatientCriteria criteria, Pageable pageable);

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
