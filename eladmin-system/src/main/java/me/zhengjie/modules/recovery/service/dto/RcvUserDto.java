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
public class RcvUserDto extends BaseDTO implements Serializable {

    /** id */
    /**
     * 防止精度丢失
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private Long patientId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证
     */
    private String certNo;

    /**
     * 电话
     */
    private String phone;

    /**
     * 生日
     */
    private Timestamp birthday;

    /**
     * 职业
     */
    private String profession;

    /**
     * 地址
     */
    private String address;

    /**
     * 联系人
     */
    private String contactName;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系人关系
     */
    private String contactRelation;

    /**
     * 状态
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * col1
     */
    private String col1;

    /**
     * col2
     */
    private String col2;

    /**
     * col3
     */
    private String col3;

    /**
     * col4
     */
    private String col4;

    /**
     * col5
     */
    private String col5;

}