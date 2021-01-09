package me.zhengjie.modules.yy.service;

import me.zhengjie.modules.system.service.dto.DeptDto;
import me.zhengjie.modules.yy.domain.Hospital;
import me.zhengjie.modules.yy.service.dto.HospitalCriteria;
import me.zhengjie.modules.yy.service.dto.HospitalDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @date 2020-12-24 21:22
 */
public interface HospitalService {

    /**
     * 查询所有医院
     *
     * @return .
     */
    List<DeptDto> queryAll();

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(HospitalCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<HospitalDto>
     */
    List<HospitalDto> querySelf(HospitalCriteria criteria);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<HospitalDto>
     */
    List<HospitalDto> queryAll(HospitalCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return HospitalDto
     */
    HospitalDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return HospitalDto
     */
    HospitalDto create(Hospital resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(Hospital resources);

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
    void download(List<HospitalDto> all, HttpServletResponse response) throws IOException;

}
