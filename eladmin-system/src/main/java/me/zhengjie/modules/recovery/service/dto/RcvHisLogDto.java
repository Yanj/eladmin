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
public class RcvHisLogDto extends BaseDTO implements Serializable {

    /** 项目购买ID */
    /**
     * 防止精度丢失
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long patItemId;

    /**
     * 就诊ID
     */
    private Long visitId;

    /**
     * 患者ID
     */
    private Long patientId;

    /**
     * 患者姓名
     */
    private String name;

    /**
     * 患者电话
     */
    private String mobilePhone;

    /**
     * 患者档案编号
     */
    private String mrn;

    /**
     * 就诊科室
     */
    private String visitDept;

    /**
     * 就诊日期
     */
    private Timestamp visitDate;

    /**
     * 项目编码
     */
    private String itemCode;

    /**
     * 项目名称
     */
    private String itemName;

    /**
     * 单价
     */
    private Long price;

    /**
     * 数量
     */
    private Long amount;

    /**
     * 单位
     */
    private String unit;

    /**
     * 应收金额
     */
    private Long costs;

    /**
     * 实收金额
     */
    private Long actualCosts;

    /**
     * 备注
     */
    private String remark;
}