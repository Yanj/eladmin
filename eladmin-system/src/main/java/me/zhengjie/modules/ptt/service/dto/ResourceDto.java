package me.zhengjie.modules.ptt.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;
import me.zhengjie.modules.system.service.dto.DeptDto;

import java.io.Serializable;

/**
 * 资源
 *
 * @author yanjun
 * @date 2020-11-28 10:10
 */
@Getter
@Setter
public class ResourceDto extends BaseDTO implements Serializable {

    private Long id;

    private ResourceTypeDto resourceType;

    private DeptDto dept;

    private String code;

    private String name;

    private Long count;

    private String status;

    private String remark;

}
