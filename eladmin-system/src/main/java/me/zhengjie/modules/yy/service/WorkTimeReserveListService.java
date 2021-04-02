package me.zhengjie.modules.yy.service;

import me.zhengjie.modules.yy.service.dto.ResourceGroupWorkTimeReserveListDto;
import me.zhengjie.modules.yy.service.dto.WorkTimeDto;
import me.zhengjie.modules.yy.service.dto.WorkTimeReserveListCriteria;
import me.zhengjie.modules.yy.service.dto.WorkTimeSmallDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author yanjun
 * @date 2021-04-01 10:32
 */
public interface WorkTimeReserveListService {

    /**
     * 导出数据
     *
     * @param all      待导出的数据
     * @param response /
     * @throws IOException /
     */
    void download(List<WorkTimeSmallDto> workTimeList, List<ResourceGroupWorkTimeReserveListDto> all, boolean showStatus, HttpServletResponse response) throws IOException;

    /**
     * 查询工作时段列表
     *
     * @param criteria .
     * @return .
     */
    List<WorkTimeSmallDto> queryWorkTimeList(WorkTimeReserveListCriteria criteria);

    /**
     * 查询工作时段预约列表
     *
     * @param criteria .
     * @return .
     */
    List<ResourceGroupWorkTimeReserveListDto> queryWorkTimeReserveList(WorkTimeReserveListCriteria criteria);

}
