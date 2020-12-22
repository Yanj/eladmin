package me.zhengjie.modules.ptt.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import me.zhengjie.modules.system.domain.Dept;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 患者部门
 *
 * @author yanjun
 * @date 2020-11-28 10:22
 */
@Entity
@Table(name = "ptt_patient_dept")
@Getter
@Setter
@IdClass(PatientDept.PK.class)
public class PatientDept implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "dept_id")
    @ApiModelProperty(value = "医院")
    private Dept dept;

    @Id
    @ManyToOne
    @JoinColumn(name = "patient_id")
        @ApiModelProperty(value = "患者")
    private Patient patient;

    public void copy(PatientDept source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PK implements Serializable {
        private Dept dept;
        private Patient patient;
    }

}
