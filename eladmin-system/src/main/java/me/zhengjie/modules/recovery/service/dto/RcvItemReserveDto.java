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
import me.zhengjie.modules.system.service.dto.DeptDto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author yanjun
 * @website https://el-admin.vip
 * @description /
 * @date 2020-10-25
 **/
@Data
public class RcvItemReserveDto extends BaseDTO implements Serializable {

    /** id */
    /**
     * 防止精度丢失
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 机构
     */
    private DeptDto org;

    /**
     * 部门
     */
    private DeptDto dept;

    /**
     * 套餐
     */
    private RcvItemDto item;

    /**
     * 资源
     */
    private RcvResourceDto resource;

    /**
     * 唯一编码
     */
    private String code;

    /**
     * 预约日期
     */
    private Timestamp date;

    /**
     * 开始时间
     */
    private Timestamp beginTime;

    /**
     * 结束时间
     */
    private Timestamp endTime;

    /**
     * 时长
     */
    private Integer timeAmount;

    /**
     * 实际开始时间
     */
    private Timestamp actualBeginTime;

    /**
     * 实际结束时间
     */
    private Timestamp actualEndTime;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;
}