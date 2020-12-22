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
package me.zhengjie.modules.recovery.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author yanjun
 * @website https://el-admin.vip
 * @description /
 * @date 2020-10-25
 **/
@Entity
@Data
@Table(name = "rcv_user_item")
public class RcvUserItem extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "user_id")
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @Column(name = "item_id")
    @ApiModelProperty(value = "套餐ID")
    private Long itemId;

    @Column(name = "item_code")
    @ApiModelProperty(value = "套餐编码")
    private String itemCode;

    @Column(name = "item_name")
    @ApiModelProperty(value = "套餐名称")
    private String itemName;

    @Column(name = "item_price")
    @ApiModelProperty(value = "套餐单价")
    private Long itemPrice;

    @Column(name = "item_times")
    @ApiModelProperty(value = "套餐次数")
    private Integer itemTimes;

    @Column(name = "item_unit")
    @ApiModelProperty(value = "套餐单位")
    private String itemUnit;

    @Column(name = "item_amount")
    @ApiModelProperty(value = "套餐总价")
    private Long itemAmount;

    @Column(name = "amount")
    @ApiModelProperty(value = "实际支付")
    private Long amount;

    @Column(name = "times")
    @ApiModelProperty(value = "剩余次数")
    private Integer times;

    @Column(name = "org_id")
    @ApiModelProperty(value = "上次预约机构")
    private Long orgId;

    @Column(name = "dept_id")
    @ApiModelProperty(value = "上次预约部门")
    private Long deptId;

    @Column(name = "reserve_date")
    @ApiModelProperty(value = "上次预约日期")
    private Timestamp reserveDate;

    @Column(name = "org_change_count")
    @ApiModelProperty(value = "机构变更次数")
    private Integer orgChangeCount;

    @Column(name = "status")
    @ApiModelProperty(value = "状态")
    private String status;

    @Column(name = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    public void copy(RcvUserItem source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}