package me.zhengjie.modules.ptt.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;
import me.zhengjie.modules.system.service.dto.DeptDto;

import java.io.Serializable;

/**
 * 患者套餐
 *
 * @author yanjun
 * @date 2020-11-28 10:45
 */
@Getter
@Setter
public class PatientTermDto extends BaseDTO implements Serializable {

    private Long id;

    private PatientDto patient;

    private TermDto term;

    private String termCode;

    private String termName;

    private String termDescription;

    private Long termDuration;

    private Integer termTimes;

    private String termUnit;

    private Long termPrice;

    private Long termAmount;

    private Integer times;

    private Long amount;

    private DeptDto lastDept;

    private String status;

    private String remark;

}
