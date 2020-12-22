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
 * 套餐
 *
 * @author yanjun
 * @date 2020-11-28 10:13
 */
@Entity
@Table(name = "ptt_term")
@Getter
@Setter
public class Term extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id")
    private Long id;

    @Column(name = "code")
    @ApiModelProperty(name = "编码")
    private String code;

    @Column(name = "name")
    @ApiModelProperty(name = "名称")
    private String name;

    @Column(name = "description")
    @ApiModelProperty(name = "描述")
    private String description;

    @Column(name = "duration")
    @ApiModelProperty(name = "时长")
    private Long duration;

    @Column(name = "times")
    @ApiModelProperty(name = "次数")
    private Integer times;

    @Column(name = "unit")
    @ApiModelProperty(name = "单位")
    private String unit;

    @Column(name = "price")
    @ApiModelProperty(name = "单价")
    private Long price;

    @Column(name = "amount")
    @ApiModelProperty(name = "总价")
    private Long amount;

    @Column(name = "status")
    @ApiModelProperty(name = "状态")
    private String status;

    @Column(name = "remark")
    @ApiModelProperty(name = "备注")
    private String remark;

    public void copy(Term source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
