package me.zhengjie.modules.yy.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 14:15
 */
@Data
public class ReserveVerifyCriteria implements Serializable {

    @Query
    private Long id;

}
