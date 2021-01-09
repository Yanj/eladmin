package me.zhengjie.modules.yy.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.modules.system.domain.Dept;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 12:46
 */
@Entity
@Table(name = "yy_patient_col")
@Getter
@Setter
public class PatientCol implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Dept dept;

    private String col1;

    private String col2;

    private String col3;

    private String col4;

    private String col5;

    private String status;

    private String remark;

    public void copy(PatientCol source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
