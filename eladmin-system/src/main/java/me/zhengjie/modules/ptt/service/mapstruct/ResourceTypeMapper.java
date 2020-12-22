package me.zhengjie.modules.ptt.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.ptt.domain.ResourceType;
import me.zhengjie.modules.ptt.service.dto.ResourceTypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 资源类型
 *
 * @author yanjun
 * @date 2020-11-28 11:16
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResourceTypeMapper extends BaseMapper<ResourceTypeDto, ResourceType> {

}
