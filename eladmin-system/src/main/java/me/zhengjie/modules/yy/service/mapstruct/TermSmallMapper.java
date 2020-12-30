package me.zhengjie.modules.yy.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.yy.domain.Term;
import me.zhengjie.modules.yy.service.dto.TermDto;
import me.zhengjie.modules.yy.service.dto.TermSmallDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author yanjun
 * @date 2020-12-24 14:35
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TermSmallMapper extends BaseMapper<TermSmallDto, Term> {

}
