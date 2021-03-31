package me.zhengjie.modules.yy.service.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2021-03-22 16:37
 */
@Data
public class PatientSync implements Serializable {

    private String infoType;

    private String patientInfo;

}
