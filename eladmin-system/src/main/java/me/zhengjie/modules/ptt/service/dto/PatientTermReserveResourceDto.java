package me.zhengjie.modules.ptt.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;

import java.io.Serializable;

/**
 * 患者套餐预约资源
 *
 * @author yanjun
 * @date 2020-11-28 10:57
 */
@Getter
@Setter
public class PatientTermReserveResourceDto extends BaseDTO implements Serializable {

    private Long id;

    private PatientTermReserveDto patientTermReserve;

    private ResourceDto resource;

    private String status;

    private String remark;

}
