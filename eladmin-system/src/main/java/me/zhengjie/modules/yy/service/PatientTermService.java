package me.zhengjie.modules.yy.service;

import me.zhengjie.modules.yy.domain.PatientTerm;
import me.zhengjie.modules.yy.domain.PatientTermTimesCount;
import me.zhengjie.modules.yy.service.dto.PatientTermCriteria;
import me.zhengjie.modules.yy.service.dto.PatientTermDto;
import me.zhengjie.modules.yy.service.dto.PatientTermTimesCountCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @date 2020-12-24 14:32
 */
public interface PatientTermService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(PatientTermCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<PatientTermDto>
     */
    List<PatientTermDto> queryAll(PatientTermCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return PatientTermDto
     */
    PatientTermDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return PatientTermDto
     */
    PatientTermDto create(PatientTerm resources);

    /**
     * 创建
     *
     * @param resources /
     * @return PatientTermDto
     */
    PatientTermDto createFreeOne(PatientTerm resources);

    /**
     * 创建
     *
     * @param resources /
     * @return PatientTermDto
     */
    PatientTermDto createFreeTwo(PatientTerm resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(PatientTerm resources);

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
    void download(List<PatientTermDto> all, HttpServletResponse response) throws IOException;


}
