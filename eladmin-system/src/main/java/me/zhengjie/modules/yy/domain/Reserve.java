package me.zhengjie.modules.yy.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.utils.enums.YesNoEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * @author yanjun
 * @date 2020-12-24 14:04
 */
@Entity
@Table(name = "yy_reserve")
@Getter
@Setter
public class Reserve extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID")
    private Long id;

    @Column(name = "org_id")
    @ApiModelProperty(value = "组织ID")
    private Long orgId;

    @Column(name = "com_id")
    @ApiModelProperty(value = "公司ID")
    private Long comId;

    @Column(name = "dept_id")
    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @JSONField(serialize = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @ApiModelProperty(value = "患者")
    private Patient patient;

    @JSONField(serialize = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id")
    @ApiModelProperty(value = "套餐")
    private Term term;

    @JSONField(serialize = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_term_id")
    @ApiModelProperty(value = "患者套餐")
    private PatientTerm patientTerm;

    @JSONField(serialize = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resource_group_id")
    @ApiModelProperty(value = "资源分组")
    private ResourceGroup resourceGroup;

    @NotNull
    @JoinColumn(name = "date")
    @ApiModelProperty(value = "日期")
    private String date;

    @NotNull
    @JSONField(serialize = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_time_id")
    @ApiModelProperty(value = "工作时段")
    private WorkTime workTime;

    @NotNull
    @JoinColumn(name = "begin_time")
    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @NotNull
    @JoinColumn(name = "end_time")
    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @JSONField(serialize = false)
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserve_id", referencedColumnName = "id")
    @ApiModelProperty(value = "预约资源")
    private Set<ReserveResource> reserveResources;

    @JSONField(serialize = false)
    @ManyToMany()
    @JoinTable(
            name = "yy_reserve_operator",
            joinColumns = {
                    @JoinColumn(name = "reserve_id", referencedColumnName = "id"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
            }
    )
    @ApiModelProperty(value = "操作员")
    private Set<User> operators;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "verify_status")
    @ApiModelProperty(value = "核销状态")
    private ReserveVerifyStatus verifyStatus;

    @Enumerated
    @Column(name = "status")
    @ApiModelProperty(value = "状态")
    private YesNoEnum status;

    @Column(name = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    public void copy(Reserve source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
