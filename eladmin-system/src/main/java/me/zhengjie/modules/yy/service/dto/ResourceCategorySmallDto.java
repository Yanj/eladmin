package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-25 23:34
 */
@Getter
@Setter
public class ResourceCategorySmallDto implements Serializable {

    private Long id;

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceCategorySmallDto that = (ResourceCategorySmallDto) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
