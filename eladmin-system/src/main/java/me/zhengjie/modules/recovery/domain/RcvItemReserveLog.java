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
@Table(name = "rcv_item_reserve_log")
public class RcvItemReserveLog extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "reserve_id")
    @ApiModelProperty(value = "预约")
    private RcvItemReserve reserve;

    @Column(name = "content")
    @ApiModelProperty(value = "变更内容")
    private String content;

    @Column(name = "before_status")
    @ApiModelProperty(value = "之前状态")
    private String beforeStatus;

    @Column(name = "after_status")
    @ApiModelProperty(value = "现在状态")
    private String afterStatus;

    @Column(name = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    public void copy(RcvItemReserveLog source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}