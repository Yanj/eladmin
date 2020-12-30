package me.zhengjie.modules.yy.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.yy.domain.WorkTime;
import me.zhengjie.modules.yy.service.dto.WorkTimeDto;
import me.zhengjie.modules.yy.service.dto.WorkTimeSmallDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author yanjun
 * @date 2020-12-24 14:35
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WorkTimeSmallMapper extends BaseMapper<WorkTimeSmallDto, WorkTime> {

}
