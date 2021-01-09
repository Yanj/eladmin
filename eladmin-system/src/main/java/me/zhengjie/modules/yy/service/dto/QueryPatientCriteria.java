package me.zhengjie.modules.yy.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2021-01-09 16:25
 */
@Data
public class QueryPatientCriteria implements Serializable {

    @Query
    private Long id;

    @Query
    private Long deptId;

    @Query(blurry = "name,mrn,phone")
    private String blurry;

}
