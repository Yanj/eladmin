package me.zhengjie.modules.yy.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

/**
 * 日志查询
 */
@Data
public class HisLogCriteria implements Serializable {

    @Query
    private Long id;

}
