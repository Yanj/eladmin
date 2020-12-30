package me.zhengjie.modules.yy.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author yanjun
 * @date 2020-12-24 22:25
 */
@Entity
@Subselect(
        " select " +
                " d1.dept_id as id, " +
                " d1.name as name, " +
                " d1.enabled as enabled, " +
                " d1.create_by as create_by, " +
                " d1.update_by as update_by, " +
                " d1.create_time as create_time, " +
                " d1.update_time as update_time " +
                " from sys_dept d1 " +
                " left join sys_dept d2 on d1.pid = d2.dept_id " +
                " where d2.pid is null and d2.dept_id is not null "
)
@Synchronize({"sys_dept"})
@Getter
@Setter
public class Hospital implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Boolean enabled;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp updateTime;

    public void copy(Hospital source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
