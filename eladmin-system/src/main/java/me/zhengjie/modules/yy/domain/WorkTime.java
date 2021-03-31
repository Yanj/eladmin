package me.zhengjie.modules.yy.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.utils.enums.YesNoEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 12:51
 */
@Entity
@Table(name = "yy_work_time")
@Getter
@Setter
public class WorkTime implements Serializable {

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

    @NotNull
    @Pattern(regexp = "\\d{2}[:]\\d{2}")
    @Column(name = "begin_time")
    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @NotNull
    @Pattern(regexp = "\\d{2}[:]\\d{2}")
    @Column(name = "end_time")
    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @Column(name = "duration")
    @ApiModelProperty(value = "时长")
    private Long duration;

    @Enumerated
    @ApiModelProperty(value = "状态")
    private YesNoEnum status;

    @ApiModelProperty(value = "备注")
    private String remark;

    public void copy(WorkTime source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
