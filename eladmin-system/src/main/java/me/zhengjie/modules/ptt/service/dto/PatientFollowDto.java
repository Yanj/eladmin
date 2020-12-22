package me.zhengjie.modules.ptt.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;

import java.io.Serializable;

/**
 * 患者跟进
 *
 * @author yanjun
 * @date 2020-11-28 10:28
 */
@Getter
@Setter
public class PatientFollowDto extends BaseDTO implements Serializable {

    private Long id;

    private PatientDto patient;

    private String followIn;

    private String status;

    private String remark;

}
