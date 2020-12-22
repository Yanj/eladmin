/**
 * eladmin
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author yanjun
 * @date 2020-10-22 17:42
 */
package me.zhengjie.modules.recovery.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.recovery.domain.HisSetting;
import me.zhengjie.modules.recovery.service.dto.HisSettingDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * @author yanjun
 * @date 2020-10-22 17:42
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HisSettingMapper extends BaseMapper<HisSettingDto, HisSetting> {

}
