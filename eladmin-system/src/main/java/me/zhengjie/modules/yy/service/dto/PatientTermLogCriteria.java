package me.zhengjie.modules.yy.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 13:56
 */
@Data
public class PatientTermLogCriteria implements Serializable {

    @Query
    private Long id;

    @Query(joinName = "patientTerm", propName = "id")
    private Long patientTermId;

}
