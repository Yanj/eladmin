/**
 * eladmin
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author yanjun
 * @date 2020-11-13 15:53
 */
package me.zhengjie.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.zhengjie.utils.HisCkInfoTypeEnum;

import javax.validation.constraints.NotBlank;

/**
 *
 * @author yanjun
 * @date 2020-11-13 15:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HisCkItemVo {

    @NotBlank
    private String patientInfo;

    @NotBlank
    private HisCkInfoTypeEnum infoType;

}
