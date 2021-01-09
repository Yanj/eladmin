package me.zhengjie.modules.yy.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author yanjun
 * @date 2020-12-24 12:46
 */
@Entity
@Table(name = "yy_patient")
@Getter
@Setter
public class Patient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String mrn;

    private String name;

    private String phone;

    private String status;

    private String remark;

    @OneToMany
    @JoinColumn(name = "patient_id")
    private Set<PatientCol> cols;

    public void copy(Patient source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
