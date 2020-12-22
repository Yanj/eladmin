package me.zhengjie.modules.ptt.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.ptt.domain.PatientTermReserveResource;
import me.zhengjie.modules.ptt.service.dto.PatientTermReserveResourceDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 患者套餐预约资源
 *
 * @author yanjun
 * @date 2020-11-28 11:16
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientTermReserveResourceMapper extends BaseMapper<PatientTermReserveResourceDto, PatientTermReserveResource> {

}
