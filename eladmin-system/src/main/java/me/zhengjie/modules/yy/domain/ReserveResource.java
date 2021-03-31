package me.zhengjie.modules.yy.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.utils.enums.YesNoEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-30 08:42
 */
@Entity
@Table(name = "yy_reserve_resource")
@Getter
@Setter
public class ReserveResource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID")
    private Long id;

    @JSONField(serialize = false)
    @ManyToOne
    @JoinColumn(name = "reserve_id")
    @ApiModelProperty(value = "预约")
    private Reserve reserve;

    @NotNull
    @Column(name = "date")
    @ApiModelProperty(value = "日期")
    private String date;

    @JSONField(serialize = false)
    @ManyToOne
    @JoinColumn(name = "work_time_id")
    @ApiModelProperty(value = "工作时段")
    private WorkTime workTime;

    @JSONField(serialize = false)
    @ManyToOne
    @JoinColumn(name = "resource_category_id")
    @ApiModelProperty(value = "资源分类")
    private ResourceCategory resourceCategory;

    @JSONField(serialize = false)
    @ManyToOne
    @JoinColumn(name = "resource_group_id")
    @ApiModelProperty(value = "资源分组")
    private ResourceGroup resourceGroup;

    @JSONField(serialize = false)
    @ManyToOne
    @JoinColumn(name = "resource_id")
    @ApiModelProperty(value = "资源")
    private Resource resource;

    @Enumerated
    @Column(name = "status")
    @ApiModelProperty(value = "状态")
    private YesNoEnum status;

    public void copy(ReserveResource source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
