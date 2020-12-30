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
 * @date 2020-12-24 12:49
 */
@Entity
@Table(name = "yy_term")
@Getter
@Setter
public class Term implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JSONField(serialize = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Dept dept;

    private String code;

    private String name;

    private Long price;

    @Column(name = "original_price")
    private Long originalPrice;

    private Integer times;

    private String unit;

    private String status;

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
