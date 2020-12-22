package me.zhengjie.modules.ptt.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

/**
 * 套餐资源信息查询
 *
 * @author yanjun
 * @date 2020-11-28 11:12
 */
@Data
public class TermResourceTypeCriteria implements Serializable {

    @Query
    private Long id;

}
