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
 * 资源类型
 *
 * @author yanjun
 * @date 2020-11-28 10:08
 */
@Entity
@Table(name = "ptt_resource_type")
@Getter
@Setter
public class ResourceType extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    @ApiModelProperty(value = "医院")
    private Dept dept;

    @Column(name = "name")
    @ApiModelProperty(name = "名称")
    private String name;

    @Column(name = "max_count")
    @ApiModelProperty(name = "最大数量")
    private Long maxCount;

    @Column(name = "status")
    @ApiModelProperty(name = "状态")
    private String status;

    @Column(name = "remark")
    @ApiModelProperty(name = "备注")
    private String remark;

    public void copy(ResourceType source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
