package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;
import me.zhengjie.modules.system.service.dto.DeptSmallDto;
import me.zhengjie.modules.system.service.dto.UserSmallDto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

/**
 * @author yanjun
 * @date 2020-12-24 14:04
 */
@Getter
@Setter
public class ReserveDto extends BaseDTO implements Serializable {

    private Long id;

    private Long orgId;

    private Long comId;

    private Long deptId;

    private PatientSmallDto patient;

    private TermSmallDto term;

    private PatientTermSmallDto patientTerm;

    private ResourceGroupSmallDto resourceGroup;

    private String date;

    private WorkTimeSmallDto workTime;

    private String beginTime;

    private String endTime;

    private Set<ReserveResourceDto> reserveResources;

    private Set<UserSmallDto> operators;

    private String verifyStatus;

    private String status;

    private String remark;

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
