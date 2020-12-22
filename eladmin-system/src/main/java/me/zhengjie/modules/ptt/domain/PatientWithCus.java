package me.zhengjie.modules.ptt.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zhengjie.modules.system.domain.DictDetail;
import org.apache.ibatis.annotations.One;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-14 18:13
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(PatientWithCus.PK.class)
@Subselect(
        "select " +
        "pc.id cus_id ,pc.dept_id, pc.`type`, pc.dict_id, pc.title, pc.cus_sort, " +
        "ppc.id patient_cus_id, ppc.patient_id, ppc.value, ppc.dict_id dict_detail_id " +
        "from ptt_cus pc " +
        "left join ptt_patient_cus ppc on pc.id = ppc.cus_id"
)
public class PatientWithCus implements Serializable {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PK implements Serializable {

        private Long deptId;

        private Long patientId;
    }

    @Id
    @Column(name = "dept_id")
    private Long deptId;

    @Id
    @Column(name = "patient_id")
    private Long patientId;

    @Column(name = "cus_id")
    private Long cusId;

    private String type;

    private String value;

    @OneToOne
    @JoinColumn(name = "dict_detail_id", referencedColumnName = "detail_id")
    private DictDetail dictDetail;

}
