package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.modules.system.service.dto.DeptSmallDto;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 12:46
 */
@Getter
@Setter
public class PatientColDto implements Serializable {

    private Long id;

    private PatientDto patient;

    private DeptSmallDto dept;

    private String col1;

    private String col2;

    private String col3;

    private String col4;

    private String col5;

    private String status;

    private String remark;

}
