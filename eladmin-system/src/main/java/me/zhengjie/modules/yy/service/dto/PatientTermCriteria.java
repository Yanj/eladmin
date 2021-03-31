package me.zhengjie.modules.yy.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;
import me.zhengjie.utils.enums.YesNoEnum;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 13:56
 */
@Data
public class PatientTermCriteria extends BaseCriteria implements Serializable {

    @Query
    private Long id;

    @Query
    private String patItemId;

    @Query(blurry = "termCode,termName")
    private String blurry;

    @Query
    private String termCode;

    @Query(joinName = "patient", propName = "id")
    private Long patientId;

    @Query(type = Query.Type.INNER_LIKE, joinName = "patient", propName = "name")
    private String patientName;

    @Query
    private YesNoEnum status;

}
