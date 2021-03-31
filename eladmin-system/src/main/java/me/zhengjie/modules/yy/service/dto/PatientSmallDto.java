package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2021-03-22 12:43
 */
@Getter
@Setter
public class PatientSmallDto implements Serializable {

    private Long id;

    private String mrn;

    private String name;

    private String phone;

}
