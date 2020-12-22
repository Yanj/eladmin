package me.zhengjie.modules.ptt.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.ptt.domain.Term;
import me.zhengjie.modules.ptt.service.dto.TermDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * 套餐
 *
 * @author yanjun
 * @date 2020-11-28 11:16
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TermMapper extends BaseMapper<TermDto, Term> {

}
