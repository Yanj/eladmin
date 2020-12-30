package me.zhengjie.modules.yy.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.yy.domain.PatientTermLog;
import me.zhengjie.modules.yy.domain.ReserveLog;
import me.zhengjie.modules.yy.service.dto.PatientTermLogDto;
import me.zhengjie.modules.yy.service.dto.ReserveLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author yanjun
 * @date 2020-12-24 14:35
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientTermLogMapper extends BaseMapper<PatientTermLogDto, PatientTermLog> {

}
