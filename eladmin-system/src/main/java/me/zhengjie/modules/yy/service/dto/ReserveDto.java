package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.modules.system.service.dto.DeptSmallDto;
import me.zhengjie.modules.yy.domain.ReserveResource;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
 * @author yanjun
 * @date 2020-12-24 14:04
 */
@Getter
@Setter
public class ReserveDto implements Serializable {

    private Long id;

    private DeptSmallDto dept;

    private PatientDto patient;

    private TermSmallDto term;

    private PatientTermSmallDto patientTerm;

    private Long patientTermId;

    private ResourceGroupSmallDto resourceGroup;

    private Long resourceGroupId;

    private WorkTimeSmallDto workTime;

    private Long workTimeId;

    private Set<ReserveResourceDto> reserveResources;

    private String date;

    private String beginTime;

    private String endTime;

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

        ReserveDto that = (ReserveDto) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
