package me.zhengjie.modules.yy.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 14:04
 */
@Data
public class ReserveCountCriteria implements Serializable {

    private Long deptId;

    private String date;

    private Long termId;

    private Long resourceGroupId;

    private String beginDate;

    private String endDate;

    private String beginTime;

    private String endTime;

}
