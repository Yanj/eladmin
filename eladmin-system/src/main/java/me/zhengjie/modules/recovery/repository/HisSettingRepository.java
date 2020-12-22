/**
 * eladmin
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author yanjun
 * @date 2020-10-22 16:56
 */
package me.zhengjie.modules.recovery.repository;

import me.zhengjie.modules.recovery.domain.HisSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

/**
 * @author yanjun
 * @date 2020-10-22 16:56
 */
public interface HisSettingRepository extends JpaRepository<HisSetting, Long>, JpaSpecificationExecutor<HisSetting> {

    HisSetting findByEnabled(Boolean enabled);

    HisSetting findByName(String name);

    void deleteAllByIdIn(Set<Long> ids);

}
