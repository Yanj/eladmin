package me.zhengjie.modules.ptt.service;

import me.zhengjie.modules.ptt.domain.Cus;
import me.zhengjie.modules.ptt.service.dto.CusCriteria;
import me.zhengjie.modules.ptt.service.dto.CusDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 自定义服务
 *
 * @author yanjun
 * @date 2020-11-28 11:08
 */
public interface CusService {

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(CusCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<CusDto>
     */
    List<CusDto> queryAll(CusCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return CusDto
     */
    CusDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return CusDto
     */
    CusDto create(Cus resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(Cus resources);

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
    void download(List<CusDto> all, HttpServletResponse response) throws IOException;

}
