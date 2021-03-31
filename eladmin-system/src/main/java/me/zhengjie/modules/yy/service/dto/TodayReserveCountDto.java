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
    private long totalCount;

    /**
     * 待处理总数
     */
    private long preprocessCount;

    /**
     * 处理中总数
     */
    private long processingCount;

    /**
     * 已处理总数
     */
    private long processedCount;

}
