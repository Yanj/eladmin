package me.zhengjie.modules.ptt.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;
import me.zhengjie.modules.system.service.dto.DeptDto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 患者套餐预约
 *
 * @author yanjun
 * @date 2020-11-28 10:51
 */
@Getter
@Setter
public class PatientTermReserveDto extends BaseDTO implements Serializable {

    public static final String STATUS_INIT = "0";
    public static final String STATUS_CHECKED = "1";
    public static final String STATUS_USED = "2";
    public static final String STATUS_CANCELED = "3";

    private Long id;

    private TermDto term;

    private PatientTermDto patientTerm;

    private DeptDto dept;

    private String date;

    private String beginTime;

    private String endTime;

    private String actualBeginTime;

    private String actualEndTime;

    private String status;

    private String remark;

}
