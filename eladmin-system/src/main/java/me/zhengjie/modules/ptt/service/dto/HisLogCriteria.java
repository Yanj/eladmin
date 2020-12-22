package me.zhengjie.modules.ptt.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

/**
 * HIS查询日志
 *
 * @author yanjun
 * @date 2020-11-28 17:27
 */
@Data
public class HisLogCriteria implements Serializable {

    @Query
    private Long id;

}
