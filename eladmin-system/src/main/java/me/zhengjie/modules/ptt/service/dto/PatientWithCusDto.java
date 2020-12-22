package me.zhengjie.modules.ptt.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zhengjie.modules.system.service.dto.DictDetailDto;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-15 10:26
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientWithCusDto implements Serializable {

    private CusDto cus;

    private Long deptId;

    private Long patientId;

    private Long cusId;

    private String type;

    private String value;

    private DictDetailDto dictDetail;

}
