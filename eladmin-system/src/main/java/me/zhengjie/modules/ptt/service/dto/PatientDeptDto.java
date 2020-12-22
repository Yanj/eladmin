package me.zhengjie.modules.ptt.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;
import me.zhengjie.modules.system.service.dto.DeptDto;
import me.zhengjie.modules.system.service.dto.DeptSmallDto;

import java.io.Serializable;

/**
 * 患者部门
 *
 * @author yanjun
 * @date 2020-11-28 10:22
 */
@Getter
@Setter
public class PatientDeptDto implements Serializable {

    private DeptSmallDto dept;

    private PatientSmallDto patient;

}
