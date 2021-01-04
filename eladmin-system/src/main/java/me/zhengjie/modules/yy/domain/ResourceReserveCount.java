package me.zhengjie.modules.yy.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * 根据 医院/日期/时段 统计资源使用情况
 */
@Subselect("" +
        "select " +
        "  t1.*, " +
        "  t1.count-t1.used_count as left_count " +
        "from ( " +
        "  select " +
        "    t.dept_id, " +
        "    t.date, " +
        "    t.work_time_id, " +
        "    t.resource_category_id, " +
        "    t.count, " +
        "    count(t.resource_category_id) as used_count " +
        "  from ( " +
        "    select " +
        "      r.dept_id, " +
        "      r.date, " +
        "      r.work_time_id, " +
        "      rr.resource_category_id, " +
        "      rc.count " +
        "    from yy_reserve_resource rr " +
        "    left join yy_reserve r on rr.reserve_id = r.id " +
        "    left join yy_resource_category rc on rr.resource_category_id =  rc.id " +
        "  ) t " +
        "  group by t.dept_id, t.date, t.work_time_id, t.resource_category_id " +
        ") t1 " +
        ""
)
@Synchronize({"yy_reserve_resource", "yy_reserve", "yy_resource_category"})
@Entity
@Data
@EqualsAndHashCode
public class ResourceReserveCount implements Serializable {

    @Embeddable
    @Data
    @EqualsAndHashCode
    public static class PK implements Serializable {

        @Column(name = "dept_id")
        private Long deptId;

        @Column(name = "date")
        private String date;

        @Column(name = "work_time_id")
        private Long workTimeId;

        @Column(name = "resource_category_id")
        private Long resourceCategoryId;
    }

    @EmbeddedId
    private PK pk;

    @Column(name = "count")
    private Integer count;

    @Column(name = "used_count")
    private Integer usedCount;

    @Column(name = "left_count")
    private Integer leftCount;

}
