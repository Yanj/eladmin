package me.zhengjie.modules.ptt.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

/**
 * 自定义信息查询
 *
 * @author yanjun
 * @date 2020-11-28 11:12
 */
@Data
public class CusCriteria implements Serializable {

    @Query
    private Long id;

    @Query(joinName = "dept", propName = "id")
    private Long deptId;

    @Query(blurry = "title")
    private String blurry;

}
