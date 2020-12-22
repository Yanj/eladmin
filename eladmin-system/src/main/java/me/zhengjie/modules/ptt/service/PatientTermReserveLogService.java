package me.zhengjie.modules.ptt.service;

import me.zhengjie.modules.ptt.domain.PatientTermReserveLog;
import me.zhengjie.modules.ptt.service.dto.PatientTermReserveLogCriteria;
import me.zhengjie.modules.ptt.service.dto.PatientTermReserveLogDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 患者套餐预约日志服务
 *
 * @author yanjun
 * @date 2020-11-28 11:08
 */
public interface PatientTermReserveLogService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(PatientTermReserveLogCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<PatientTermReserveLogDto>
     */
    List<PatientTermReserveLogDto> queryAll(PatientTermReserveLogCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return PatientTermReserveLogDto
     */
    PatientTermReserveLogDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return PatientTermReserveLogDto
     */
    PatientTermReserveLogDto create(PatientTermReserveLog resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(PatientTermReserveLog resources);

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
    void download(List<PatientTermReserveLogDto> all, HttpServletResponse response) throws IOException;

}
