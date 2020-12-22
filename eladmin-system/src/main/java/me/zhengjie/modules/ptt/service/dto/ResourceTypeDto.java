package me.zhengjie.modules.ptt.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;
import me.zhengjie.modules.system.service.dto.DeptDto;

import java.io.Serializable;

/**
 * 资源类型
 *
 * @author yanjun
 * @date 2020-11-28 10:08
 */
@Getter
@Setter
public class ResourceTypeDto extends BaseDTO implements Serializable {

    private Long id;

    private DeptDto dept;

    private String name;

    private Long maxCount;

    private String status;

    private String remark;

}
