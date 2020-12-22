package me.zhengjie.modules.ptt.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zhengjie.modules.system.domain.Dept;

import java.io.Serializable;
import java.util.Set;

/**
 * @author yanjun
 * @date 2020-12-08 23:46
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientFull implements Serializable {

    private Long id;

    private Long patientId;

    private String name;

    private String mrn;

    private String certNo;

    private String phone;

    private String birthday;

    private String profession;

    private String address;

    private String contactName;

    private String contactPhone;

    private String contactRelation;

    private String status;

    private String remark;

    private Dept dept;

    private Set<PatientCus> cols;

}
