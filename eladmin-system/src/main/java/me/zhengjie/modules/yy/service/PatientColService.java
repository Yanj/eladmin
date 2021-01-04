package me.zhengjie.modules.yy.service;

import me.zhengjie.modules.yy.domain.PatientCol;
import me.zhengjie.modules.yy.service.dto.PatientColCriteria;
import me.zhengjie.modules.yy.service.dto.PatientColDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @date 2020-12-24 14:32
 */
public interface PatientColService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(PatientColCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<PatientColDto>
     */
    List<PatientColDto> queryAll(PatientColCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return PatientColDto
     */
    PatientColDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return PatientColDto
     */
    PatientColDto create(PatientCol resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(PatientCol resources);

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
    void download(List<PatientColDto> all, HttpServletResponse response) throws IOException;

}
