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
 * 资源
 *
 * @author yanjun
 * @date 2020-11-28 10:10
 */
@Entity
@Table(name = "ptt_resource")
@Getter
@Setter
public class Resource extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resource_type_id")
    @ApiModelProperty(value = "资源类型")
    private ResourceType resourceType;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    @ApiModelProperty(value = "医院")
    private Dept dept;

    @Column(name = "code")
    @ApiModelProperty(name = "编码")
    private String code;

    @Column(name = "name")
    @ApiModelProperty(name = "名称")
    private String name;

    @Column(name = "count")
    @ApiModelProperty(name = "数量")
    private Long count;

    @Column(name = "status")
    @ApiModelProperty(name = "状态")
    private String status;

    @Column(name = "remark")
    @ApiModelProperty(name = "备注")
    private String remark;

    public void copy(Resource source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
