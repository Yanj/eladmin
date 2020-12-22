package me.zhengjie.modules.ptt.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

/**
 * 患者套餐信息查询
 *
 * @author yanjun
 * @date 2020-11-28 11:12
 */
@Data
public class PatientTermCriteria implements Serializable {

    @Query
    private Long id;

    @Query(propName = "id", joinName = "patient")
    private Long patientId;

    @Query(propName = "id", joinName = "term")
    private Long termId;

    @Query(propName = "name", joinName = "patient", type = Query.Type.INNER_LIKE)
    private String patientName;

    @Query(propName = "name", joinName = "term", type = Query.Type.INNER_LIKE)
    private String termName;

}
