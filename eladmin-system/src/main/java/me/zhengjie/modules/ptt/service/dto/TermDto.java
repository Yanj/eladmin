package me.zhengjie.modules.ptt.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;
import me.zhengjie.service.dto.HisCkItemDto;

import java.io.Serializable;

/**
 * 套餐
 *
 * @author yanjun
 * @date 2020-11-28 10:13
 */
@Getter
@Setter
public class TermDto extends BaseDTO implements Serializable {

    private Long id;

    private String code;

    private String name;

    private String description;

    private Long duration;

    private Integer times;

    private String unit;

    private Long price;

    private Long amount;

    private String status;

    private String remark;

    // ====== others

    private HisCkItemDto ckItem;

}
