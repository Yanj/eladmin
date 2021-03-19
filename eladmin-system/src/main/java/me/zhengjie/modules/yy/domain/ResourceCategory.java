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
 * @date 2020-12-24 12:53
 */
@Entity
@Table(name = "yy_resource_category")
@Getter
@Setter
public class ResourceCategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "组织ID")
    @JoinColumn(name = "org_id")
    private Long orgId;

    @ApiModelProperty(value = "公司ID")
    @JoinColumn(name = "com_id")
    private Long comId;

    @ApiModelProperty(value = "部门ID")
    @JoinColumn(name = "dept_id")
    private Long deptId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "数量")
    private Integer count;

    @Enumerated
    @ApiModelProperty(value = "状态")
    private YesNoEnum status;

    @ApiModelProperty(value = "备注")
    private String remark;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "resourceCategory")
    private Set<Resource> resources;

    @JSONField(serialize = false)
    @ManyToMany(mappedBy = "resourceCategories")
    private Set<ResourceGroup> resourceGroups;

    public void copy(ResourceCategory source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
