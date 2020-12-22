package me.zhengjie.modules.ptt.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.ptt.domain.PatientDept;
import me.zhengjie.modules.ptt.service.dto.PatientDeptDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 患者医院
 *
 * @author yanjun
 * @date 2020-11-28 11:16
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientDeptMapper extends BaseMapper<PatientDeptDto, PatientDept> {

}
