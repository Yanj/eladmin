package me.zhengjie.modules.ptt.service;

import me.zhengjie.modules.ptt.domain.HisLog;
import me.zhengjie.modules.ptt.service.dto.HisLogCriteria;
import me.zhengjie.modules.ptt.service.dto.HisLogDto;
import me.zhengjie.service.dto.HisCkItemDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * HIS查询日志服务
 *
 * @author yanjun
 * @date 2020-11-28 11:08
 */
public interface HisLogService {

    /**
     * 新增或者更新数据
     *
     * @param ckItemList .
     */
    List<HisLogDto> createOrUpdate(List<HisCkItemDto> ckItemList);

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
     * @return List<CusDto>
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
