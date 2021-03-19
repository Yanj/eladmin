package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.modules.system.service.dto.DeptSmallDto;
import me.zhengjie.modules.yy.domain.Resource;

import java.io.Serializable;
import java.util.Set;

/**
 * @author yanjun
 * @date 2020-12-24 12:53
 */
@Getter
@Setter
public class ResourceCategoryDto implements Serializable {

    private Long id;

    private Long orgId;

    private Long comId;

    private Long deptId;

    private String name;

    private Integer count;

    private String status;

    private String remark;

    private Set<ResourceSmallDto> resources;

    private Set<ResourceGroupSmallDto> resourceGroups;

    public Boolean getHasChildren() {
        return false;
    }

    public Boolean getLeaf() {
        return true;
    }

    public String getLabel() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceCategoryDto that = (ResourceCategoryDto) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
