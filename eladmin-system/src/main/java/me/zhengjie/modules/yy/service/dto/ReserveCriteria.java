package me.zhengjie.modules.yy.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 14:04
 */
@Data
public class ReserveCriteria implements Serializable {

    @Query
    private Long id;

    @Query(joinName = "dept", propName = "id")
    private Long deptId;

    @Query
    private String date;

    @Query(joinName = "workTime", propName = "id")
    private Long workTimeId;

    @Query(joinName = "term", propName = "id")
    private Long termId;

    @Query(joinName = "patientTerm", propName = "id")
    private Long patientTermId;

}
