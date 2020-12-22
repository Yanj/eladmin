package me.zhengjie.modules.ptt.service;

import me.zhengjie.modules.ptt.domain.PatientTermReserve;
import me.zhengjie.modules.ptt.domain.PatientTermReserveResource;
import me.zhengjie.modules.ptt.service.dto.PatientTermReserveCriteria;
import me.zhengjie.modules.ptt.service.dto.PatientTermReserveDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 患者套餐预约服务
 *
 * @author yanjun
 * @date 2020-11-28 11:08
 */
public interface PatientTermReserveService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(PatientTermReserveCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<PatientTermReserveDto>
     */
    List<PatientTermReserveDto> queryAll(PatientTermReserveCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return PatientTermReserveDto
     */
    PatientTermReserveDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return PatientTermReserveDto
     */
    PatientTermReserveDto create(PatientTermReserve resources);

    /**
     * 创建
     *
     * @param resources /
     * @return PatientTermReserveDto
     */
    PatientTermReserveDto[] create(PatientTermReserve[] resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(PatientTermReserve resources);

    /**
     * 签到
     *
     * @param resources .
     */
    void checkIn(PatientTermReserve resources);

    /**
     * 作废
     *
     * @param resources .
     */
    void cancel(PatientTermReserve resources);

    /**
     * 核销
     *
     * @param resources .
     */
    void verify(PatientTermReserveResource[] resources);

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
    void download(List<PatientTermReserveDto> all, HttpServletResponse response) throws IOException;

}
