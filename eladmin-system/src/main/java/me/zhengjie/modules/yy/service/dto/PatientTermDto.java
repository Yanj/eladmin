package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author yanjun
 * @date 2020-12-24 13:56
 */
@Getter
@Setter
public class PatientTermDto implements Serializable {

    private Long id;

    private String patItemId;

    private PatientTermDto parent;

    private PatientDto patient;

    private Long patientId;

    private String termCode;

    private String termName;

    private Long termPrice;

    private Long termOriginalPrice;

    private Integer termTimes;

    private String termUnit;

    private Long price;

    private Integer times;

    private Integer freeTimes;

    private String status;

    private String remark;

    private String createBy;

    private String updatedBy;

    private Timestamp createTime;

    private Timestamp updateTime;

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
