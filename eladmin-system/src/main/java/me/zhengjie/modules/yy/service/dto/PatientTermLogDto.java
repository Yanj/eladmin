package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author yanjun
 * @date 2020-12-24 14:11
 */
@Getter
@Setter
public class PatientTermLogDto implements Serializable {

    private Long id;

    private PatientTermSmallDto patientTerm;

    private String content;

    private String type;

    private String before;

    private String after;

    private String createBy;

    private Timestamp createTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PatientTermLogDto that = (PatientTermLogDto) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
