package me.zhengjie.modules.ptt.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.ptt.domain.TermResourceType;
import me.zhengjie.modules.ptt.service.dto.TermResourceTypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 套餐资源类型
 *
 * @author yanjun
 * @date 2020-11-28 11:16
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TermResourceTypeMapper extends BaseMapper<TermResourceTypeDto, TermResourceType> {

}
