package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author yanjun
 * @date 2020-12-28 18:55
 */
@Getter
@Setter
public class ReserveCountItemDto implements Serializable {

    private String date;

    private WorkTimeSmallDto workTime;

    private Map<String, Long> terms;

}
