package me.zhengjie.modules.ptt.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author yanjun
 * @date 2020-12-11 11:49
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientWithCols implements Serializable {

    private Long id;

    private Long deptId;

    private List<Cus> cols;

    public PatientWithCols(Long id, Long deptId) {
        this.id = id;
        this.deptId = deptId;
    }

}
