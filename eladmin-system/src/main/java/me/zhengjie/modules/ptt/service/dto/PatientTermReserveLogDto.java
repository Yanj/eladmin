package me.zhengjie.modules.ptt.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;

import java.io.Serializable;

/**
 * 患者套餐预约日志
 *
 * @author yanjun
 * @date 2020-11-28 10:55
 */
@Getter
@Setter
public class PatientTermReserveLogDto extends BaseDTO implements Serializable {

    private Long id;

    private PatientTermReserveDto patientTermReserve;

    private String beforeStatus;

    private String afterStatus;

    private String content;

    private String status;

    private String remark;

}
