package me.zhengjie.modules.yy.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 14:11
 */
@Data
public class ReserveLogCriteria implements Serializable {

    @Query
    private Long id;

    @Query(joinName = "reserve", propName = "id")
    private Long reserveId;

}
