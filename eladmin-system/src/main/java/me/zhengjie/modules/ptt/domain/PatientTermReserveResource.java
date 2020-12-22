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
 * 患者套餐预约资源
 *
 * @author yanjun
 * @date 2020-11-28 10:57
 */
@Entity
@Table(name = "ptt_patient_term_reserve_resource")
@Getter
@Setter
public class PatientTermReserveResource extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_term_reserve_id")
    @ApiModelProperty(name = "患者套餐预约")
    private PatientTermReserve patientTermReserve;

    @OneToOne
    @JoinColumn(name = "resource_id")
    @ApiModelProperty(name = "资源")
    private Resource resource;

    @Column(name = "status")
    @ApiModelProperty(name = "状态")
    private String status;

    @Column(name = "remark")
    @ApiModelProperty(name = "备注")
    private String remark;

    public void copy(PatientTermReserveResource source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
