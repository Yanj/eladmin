package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author yanjun
 * @date 2020-12-28 18:51
 */
@Getter
@Setter
public class ReserveCountDto {

    private List<String> dates;

    private List<WorkTimeSmallDto> workTimes;

    private List<TermSmallDto> terms;

    private List<ReserveCountItemDto> items;

}
