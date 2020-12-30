package me.zhengjie.modules.yy.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 12:55
 */
@Data
public class ResourceCriteria implements Serializable {

    @Query
    private Long id;

    @Query(joinName = "dept", propName = "id")
    private Long deptId;

}
