package me.zhengjie.modules.ptt.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.ptt.domain.Cus;
import me.zhengjie.modules.ptt.service.dto.CusDto;
import me.zhengjie.modules.ptt.service.dto.OrgDto;
import me.zhengjie.modules.system.domain.Dept;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 医院信息
 *
 * @author yanjun
 * @date 2020-11-28 11:16
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrgMapper extends BaseMapper<OrgDto, Dept> {

}
