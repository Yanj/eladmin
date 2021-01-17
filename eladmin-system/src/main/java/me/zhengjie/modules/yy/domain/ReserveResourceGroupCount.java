package me.zhengjie.modules.yy.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Subselect;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;

/*
select dept_id, date, work_time_id, resource_group_id, count(resource_group_id) from yy_reserve group by dept_id, date, work_time_id, resource_group_id
 */
/**
 *
 */
@Subselect("" +
        "select " +
        "  r.dept_id as dept_id, " +
        "  r.date as date, " +
        "  r.work_time_id as work_time_id, " +
        "  r.resource_group_id as resource_group_id, " +
        "  count(r.resource_group_id) as count " +
        "from yy_reserve r " +
        "where r.status <> 'canceled' " +
        "group by dept_id, date, work_time_id, resource_group_id "
)
@Entity
@Data
public class ReserveResourceGroupCount implements Serializable {

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

        @Column(name = "resource_group_id")
        private Long resourceGroupId;
    }

    @EmbeddedId
    private PK pk;

    @Column(name = "count")
    private Integer count;

}
