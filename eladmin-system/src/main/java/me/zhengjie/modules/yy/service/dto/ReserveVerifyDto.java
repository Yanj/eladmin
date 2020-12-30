package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
 * @author yanjun
 * @date 2020-12-24 14:15
 */
@Getter
@Setter
public class ReserveVerifyDto implements Serializable {

    private Long id;

    private ReserveSmallDto reserve;

    private Long reserveId;

    private ResourceGroupSmallDto resourceGroup;

    private Long resourceGroupId;

    private Set<ResourceSmallDto> resources;

    private String status;

    private String createBy;

    private String updatedBy;

    private Timestamp createTime;

    private Timestamp updateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReserveVerifyDto that = (ReserveVerifyDto) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
