package me.zhengjie.modules.yy.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;
import me.zhengjie.utils.enums.YesNoEnum;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 12:55
 */
@Data
public class ResourceCriteria extends BaseCriteria implements Serializable {

    @Query
    private Long id;

    @Query(blurry = "name")
    private String blurry;

    @Query
    private String name;

    @Query
    private YesNoEnum status;

}
