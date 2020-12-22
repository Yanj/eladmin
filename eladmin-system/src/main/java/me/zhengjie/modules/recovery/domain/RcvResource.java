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

/**
 * @author yanjun
 * @website https://el-admin.vip
 * @description /
 * @date 2020-10-25
 **/
@Entity
@Data
@Table(name = "rcv_resource")
public class RcvResource extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "org_id")
    @ApiModelProperty(value = "机构")
    private Dept org;

    @OneToOne
    @JoinColumn(name = "dept_id")
    @ApiModelProperty(value = "部门")
    private Dept dept;

    @OneToOne
    @JoinColumn(name = "type_id")
    @ApiModelProperty(value = "类型")
    private RcvResourceType type;

    @Column(name = "code")
    @ApiModelProperty(value = "编码")
    private String code;

    @Column(name = "name")
    @ApiModelProperty(value = "名称")
    private String name;

    @Column(name = "status")
    @ApiModelProperty(value = "状态")
    private String status;

    @Column(name = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    public void copy(RcvResource source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}