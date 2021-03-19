package me.zhengjie.modules.yy.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;
import me.zhengjie.utils.enums.YesNoEnum;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 14:36
 */
@Data
public class PatientCriteria implements Serializable {

    @Query
    private Long id;

    @Query
    private Long orgId;

    @Query
    private Long comId;

    @Query
    private Long deptId;

    @Query(blurry = "mrn,name,phone")
    private String blurry;

    @Query
    private String mrn;

    @Query
    private String name;

    @Query
    private String phone;

    @Query
    private YesNoEnum status;

    private String infoType;

    private String patientInfo;

}
