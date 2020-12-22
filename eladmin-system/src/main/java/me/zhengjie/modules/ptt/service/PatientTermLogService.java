package me.zhengjie.modules.ptt.service;

import me.zhengjie.modules.ptt.domain.PatientTermLog;
import me.zhengjie.modules.ptt.service.dto.PatientTermLogCriteria;
import me.zhengjie.modules.ptt.service.dto.PatientTermLogDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 患者套餐日志服务
 *
 * @author yanjun
 * @date 2020-11-28 11:08
 */
public interface PatientTermLogService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(PatientTermLogCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<PatientTermLogDto>
     */
    List<PatientTermLogDto> queryAll(PatientTermLogCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return PatientTermLogDto
     */
    PatientTermLogDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return PatientTermLogDto
     */
    PatientTermLogDto create(PatientTermLog resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(PatientTermLog resources);

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
    void download(List<PatientTermLogDto> all, HttpServletResponse response) throws IOException;

}
