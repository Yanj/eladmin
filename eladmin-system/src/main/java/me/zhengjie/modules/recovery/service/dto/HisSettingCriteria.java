/**
 * eladmin
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author yanjun
 * @date 2020-10-23 10:11
 */
package me.zhengjie.modules.recovery.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author yanjun
 * @date 2020-10-23 10:11
 */
@Data
public class HisSettingCriteria implements Serializable {

    @Query
    private Long id;

    @Query(blurry = "name,address")
    private String blurry;

    @Query
    private Boolean enabled;

    @Query(type = Query.Type.BETWEEN)
    private List<Timestamp> createTime;

}
