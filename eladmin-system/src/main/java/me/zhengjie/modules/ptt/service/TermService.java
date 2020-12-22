package me.zhengjie.modules.ptt.service;

import me.zhengjie.modules.ptt.domain.Term;
import me.zhengjie.modules.ptt.service.dto.TermCriteria;
import me.zhengjie.modules.ptt.service.dto.TermDto;
import me.zhengjie.modules.ptt.service.dto.TermReserveDto;
import me.zhengjie.modules.ptt.service.dto.TermReserveTimeDto;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.service.dto.HisCkItemDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 套餐服务
 *
 * @author yanjun
 * @date 2020-11-28 11:08
 */
public interface TermService {

    /**
     * 查询预约时段
     *
     * @param criteria .
     * @return .
     */
    Map<String, List<TermReserveTimeDto>> queryReserveTime(TermCriteria criteria);

    /**
     * 查询预约列表
     *
     * @param criteria .
     * @return .
     */
    List<TermReserveDto> queryReserveList(TermCriteria criteria);

    /**
     * 新增或者更新数据
     *
     * @param ckItemList .
     * @return .
     */
    Map<String, Term> createOrUpdate(Dept userDept, List<HisCkItemDto> ckItemList);

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(TermCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<TermDto>
     */
    List<TermDto> queryAll(TermCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return TermDto
     */
    TermDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return TermDto
     */
    TermDto create(Term resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(Term resources);

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
    void download(List<TermDto> all, HttpServletResponse response) throws IOException;

}
