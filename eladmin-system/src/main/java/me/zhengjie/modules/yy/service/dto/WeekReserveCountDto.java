package me.zhengjie.modules.yy.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author yanjun
 * @date 2021-01-12 18:09
 */
@Data
public class WeekReserveCountDto implements Serializable {

    private List<String> dates;

    private List<Long> all;

    private List<Long> init;

    private List<Long> checkIn;

    private List<Long> verified;

}
