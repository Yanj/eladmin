package me.zhengjie.modules.yy.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.yy.domain.Hospital;
import me.zhengjie.modules.yy.service.dto.HospitalDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author yanjun
 * @date 2020-12-24 21:21
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HospitalMapper extends BaseMapper<HospitalDto, Hospital> {

}
