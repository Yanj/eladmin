package me.zhengjie.modules.ptt.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-14 16:16
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(PatientWithDept.PK.class)
@Subselect(
        "select " +
                "p.*, " +
                "d.dept_id dept_id, d.dept_id dept_dept_id, " +
                "sd.`name` dept_name " +
                "from ptt_patient p " +
                "left join ptt_patient_dept d on p.id = d.patient_id " +
                "left join sys_dept sd on d.dept_id = sd.dept_id"
)
@Synchronize(value = {"ptt_patient", "ptt_patient_dept"})
public class PatientWithDept implements Serializable {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PK implements Serializable {

        private Long patientId;

        private Long deptId;
    }

    @Id
    @Column(name = "id")
    private Long patientId;

    @Id
    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "name")
    private String name;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "contact_relation")
    private String contactRelation;

    @Column(name = "dept_name")
    private String deptName;

}
