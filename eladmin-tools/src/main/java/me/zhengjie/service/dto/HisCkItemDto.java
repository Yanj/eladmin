/**
 * eladmin
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author yanjun
 * @date 2020-11-13 15:57
 */
package me.zhengjie.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 患者信息
 *
 * @author yanjun
 * @date 2020-11-13 15:57
 */
@Data
public class HisCkItemDto implements Serializable {

    // 患者ID
    private Long patientId;

    // 患者姓名
    private String name;

    // 患者电话
    private String mobilePhone;

    // 患者病案号
    private Long mrn;

    // 就诊科室
    private String visitDept;

    // 就诊日期
    private Date visitDate;

    // 项目编码
    private String itemCode;

    // 项目名称
    private String itemName;

    // 单价
    private Long price;

    // 数量
    private Long amount;

    // 单位
    private String unit;

    // 应收金额
    private Long costs;

    // 实收金额
    private Long actualCosts;

    // 项目购买ID，不重复，用来判断数据是否已同步
    private Long patItemId;

    // 就诊ID，每次就诊生成一个唯一ID
    private Long visitId;

}
