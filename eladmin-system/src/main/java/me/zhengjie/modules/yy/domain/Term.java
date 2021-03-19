package me.zhengjie.modules.yy.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.utils.enums.YesNoEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author yanjun
 * @date 2020-12-24 12:49
 */
@Entity
@Table(name = "yy_term")
@Getter
@Setter
public class Term implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID")
    private Long id;

    @JoinColumn(name = "org_id")
    @ApiModelProperty(value = "组织ID")
    private Long orgId;

    @JoinColumn(name = "com_id")
    @ApiModelProperty(value = "公司ID")
    private Long comId;

    @JoinColumn(name = "dept_id")
    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "外部系统ID")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "价格")
    private Long price;

    @Column(name = "original_price")
    @ApiModelProperty(value = "原价")
    private Long originalPrice;

    @ApiModelProperty(value = "次数")
    private Integer times;

    @ApiModelProperty(value = "单位")
    private String unit;

    @Enumerated
    @ApiModelProperty(value = "状态")
    private YesNoEnum status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @JSONField(serialize = false)
    @ManyToMany()
    @JoinTable(
            name = "yy_term_resource_group",
            joinColumns = {
                    @JoinColumn(name = "term_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "resource_group_id", referencedColumnName = "id")
            }
    )
    private Set<ResourceGroup> resourceGroups;

    public void copy(Term source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
