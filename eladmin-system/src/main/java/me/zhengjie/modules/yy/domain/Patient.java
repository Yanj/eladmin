package me.zhengjie.modules.yy.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.utils.enums.YesNoEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @author yanjun
 * @date 2020-12-24 12:46
 */
@Entity
@Table(name = "yy_patient")
@Getter
@Setter
public class Patient implements Serializable {

    @Id
    @Column(name = "id")
    @NotNull(groups = BaseEntity.Update.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @Column(name = "org_id")
    @ApiModelProperty(value = "机构ID")
    private Long orgId;

    @Column(name = "com_id")
    @ApiModelProperty(value = "公司ID")
    private Long comId;

    @Column(name = "dept_id")
    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @Enumerated(EnumType.STRING)
    @ApiModelProperty(value = "来源")
    private PatientSourceEnum source;

    @ApiModelProperty(value = "外部系统ID")
    private String code;

    @ApiModelProperty(value = "档案号")
    private String mrn;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "自定义1")
    private String col1;

    @ApiModelProperty(value = "自定义2")
    private String col2;

    @ApiModelProperty(value = "自定义3")
    private String col3;

    @ApiModelProperty(value = "自定义4")
    private String col4;

    @ApiModelProperty(value = "自定义5")
    private String col5;

    @Enumerated
    @ApiModelProperty(value = "状态")
    private YesNoEnum status;

    @ApiModelProperty(value = "备注")
    private String remark;

    public void copy(Patient source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
