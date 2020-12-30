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
 * @date 2020-12-24 12:58
 */
@Entity
@Table(name = "yy_resource_group")
@Getter
@Setter
public class ResourceGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JSONField(serialize = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Dept dept;

    private String name;

    private String status;

    private String remark;

    @JSONField(serialize = false)
    @ManyToMany()
    @JoinTable(
            name = "yy_resource_group_category",
            joinColumns = {
                    @JoinColumn(name = "resource_group_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "resource_category_id", referencedColumnName = "id")
            }
    )
    private Set<ResourceCategory> resourceCategories;

    @JSONField(serialize = false)
    @ManyToMany(mappedBy = "resourceGroups")
    private Set<Term> terms;

    public void copy(ResourceGroup source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
