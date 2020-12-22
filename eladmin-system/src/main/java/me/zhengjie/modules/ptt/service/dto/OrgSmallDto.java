package me.zhengjie.modules.ptt.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-03 10:29
 */
@Getter
@Setter
public class OrgSmallDto extends BaseDTO implements Serializable {

    private Long id;

    private String name;

    private Boolean enabled;

    private Integer deptSort;

    private Long pid;

    private Integer subCount;

    private Integer level;

}
