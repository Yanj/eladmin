package me.zhengjie.modules.yy.service;

import me.zhengjie.modules.yy.domain.HisLog;
import me.zhengjie.modules.yy.service.dto.HisLogCriteria;
import me.zhengjie.modules.yy.service.dto.HisLogDto;
import me.zhengjie.service.dto.HisCkItemDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author yanjun
 * @date 2020-12-24 14:32
 */
public interface HisLogService {

    /**
     * 保存HIS查询日志
     *
     * @param list .
     * @return .
     */
    List<HisLogDto> create(List<HisCkItemDto> list);

    /**
     * 查询数据分页
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return Map<String, Object>
     */
    Map<String, Object> queryAll(HisLogCriteria criteria, Pageable pageable);

    /**
     * 查询所有数据不分页
     *
     * @param criteria 条件参数
     * @return List<HisLogDto>
     */
    List<HisLogDto> queryAll(HisLogCriteria criteria);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return HisLogDto
     */
    HisLogDto findById(Long id);

    /**
     * 创建
     *
     * @param resources /
     * @return HisLogDto
     */
    HisLogDto create(HisLog resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void update(HisLog resources);

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
    void download(List<HisLogDto> all, HttpServletResponse response) throws IOException;

}
