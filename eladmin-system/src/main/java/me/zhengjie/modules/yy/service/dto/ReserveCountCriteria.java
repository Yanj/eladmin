package me.zhengjie.modules.yy.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;
import me.zhengjie.utils.enums.YesNoEnum;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 14:04
 */
@Data
public class ReserveCountCriteria extends BaseCriteria implements Serializable {

    @Query
    private YesNoEnum status;

    private String date;

    private int days;

    private Long termId;

    private Long resourceGroupId;

    private String beginDate;

    private String endDate;

    private String beginTime;

    private String endTime;

}
