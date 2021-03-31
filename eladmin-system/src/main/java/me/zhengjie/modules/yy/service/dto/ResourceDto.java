package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 12:55
 */
@Getter
@Setter
public class ResourceDto implements Serializable {

    private Long id;

    private Long orgId;

    private Long comId;

    private Long deptId;

    private ResourceCategorySmallDto resourceCategory;

    private String name;

    private Integer count;

    private String status;

    private String remark;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceDto that = (ResourceDto) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
