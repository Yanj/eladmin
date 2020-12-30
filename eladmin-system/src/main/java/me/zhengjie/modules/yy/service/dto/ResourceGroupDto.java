package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.modules.system.service.dto.DeptSmallDto;

import java.io.Serializable;
import java.util.Set;

/**
 * @author yanjun
 * @date 2020-12-24 12:58
 */
@Getter
@Setter
public class ResourceGroupDto implements Serializable {

    private Long id;

    private DeptSmallDto dept;

    private String name;

    private String status;

    private String remark;

    private Set<ResourceCategorySmallDto> resourceCategories;

    private Set<TermSmallDto> terms;

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

        ResourceGroupDto that = (ResourceGroupDto) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
