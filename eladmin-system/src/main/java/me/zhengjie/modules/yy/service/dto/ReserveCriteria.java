package me.zhengjie.modules.yy.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;
import me.zhengjie.modules.yy.domain.ReserveVerifyStatus;
import me.zhengjie.utils.enums.YesNoEnum;

import java.io.Serializable;
import java.util.Set;

/**
 * @author yanjun
 * @date 2020-12-24 14:04
 */
@Data
public class ReserveCriteria extends BaseCriteria implements Serializable {

    @Query
    private Long id;

    @Query
    private YesNoEnum status;

    @Query
    private ReserveVerifyStatus verifyStatus;

    @Query
    private String date;

    @Query(joinName = "workTime", propName = "id")
    private Long workTimeId;

    @Query(joinName = "term", propName = "id")
    private Long termId;

    @Query(joinName = "patientTerm", propName = "id")
    private Long patientTermId;

    @Query(type = Query.Type.INNER_LIKE, joinName = "patient", propName = "name")
    private String patientName;

}
