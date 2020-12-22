package me.zhengjie.modules.ptt.service;

import me.zhengjie.modules.ptt.domain.PatientCus;
import me.zhengjie.modules.ptt.service.dto.PatientCusCriteria;
import me.zhengjie.modules.ptt.service.dto.PatientCusDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 患者自定义服务
 *
 * @author yanjun
 * @date 2020-11-28 11:08
 */
public interface PatientCusService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(PatientCusCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<PatientCusDto>
     */
    List<PatientCusDto> queryAll(PatientCusCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return PatientCusDto
     */
    PatientCusDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return PatientCusDto
     */
    PatientCusDto create(PatientCus resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(PatientCus resources);

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
    void download(List<PatientCusDto> all, HttpServletResponse response) throws IOException;

}
