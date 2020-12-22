package me.zhengjie.modules.ptt.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;

import java.io.Serializable;
import java.util.Set;

/**
 * 患者信息
 *
 * @author yanjun
 * @date 2020-11-28 11:18
 */
@Getter
@Setter
public class PatientSmallDto implements Serializable {

    private Long id;

    private String name;

}
