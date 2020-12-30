package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-30 08:42
 */
@Getter
@Setter
public class ReserveResourceDto implements Serializable {

    private Long id;

    private ReserveSmallDto reserve;

    private ResourceCategorySmallDto resourceCategory;

    private ResourceGroupSmallDto resourceGroup;

    private ResourceSmallDto resource;

}
