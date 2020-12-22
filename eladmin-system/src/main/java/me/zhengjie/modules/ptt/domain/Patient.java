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
import java.util.Set;

/**
 * 患者信息
 *
 * @author yanjun
 * @date 2020-11-28 10:04
 */
@Entity
@Table(name = "ptt_patient")
@Getter
@Setter
public class Patient extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "patient_id")
    @ApiModelProperty(name = "患者 id")
    private Long patientId;

    @Column(name = "name")
    @ApiModelProperty(name = "姓名")
    private String name;

    @Column(name = "mrn")
    @ApiModelProperty(name = "档案编号")
    private String mrn;

    @Column(name = "cert_no")
    @ApiModelProperty(name = "身份证")
    private String certNo;

    @Column(name = "phone")
    @ApiModelProperty(name = "电话")
    private String phone;

    @Column(name = "birthday")
    @ApiModelProperty(name = "生日")
    private String birthday;

    @Column(name = "profession")
    @ApiModelProperty(name = "职业")
    private String profession;

    @Column(name = "address")
    @ApiModelProperty(name = "地址")
    private String address;

    @Column(name = "contact_name")
    @ApiModelProperty(name = "联系人")
    private String contactName;

    @Column(name = "contact_phone")
    @ApiModelProperty(name = "联系人电话")
    private String contactPhone;

    @Column(name = "contact_relation")
    @ApiModelProperty(name = "联系人关系")
    private String contactRelation;

    @Column(name = "status")
    @ApiModelProperty(name = "状态")
    private String status;

    @Column(name = "remark")
    @ApiModelProperty(name = "备注")
    private String remark;

    @ManyToMany
    @ApiModelProperty(name = "医院")
    @JoinTable(
            name = "ptt_patient_dept",
            joinColumns = {@JoinColumn(name = "patient_id")},
            inverseJoinColumns = {@JoinColumn(name = "dept_id")}
    )
    private Set<Dept> depts;

    public void copy(Patient source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
