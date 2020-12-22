package me.zhengjie.modules.ptt.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;
import me.zhengjie.modules.system.service.dto.DeptDto;

import java.io.Serializable;

/**
 * 套餐资源类型
 *
 * @author yanjun
 * @date 2020-11-28 10:15
 */
@Getter
@Setter
public class TermResourceTypeDto extends BaseDTO implements Serializable {

    private Long id;

    private TermDto term;

    private DeptDto dept;

    private ResourceTypeDto resourceType;

    private Boolean nullable;

    private String status;

    private String remark;

}
