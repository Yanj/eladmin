package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.modules.system.service.dto.DeptSmallDto;

import java.io.Serializable;
import java.util.Set;

/**
 * @author yanjun
 * @date 2020-12-24 12:49
 */
@Getter
@Setter
public class TermSmallDto implements Serializable {

    private Long id;

    private String code;

    private String name;

    private Long price;

    private Long originalPrice;

    private Integer times;

    private String unit;

    private Long duration;

    private Long operatorCount;

    private String status;

    private String remark;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TermSmallDto that = (TermSmallDto) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
