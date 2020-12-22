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
@Table(name = "rcv_item_resource_type")
public class RcvItemResourceType extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @ApiModelProperty(value = "套餐")
    private RcvItem item;

//    @Column(name = "item_id")
//    @ApiModelProperty(value = "套餐ID")
//    private Long itemId;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    @ApiModelProperty(value = "部门")
    private Dept dept;

//    @Column(name = "dept_id")
//    @ApiModelProperty(value = "部门ID")
//    private Long deptId;

    @ManyToOne
    @JoinColumn(name = "resource_type_id")
    @ApiModelProperty(value = "资源类型")
    private RcvResourceType resourceType;

//    @Column(name = "resouce_type_id")
//    @ApiModelProperty(value = "资源类型ID")
//    private Long resourceTypeId;

    @Column(name = "status")
    @ApiModelProperty(value = "状态")
    private String status;

    @Column(name = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    public void copy(RcvItemResourceType source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}