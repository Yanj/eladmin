package me.zhengjie.modules.ptt.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.ptt.domain.PatientTermReserveLog;
import me.zhengjie.modules.ptt.service.dto.PatientTermReserveLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 患者套餐预约日志
 *
 * @author yanjun
 * @date 2020-11-28 11:16
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientTermReserveLogMapper extends BaseMapper<PatientTermReserveLogDto, PatientTermReserveLog> {

}
