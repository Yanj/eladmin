package me.zhengjie.modules.ptt.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

/**
 * 医院查询
 *
 * @author yanjun
 * @date 2020-11-28 11:12
 */
@Data
public class OrgCriteria implements Serializable {

    @Query
    private Long id;

    @Query
    private Long pid;

    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    @Query
    private Boolean enabled;

}
