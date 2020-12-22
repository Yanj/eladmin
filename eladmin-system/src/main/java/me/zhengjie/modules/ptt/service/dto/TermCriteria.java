package me.zhengjie.modules.ptt.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

/**
 * 套餐信息查询
 *
 * @author yanjun
 * @date 2020-11-28 11:12
 */
@Data
public class TermCriteria implements Serializable {

    @Query
    private Long id;

    @Query(propName = "name", type = Query.Type.INNER_LIKE)
    private String name;

    private Long deptId;

    private Long termId;

    private String date;

}
