package me.zhengjie.modules.yy.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2021-01-11 17:42
 */
@Data
public class TodayReserveCountDto implements Serializable {

    /**
     * 总数
     */
    private int totalCount;

    /**
     * 待处理总数
     */
    private int preprocessCount;

    /**
     * 处理中总数
     */
    private int processingCount;

    /**
     * 已处理总数
     */
    private int processedCount;

}
