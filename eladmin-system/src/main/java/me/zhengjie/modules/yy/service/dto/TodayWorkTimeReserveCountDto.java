package me.zhengjie.modules.yy.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TodayWorkTimeReserveCountDto implements Serializable {

    private List<String> workTimes;

    private List<Long> counts;

}
