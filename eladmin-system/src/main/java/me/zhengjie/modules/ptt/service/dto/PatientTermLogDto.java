package me.zhengjie.modules.ptt.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;

import java.io.Serializable;

/**
 * 患者套餐日志
 *
 * @author yanjun
 * @date 2020-11-28 10:48
 */
@Getter
@Setter
public class PatientTermLogDto extends BaseDTO implements Serializable {

    private Long id;

    private TermDto term;

    private PatientTermDto patientTerm;

    private PatientTermReserveDto patientTermReserve;

    private Integer beforeTimes;

    private Integer afterTimes;

    private String content;

    private String status;

    private String remark;

}
