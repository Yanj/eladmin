package me.zhengjie.modules.ptt.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;
import me.zhengjie.modules.system.service.dto.DeptSmallDto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 患者信息
 *
 * @author yanjun
 * @date 2020-11-28 11:18
 */
@Getter
@Setter
public class PatientDto extends BaseDTO implements Serializable {

    private Long id;

    private Long patientId;

    private String name;

    private String mrn;

    private String certNo;

    private String phone;

    private String birthday;

    private String profession;

    private String address;

    private String contactName;

    private String contactPhone;

    private String contactRelation;

    private String status;

    private String remark;

    private Set<OrgSmallDto> depts;

    private Map<String, PatientCusDto> cols;

    private List<PatientCusDto> colList;

    private DeptSmallDto dept;

}
