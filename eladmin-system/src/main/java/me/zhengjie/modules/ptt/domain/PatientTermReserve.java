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
import java.sql.Timestamp;

/**
 * 患者套餐预约
 *
 * @author yanjun
 * @date 2020-11-28 10:51
 */
@Entity
@Table(name = "ptt_patient_term_reserve")
@Getter
@Setter
public class PatientTermReserve extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "term_id")
    @ApiModelProperty(name = "套餐")
    private Term term;

    @ManyToOne
    @JoinColumn(name = "patient_term_id")
    @ApiModelProperty(name = "患者套餐")
    private PatientTerm patientTerm;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    @ApiModelProperty(name = "医院")
    private Dept dept;

    @Column(name = "date")
    @ApiModelProperty(name = "预约日期")
    private String date;

    @Column(name = "begin_time")
    @ApiModelProperty(name = "开始时间")
    private String beginTime;

    @Column(name = "end_time")
    @ApiModelProperty(name = "结束时间")
    private String endTime;

    @Column(name = "actual_begin_time")
    @ApiModelProperty(name = "实际开始时间")
    private String actualBeginTime;

    @Column(name = "actual_end_time")
    @ApiModelProperty(name = "实际结束时间")
    private String actualEndTime;

    @Column(name = "status")
    @ApiModelProperty(name = "状态")
    private String status;

    @Column(name = "remark")
    @ApiModelProperty(name = "备注")
    private String remark;

    public void copy(PatientTermReserve source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
