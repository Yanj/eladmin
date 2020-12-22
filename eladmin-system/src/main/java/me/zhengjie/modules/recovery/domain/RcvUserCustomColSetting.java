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
import me.zhengjie.modules.system.domain.Dict;

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
@Table(name = "rcv_user_custom_col_setting")
public class RcvUserCustomColSetting extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @JoinColumn(name = "org_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @ApiModelProperty(value = "机构id")
    private RcvOrg org;

    @JoinColumn(name = "dept_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @ApiModelProperty(value = "部门id")
    private RcvDept dept;

    @Column(name = "name")
    @ApiModelProperty(value = "列名称")
    private String name;

    @Column(name = "description")
    @ApiModelProperty(value = "列描述")
    private String description;

    private String type;

    @JoinColumn(name = "dict_id")
    @ManyToOne()
    @ApiModelProperty(value = "字典")
    private Dict dict;

    @Column(name = "status")
    @ApiModelProperty(value = "状态")
    private String status;

    @Column(name = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    public void copy(RcvUserCustomColSetting source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}