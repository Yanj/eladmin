package me.zhengjie.modules.ptt.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

/**
 * 患者套餐日志查询
 *
 * @author yanjun
 * @date 2020-11-28 11:12
 */
@Data
public class PatientTermLogCriteria implements Serializable {

    @Query
    private Long id;

    @Query(propName = "name", joinName = "term", type = Query.Type.INNER_LIKE)
    private String termName;

}
