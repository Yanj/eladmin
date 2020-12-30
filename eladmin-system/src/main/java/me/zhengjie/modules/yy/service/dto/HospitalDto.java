package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author yanjun
 * @date 2020-12-24 21:18
 */
@Getter
@Setter
public class HospitalDto implements Serializable {

    private Long id;

    private String name;

    private Boolean enabled;

    private String createBy;

    private String updateBy;

    private Timestamp createTime;

    private Timestamp updateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HospitalDto that = (HospitalDto) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
