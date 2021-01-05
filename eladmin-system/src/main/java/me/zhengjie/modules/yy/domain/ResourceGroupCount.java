package me.zhengjie.modules.yy.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Subselect(value = "" +
        "select " +
        "  rg.id as id , " +
        "  rg.dept_id as dept_id, " +
        "  rg.name as name, " +
        "  rg.status as status," +
        "  rg.remark as remark, " +
        "  min(rc.count) as count " +
        "from yy_resource_group_category rgc " +
        "left join yy_resource_group  rg on rgc.resource_group_id = rg.id " +
        "left join yy_resource_category rc on rgc.resource_category_id = rc.id " +
        "group by rg.id " +
        ""
)
@Entity
@Data
@EqualsAndHashCode()
public class ResourceGroupCount implements Serializable {

    @Id
    private Long id;

    @Column(name = "dept_id")
    private Long deptId;

    private String name;

    private String status;

    private String remark;

    private Integer count;

}
