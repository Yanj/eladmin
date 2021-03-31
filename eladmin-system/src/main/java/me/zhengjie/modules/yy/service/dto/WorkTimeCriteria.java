package me.zhengjie.modules.yy.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;
import me.zhengjie.utils.enums.YesNoEnum;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 12:51
 */
@Data
public class WorkTimeCriteria extends BaseCriteria implements Serializable {

    @Query
    private Long id;

    @Query
    private YesNoEnum status;

}
