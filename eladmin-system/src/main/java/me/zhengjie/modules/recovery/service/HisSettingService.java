/**
 * eladmin
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author yanjun
 * @date 2020-10-22 17:25
 */
package me.zhengjie.modules.recovery.service;

import me.zhengjie.modules.recovery.domain.HisSetting;
import me.zhengjie.modules.recovery.service.dto.HisSettingCriteria;
import me.zhengjie.modules.recovery.service.dto.HisSettingDto;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author yanjun
 * @date 2020-10-22 17:25
 */

public interface HisSettingService {

    HisSettingDto findById(long id);

    HisSettingDto findByName(String name);

    void create(HisSetting resources);

    void update(HisSetting resources);

    void delete(Set<Long> ids);

    Object queryAll(HisSettingCriteria criteria, Pageable pageable);

    List<HisSettingDto> queryAll(HisSettingCriteria criteria);

    void download(List<HisSettingDto> queryAll, HttpServletResponse response) throws IOException;

}
