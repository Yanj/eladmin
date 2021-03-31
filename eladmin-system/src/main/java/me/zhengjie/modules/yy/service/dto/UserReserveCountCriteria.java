package me.zhengjie.modules.yy.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2021-03-31 16:45
 */
@Data
public class UserReserveCountCriteria extends BaseCriteria implements Serializable {

    private String beginDate;

    private String endDate;

}
