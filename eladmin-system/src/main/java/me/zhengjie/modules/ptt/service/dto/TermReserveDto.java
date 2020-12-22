package me.zhengjie.modules.ptt.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author yanjun
 * @date 2020-12-17 18:13
 */
@Getter
@Setter
public class TermReserveDto implements Serializable {

    private Long deptId;

    private Long termId;

    private String date;

    private String beginTime;

    private String endTime;

    private List<PatientTermReserveDto> patientTermReserveList;

}
