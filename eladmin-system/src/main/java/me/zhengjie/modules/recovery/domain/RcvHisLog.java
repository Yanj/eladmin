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
import me.zhengjie.service.dto.HisCkItemDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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
@Table(name = "rcv_his_log")
public class RcvHisLog extends BaseEntity implements Serializable {

    public static RcvHisLog create(HisCkItemDto ckItem) {
        RcvHisLog log = new RcvHisLog();
        log.setPatientId(ckItem.getPatientId());
        log.setName(ckItem.getName());
        log.setMobilePhone(ckItem.getMobilePhone());
        if (null != ckItem.getMrn()) {
            log.setMrn(ckItem.getMrn().toString());
        }
        log.setVisitDept(ckItem.getVisitDept());
        if (null != ckItem.getVisitDate()) {
            log.setVisitDate(new Timestamp(ckItem.getVisitDate().getTime()));
        }
        log.setItemCode(ckItem.getItemCode());
        log.setItemName(ckItem.getItemName());
        log.setPrice(ckItem.getPrice());
        log.setAmount(ckItem.getAmount());
        log.setUnit(ckItem.getUnit());
        log.setCosts(ckItem.getCosts());
        log.setActualCosts(ckItem.getActualCosts());
        log.setPatItemId(ckItem.getPatItemId());
        log.setVisitId(ckItem.getVisitId());
        return log;
    }

    @Id
    @Column(name = "pat_item_id")
    @ApiModelProperty(value = "项目购买ID")
    private Long patItemId;

    @Column(name = "visit_id")
    @ApiModelProperty(value = "就诊ID")
    private Long visitId;

    @Column(name = "patient_id")
    @ApiModelProperty(value = "患者ID")
    private Long patientId;

    @Column(name = "name")
    @ApiModelProperty(value = "患者姓名")
    private String name;

    @Column(name = "mobile_phone")
    @ApiModelProperty(value = "患者电话")
    private String mobilePhone;

    @Column(name = "mrn")
    @ApiModelProperty(value = "患者档案编号")
    private String mrn;

    @Column(name = "visit_dept")
    @ApiModelProperty(value = "就诊科室")
    private String visitDept;

    @Column(name = "visit_date", nullable = false)
    @NotNull
    @ApiModelProperty(value = "就诊日期")
    private Timestamp visitDate;

    @Column(name = "item_code")
    @ApiModelProperty(value = "项目编码")
    private String itemCode;

    @Column(name = "item_name")
    @ApiModelProperty(value = "项目名称")
    private String itemName;

    @Column(name = "price")
    @ApiModelProperty(value = "单价")
    private Long price;

    @Column(name = "amount")
    @ApiModelProperty(value = "数量")
    private Long amount;

    @Column(name = "unit")
    @ApiModelProperty(value = "单位")
    private String unit;

    @Column(name = "costs")
    @ApiModelProperty(value = "应收金额")
    private Long costs;

    @Column(name = "actual_costs")
    @ApiModelProperty(value = "实收金额")
    private Long actualCosts;

    @Column(name = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    public void copy(RcvHisLog source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}