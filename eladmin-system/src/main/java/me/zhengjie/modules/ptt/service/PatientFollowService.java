package me.zhengjie.modules.ptt.service;

import me.zhengjie.modules.ptt.domain.PatientFollow;
import me.zhengjie.modules.ptt.service.dto.PatientFollowCriteria;
import me.zhengjie.modules.ptt.service.dto.PatientFollowDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 患者跟进服务
 *
 * @author yanjun
 * @date 2020-11-28 11:08
 */
public interface PatientFollowService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(PatientFollowCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<PatientFollowDto>
     */
    List<PatientFollowDto> queryAll(PatientFollowCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return PatientFollowDto
     */
    PatientFollowDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return PatientFollowDto
     */
    PatientFollowDto create(PatientFollow resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(PatientFollow resources);

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
    void download(List<PatientFollowDto> all, HttpServletResponse response) throws IOException;

}
