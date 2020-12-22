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
package me.zhengjie.modules.ptt.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * his 查询日志
 *
 * @author yanjun
 * @date 2020-11-28 11:00
 **/
@Getter
@Setter
public class HisLogDto extends BaseDTO implements Serializable {

    private Long patItemId;

    private Long visitId;

    private Long patientId;

    private String name;

    private String mobilePhone;

    private String mrn;

    private String visitDept;

    private Timestamp visitDate;

    private String itemCode;

    private String itemName;

    private Long price;

    private Long amount;

    private String unit;

    private Long costs;

    private Long actualCosts;

    private String remark;

}