package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;
import me.zhengjie.modules.yy.domain.PatientTermType;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author yanjun
 * @date 2020-12-24 13:56
 */
@Getter
@Setter
public class PatientTermDto extends BaseDTO implements Serializable {

    private Long id;

    private Long orgId;

    private Long comId;

    private Long deptId;

    private String patItemId;

    private String type;

    private Long pid;

    private PatientSmallDto patient;

    private Long termId;

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

    private String status;

    private Long duration;

    private Long operatorCount;

    private String remark;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PatientTermDto that = (PatientTermDto) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
