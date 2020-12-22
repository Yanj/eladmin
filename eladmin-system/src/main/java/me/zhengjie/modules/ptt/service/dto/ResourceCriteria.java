package me.zhengjie.modules.ptt.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

/**
 * 资源信息查询
 *
 * @author yanjun
 * @date 2020-11-28 11:12
 */
@Data
public class ResourceCriteria implements Serializable {

    @Query
    private Long id;

    @Query(propName = "name", type = Query.Type.INNER_LIKE)
    private String name;

    @Query(propName = "id", joinName = "resourceType")
    private Long resourceTypeId;

    @Query(propName = "id", joinName = "dept")
    private Long deptId;

}
