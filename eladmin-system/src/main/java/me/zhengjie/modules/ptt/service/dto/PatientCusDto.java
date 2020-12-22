package me.zhengjie.modules.ptt.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;
import me.zhengjie.modules.system.service.dto.DeptDto;
import me.zhengjie.modules.system.service.dto.DictDetailDto;
import me.zhengjie.modules.system.service.dto.DictDto;

import java.io.Serializable;

/**
 * 患者自定义信息
 *
 * @author yanjun
 * @date 2020-11-28 14:58
 */
@Getter
@Setter
public class PatientCusDto extends BaseDTO implements Serializable {

    private Long id;

    private CusDto cus;

    private DeptDto dept;

    private PatientDto patient;

    private String type;

    private DictDetailDto dictDetail;

    private String value;

    private String status;

    private String remark;

}
