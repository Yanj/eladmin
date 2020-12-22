package me.zhengjie.modules.ptt.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.ptt.domain.PatientTermLog;
import me.zhengjie.modules.ptt.service.dto.PatientTermLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 患者套餐日志
 *
 * @author yanjun
 * @date 2020-11-28 11:16
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientTermLogMapper extends BaseMapper<PatientTermLogDto, PatientTermLog> {

}
