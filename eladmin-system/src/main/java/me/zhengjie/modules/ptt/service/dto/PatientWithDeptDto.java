package me.zhengjie.modules.ptt.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-15 10:47
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientWithDeptDto implements Serializable {

    private Long patientId;

    private Long deptId;

    private String name;

    private String contactName;

    private String contactPhone;

    private String contactRelation;

    private String deptName;

}
