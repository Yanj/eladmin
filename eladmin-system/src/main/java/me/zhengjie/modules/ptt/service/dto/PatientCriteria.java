package me.zhengjie.modules.ptt.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

/**
 * 患者信息查询
 *
 * @author yanjun
 * @date 2020-11-28 11:12
 */
@Data
public class PatientCriteria implements Serializable {

    @Query
    private Long id;

    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    @Query(blurry = "name")
    private String blurry;

    private Long deptId;

    private Long patientId;

}
