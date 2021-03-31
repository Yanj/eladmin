package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.modules.system.service.dto.DeptSmallDto;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 12:51
 */
@Getter
@Setter
public class WorkTimeDto implements Serializable {

    private Long id;

    private Long orgId;

    private Long comId;

    private Long deptId;

    private String beginTime;

    private String endTime;

    private Long duration;

    private String status;

    private String remark;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkTimeDto that = (WorkTimeDto) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
