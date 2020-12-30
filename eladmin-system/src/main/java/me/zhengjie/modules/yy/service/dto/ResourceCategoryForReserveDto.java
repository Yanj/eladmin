package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.modules.system.service.dto.DeptSmallDto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author yanjun
 * @date 2020-12-28 13:07
 */
@Getter
@Setter
public class ResourceCategoryForReserveDto implements Serializable {

    private Long id;

    private DeptSmallDto dept;

    private String name;

    private String status;

    private String remark;

    private List<ResourceSmallDto> resources;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceCategoryForReserveDto that = (ResourceCategoryForReserveDto) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
