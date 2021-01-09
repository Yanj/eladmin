package me.zhengjie.modules.yy.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author yanjun
 * @date 2021-01-09 11:15
 */
/*
select
  p.id as patient_id,
  p.code as code,
  p.mrn as mrn,
  p.name as name,
  p.phone as phone,
  pc.id as id,
  pc.dept_id as dept_id,
  pc.col1 as col1,
  pc.col2 as col2,
  pc.col3 as col3,
  pc.col4 as col4,
  pc.col5 as col5,
  pc.status as status,
  pc.remark as remark
from yy_patient p
left join yy_patient_col pc on p.id = pc.patient_id
where pc.status = '1'
 */
@Subselect(
        value = "" +
                "select  " +
                "  p.id as patient_id, " +
                "  p.code as code, " +
                "  p.mrn as mrn, " +
                "  p.name as name, " +
                "  p.phone as phone, " +
                "  pc.id as id, " +
                "  pc.dept_id as dept_id, " +
                "  pc.col1 as col1, " +
                "  pc.col2 as col2, " +
                "  pc.col3 as col3, " +
                "  pc.col4 as col4, " +
                "  pc.col5 as col5, " +
                "  pc.status as status, " +
                "  pc.remark as remark " +
                "from yy_patient p " +
                "left join yy_patient_col pc on p.id = pc.patient_id " +
                "where pc.status = '1'"
)
@Synchronize({"yy_patient", "yy_patient_col"})
@Entity
@Data
@EqualsAndHashCode
public class QueryPatient implements Serializable {

    @Id
    private Long id;

    @Column(name = "patient_id")
    private Long patientId;

    private String code;

    private String mrn;

    private String name;

    private String phone;

    private String status;

    private String remark;

    @Column(name = "dept_id")
    private Long deptId;

    private String col1;

    private String col2;

    private String col3;

    private String col4;

    private String col5;

    public void copy(QueryPatient source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
