package me.zhengjie.modules.yy.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import me.zhengjie.utils.enums.YesNoEnum;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author yanjun
 * @date 2020-12-24 13:56
 */
@Entity
@Table(name = "yy_patient_term")
@Getter
@Setter
public class PatientTerm extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID")
    private Long id;

    @Column(name = "org_id")
    @ApiModelProperty(value = "组织ID")
    private Long orgId;

    @Column(name = "com_id")
    @ApiModelProperty(value = "公司ID")
    private Long comId;

    @Column(name = "dept_id")
    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @Column(name = "pat_item_id")
    @ApiModelProperty(value = "外部系统ID")
    private String patItemId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @ApiModelProperty(value = "类型")
    private PatientTermType type;

    @Column(name = "pid")
    @ApiModelProperty(value = "赠setPatItemId送套餐ID")
    private Long pid;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @ApiModelProperty(value = "患者")
    private Patient patient;

    @Column(name = "term_id")
    @ApiModelProperty(value = "套餐ID")
    private Long termId;

    @Column(name = "term_code")
    @ApiModelProperty(value = "套餐外部系统ID")
    private String termCode;

    @Column(name = "term_name")
    @ApiModelProperty(value = "套餐名称")
    private String termName;

    @Column(name = "term_price")
    @ApiModelProperty(value = "套餐价格")
    private Long termPrice;

    @Column(name = "term_original_price")
    @ApiModelProperty(value = "套餐原价")
    private Long termOriginalPrice;

    @Column(name = "term_times")
    @ApiModelProperty(value = "套餐次数")
    private Integer termTimes;

    @Column(name = "term_unit")
    @ApiModelProperty(value = "套餐单位")
    private String termUnit;

    @Column(name = "term_duration")
    @ApiModelProperty(value = "套餐时长")
    private Long termDuration;

    @Column(name = "term_operator_count")
    @ApiModelProperty(value = "套餐操作员数量")
    private Long termOperatorCount;

    @Column(name = "price")
    @ApiModelProperty(value = "购买价格")
    private Long price;

    @Column(name = "total_times")
    @ApiModelProperty(value = "总次数")
    private Integer totalTimes;

    @Column(name = "times")
    @ApiModelProperty(value = "剩余次数")
    private Integer times;

    @Column(name = "free_times")
    @ApiModelProperty(value = "已赠送次数")
    private Integer freeTimes;

    @Column(name = "duration")
    @ApiModelProperty(value = "时长")
    private Long duration;

    @Column(name = "operator_count")
    @ApiModelProperty(value = "操作员数量")
    private Long operatorCount;

    @Enumerated
    @Column(name = "status")
    @ApiModelProperty(value = "状态")
    private YesNoEnum status;

    @Column(name = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    public void copy(PatientTerm source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
