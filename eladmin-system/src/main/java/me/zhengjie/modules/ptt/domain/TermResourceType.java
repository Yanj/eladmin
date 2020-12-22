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

/**
 * 套餐资源类型
 *
 * @author yanjun
 * @date 2020-11-28 10:15
 */
@Entity
@Table(name = "ptt_term_resource_type")
@Getter
@Setter
public class TermResourceType extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "term_id")
    @ApiModelProperty(value = "套餐")
    private Term term;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    @ApiModelProperty(value = "医院")
    private Dept dept;

    @ManyToOne
    @JoinColumn(name = "resource_type_id")
    @ApiModelProperty(value = "资源类型")
    private ResourceType resourceType;

    @Column(name = "nullable")
    @ApiModelProperty(name = "是否可以为空")
    private Boolean nullable;

    @Column(name = "status")
    @ApiModelProperty(name = "状态")
    private String status;

    @Column(name = "remark")
    @ApiModelProperty(name = "备注")
    private String remark;

    public void copy(TermResourceType source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
