package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-30 08:42
 */
@Getter
@Setter
public class ReserveResourceCriteria implements Serializable {

    @Query
    private Long id;

}
