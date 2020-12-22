package me.zhengjie.modules.ptt.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.ptt.domain.PatientFollow;
import me.zhengjie.modules.ptt.service.dto.PatientFollowDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 患者跟进
 *
 * @author yanjun
 * @date 2020-11-28 11:16
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientFollowMapper extends BaseMapper<PatientFollowDto, PatientFollow> {

}
