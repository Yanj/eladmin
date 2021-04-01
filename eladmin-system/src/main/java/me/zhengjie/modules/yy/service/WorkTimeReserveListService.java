package me.zhengjie.modules.yy.service;

import me.zhengjie.modules.yy.service.dto.ResourceGroupWorkTimeReserveListDto;
import me.zhengjie.modules.yy.service.dto.WorkTimeReserveListCriteria;
import me.zhengjie.modules.yy.service.dto.WorkTimeSmallDto;

import java.util.List;

/**
 * @author yanjun
 * @date 2021-04-01 10:32
 */
public interface WorkTimeReserveListService {

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
