package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author yanjun
 * @date 2020-12-24 13:56
 */
@Getter
@Setter
public class PatientTermSmallDto extends BaseDTO implements Serializable {

    private Long id;

    private Long patientId;

    private String termCode;

    private String termName;

    private Long termPrice;

    private Long termOriginalPrice;

    private Integer termTimes;

    private String termUnit;

    private Long termDuration;

    private Long termOperatorCount;

    private Long price;

    private Integer totalTimes;

    private Integer times;

    private Integer freeTimes;

    private Long duration;

    private Long operatorCount;

    private String status;

    private String remark;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PatientTermSmallDto that = (PatientTermSmallDto) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
