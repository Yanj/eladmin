package me.zhengjie.modules.ptt.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.Dict;
import me.zhengjie.modules.system.domain.DictDetail;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 患者自定义信息
 *
 * @author yanjun
 * @date 2020-11-28 11:01
 */
@Entity
@Table(name = "ptt_patient_cus")
@Getter
@Setter
public class PatientCus extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cus_id")
    @ApiModelProperty(name = "自定义信息")
    private Cus cus;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    @ApiModelProperty(name = "部门信息")
    private Dept dept;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @ApiModelProperty(name = "患者信息")
    private Patient patient;

    @Column(name = "type")
    @ApiModelProperty(name = "类型: text-文本, dict-字典")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dict_id", referencedColumnName = "detail_id")
    @ApiModelProperty(name = "字典")
    private DictDetail dictDetail;

    @Column(name = "value")
    @ApiModelProperty(name = "内容")
    private String value;

    @Column(name = "status")
    @ApiModelProperty(name = "状态")
    private String status;

    @Column(name = "remark")
    @ApiModelProperty(name = "备注")
    private String remark;

    public void copy(PatientCus source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
