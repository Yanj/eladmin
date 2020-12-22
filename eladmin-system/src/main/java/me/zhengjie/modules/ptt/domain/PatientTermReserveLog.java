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
 * 患者套餐预约日志
 *
 * @author yanjun
 * @date 2020-11-28 10:55
 */
@Entity
@Table(name = "ptt_patient_term_reserve_log")
@Getter
@Setter
public class PatientTermReserveLog extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_term_reserve_id")
    @ApiModelProperty(name = "患者套餐预约")
    private PatientTermReserve patientTermReserve;

    @Column(name = "before_status")
    @ApiModelProperty(name = "变更前状态")
    private String beforeStatus;

    @Column(name = "after_status")
    @ApiModelProperty(name = "变更后状态")
    private String afterStatus;

    @Column(name = "content")
    @ApiModelProperty(name = "变更内容")
    private String content;

    @Column(name = "status")
    @ApiModelProperty(name = "状态")
    private String status;

    @Column(name = "remark")
    @ApiModelProperty(name = "备注")
    private String remark;

    public void copy(PatientTermReserveLog source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
