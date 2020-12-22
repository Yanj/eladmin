/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.recovery.service.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import me.zhengjie.base.BaseDTO;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author yanjun
 * @website https://el-admin.vip
 * @description /
 * @date 2020-10-25
 **/
@Data
public class RcvUserItemDto extends BaseDTO implements Serializable {

    /** id */
    /**
     * 防止精度丢失
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 套餐ID
     */
    private Long itemId;

    /**
     * 套餐编码
     */
    private String itemCode;

    /**
     * 套餐名称
     */
    private String itemName;

    /**
     * 套餐单价
     */
    private Long itemPrice;

    /**
     * 套餐次数
     */
    private Integer itemTimes;

    /**
     * 套餐单位
     */
    private String itemUnit;

    /**
     * 套餐总价
     */
    private Long itemAmount;

    /**
     * 实际支付
     */
    private Long amount;

    /**
     * 剩余次数
     */
    private Integer times;

    /**
     * 上次预约机构
     */
    private Long orgId;

    /**
     * 上次预约部门
     */
    private Long deptId;

    /**
     * 上次预约日期
     */
    private Timestamp reserveDate;

    /**
     * 机构变更次数
     */
    private Integer orgChangeCount;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}