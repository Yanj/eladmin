package me.zhengjie.modules.ptt.service;

import me.zhengjie.modules.ptt.domain.Patient;
import me.zhengjie.modules.ptt.domain.PatientTerm;
import me.zhengjie.modules.ptt.domain.Term;
import me.zhengjie.modules.ptt.service.dto.PatientTermCriteria;
import me.zhengjie.modules.ptt.service.dto.PatientTermDto;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.service.dto.HisCkItemDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 患者套餐服务
 *
 * @author yanjun
 * @date 2020-11-28 11:08
 */
public interface PatientTermService {

    /**
     * 更新
     *
     * @param userDept .
     * @param patient  .
     * @param term     .
     * @param ckItem   .
     */
    void createOrUpdate(Dept userDept, Patient patient, Term term, HisCkItemDto ckItem);

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
