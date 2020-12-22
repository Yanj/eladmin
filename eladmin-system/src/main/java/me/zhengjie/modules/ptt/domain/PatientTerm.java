package me.zhengjie.modules.ptt.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import me.zhengjie.modules.system.domain.Dept;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 患者套餐
 *
 * @author yanjun
 * @date 2020-11-28 10:45
 */
@Entity
@Table(name = "ptt_patient_term")
@Getter
@Setter
public class PatientTerm extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @ApiModelProperty(value = "患者信息")
    private Patient patient;

    @OneToOne
    @JoinColumn(name = "term_id")
    @ApiModelProperty(value = "套餐信息")
    private Term term;

    @Column(name = "term_code")
    @ApiModelProperty(value = "套餐编码")
    private String termCode;

    @Column(name = "term_name")
    @ApiModelProperty(value = "套餐名称")
    private String termName;

    @Column(name = "term_description")
    @ApiModelProperty(value = "套餐描述")
    private String termDescription;

    @Column(name = "term_duration")
    @ApiModelProperty(value = "套餐时长")
    private Long termDuration;

    @Column(name = "term_times")
    @ApiModelProperty(value = "套餐次数")
    private Integer termTimes;

    @Column(name = "term_unit")
    @ApiModelProperty(value = "套餐单位")
    private String termUnit;

    @Column(name = "term_price")
    @ApiModelProperty(value = "套餐单价")
    private Long termPrice;

    @Column(name = "term_amount")
    @ApiModelProperty(value = "套餐总价")
    private Long termAmount;

    @Column(name = "times")
    @ApiModelProperty(value = "剩余次数")
    private Integer times;

    @Column(name = "amount")
    @ApiModelProperty(value = "实际支付金额")
    private Long amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_dept_id")
    @ApiModelProperty(name = "上一次预约医院")
    private Dept lastDept;

    @Column(name = "status")
    @ApiModelProperty(value = "状态")
    private String status;

    @Column(name = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    public void copy(PatientTerm source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
