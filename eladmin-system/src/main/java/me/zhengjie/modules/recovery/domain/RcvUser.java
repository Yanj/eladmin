/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.recovery.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.base.BaseEntity;
import me.zhengjie.modules.system.domain.Dept;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
 * @author yanjun
 * @website https://el-admin.vip
 * @description /
 * @date 2020-10-25
 **/
@Entity
@Data
@Table(name = "rcv_user")
public class RcvUser extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "name")
    @ApiModelProperty(value = "姓名")
    private String name;

    @Column(name = "cert_no")
    @ApiModelProperty(value = "身份证")
    private String certNo;

    @Column(name = "phone")
    @ApiModelProperty(value = "电话")
    private String phone;

    @Column(name = "birthday")
    @ApiModelProperty(value = "生日")
    private Timestamp birthday;

    @Column(name = "profession")
    @ApiModelProperty(value = "职业")
    private String profession;

    @Column(name = "address")
    @ApiModelProperty(value = "地址")
    private String address;

    @Column(name = "contact_name")
    @ApiModelProperty(value = "联系人")
    private String contactName;

    @Column(name = "contact_phone")
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;

    @Column(name = "contact_relation")
    @ApiModelProperty(value = "联系人关系")
    private String contactRelation;

    @Column(name = "status")
    @ApiModelProperty(value = "状态")
    private String status;

    @Column(name = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    @Column(name = "col1")
    @ApiModelProperty(value = "col1")
    private String col1;

    @Column(name = "col2")
    @ApiModelProperty(value = "col2")
    private String col2;

    @Column(name = "col3")
    @ApiModelProperty(value = "col3")
    private String col3;

    @Column(name = "col4")
    @ApiModelProperty(value = "col4")
    private String col4;

    @Column(name = "col5")
    @ApiModelProperty(value = "col5")
    private String col5;

    @ManyToMany
    @JoinTable(
            name = "rcv_user_depts",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "dept_id", referencedColumnName = "dept_id")}
    )
    @ApiModelProperty(value = "部门", hidden = true)
    private Set<Dept> depts;

    public void copy(RcvUser source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}