package me.zhengjie.modules.ptt.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 患者套餐日志
 *
 * @author yanjun
 * @date 2020-11-28 10:48
 */
@Entity
@Table(name = "ptt_patient_term_log")
@Getter
@Setter
public class PatientTermLog extends BaseEntity implements Serializable {

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
    @JoinColumn(name = "patient_term_reserve_id")
    @ApiModelProperty(name = "患者套餐预约")
    private PatientTermReserve patientTermReserve;

    @Column(name = "before_times")
    @ApiModelProperty(name = "变更前次数")
    private Integer beforeTimes;

    @Column(name = "after_times")
    @ApiModelProperty(name = "变更后次数")
    private Integer afterTimes;

    @Column(name = "content")
    @ApiModelProperty(name = "变更内容")
    private String content;

    @Column(name = "status")
    @ApiModelProperty(name = "状态")
    private String status;

    @Column(name = "remark")
    @ApiModelProperty(name = "备注")
    private String remark;

    public void copy(PatientTermLog source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
