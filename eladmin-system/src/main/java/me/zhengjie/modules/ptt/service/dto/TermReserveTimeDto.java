package me.zhengjie.modules.ptt.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 套餐-时段
 *
 * @author yanjun
 * @date 2020-12-17 15:03
 */
@Getter
@Setter
public class TermReserveTimeDto implements Serializable {

    private Long deptId;

    private Long termId;

    private String date;

    private String beginTime;

    private String endTime;

    private Integer available;

    private Integer count;

    private List<PatientTermReserveDto> patientTermReserveList;

}
