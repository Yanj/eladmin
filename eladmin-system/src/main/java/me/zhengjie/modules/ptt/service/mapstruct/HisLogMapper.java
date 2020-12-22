package me.zhengjie.modules.ptt.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.ptt.domain.HisLog;
import me.zhengjie.modules.ptt.service.dto.HisLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * His 查询日志信息
 *
 * @author yanjun
 * @date 2020-11-28 11:16
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HisLogMapper extends BaseMapper<HisLogDto, HisLog> {

}
