package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.modules.system.service.dto.DeptSmallDto;
import me.zhengjie.utils.enums.YesNoEnum;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 12:51
 */
@Getter
@Setter
public class WorkTimeSmallDto implements Serializable {

    private Long id;

    private String beginTime;

    private String endTime;

    private String status;

    private String remark;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkTimeSmallDto that = (WorkTimeSmallDto) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
