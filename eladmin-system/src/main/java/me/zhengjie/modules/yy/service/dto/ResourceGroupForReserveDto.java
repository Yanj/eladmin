package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author yanjun
 * @date 2020-12-28 13:26
 */
@Getter
@Setter
public class ResourceGroupForReserveDto implements Serializable {

    private Long id;

    private String name;

    private List<ResourceCategoryForReserveDto> resourceCategories;

}
