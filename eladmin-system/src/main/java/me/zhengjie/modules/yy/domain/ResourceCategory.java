package me.zhengjie.modules.yy.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.modules.system.domain.Dept;

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
    private Long id;

    @JSONField(serialize = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Dept dept;

    private String name;

    private Integer count;

    private String status;

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
