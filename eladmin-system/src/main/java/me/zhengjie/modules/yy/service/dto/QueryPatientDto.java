package me.zhengjie.modules.yy.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2021-01-09 11:15
 */
@Data
@EqualsAndHashCode
public class QueryPatientDto implements Serializable {

    private Long id;

    private Long patientId;

    private String code;

    private String mrn;

    private String name;

    private String phone;

    private String status;

    private String remark;

    private Long deptId;

    private String col1;

    private String col2;

    private String col3;

    private String col4;

    private String col5;

}
