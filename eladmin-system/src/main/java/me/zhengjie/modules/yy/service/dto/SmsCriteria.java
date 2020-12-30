package me.zhengjie.modules.yy.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 12:49
 */
@Data
public class SmsCriteria implements Serializable {

    @Query
    private Long id;

    @Query(blurry = "mobile,content")
    private String blurry;

}
