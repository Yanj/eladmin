package me.zhengjie.modules.ptt.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.Dict;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 自定义信息
 *
 * @author yanjun
 * @date 2020-11-28 11:00
 */
@Entity
@Table(name = "ptt_cus")
@Getter
@Setter
public class Cus extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    @ApiModelProperty(name = "医院")
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Dept dept;

    @Column(name = "type")
    @ApiModelProperty(name = "类型: text-文本, dict-字典")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dict_id")
    @ApiModelProperty(name = "字典")
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Dict dict;

    @Column(name = "title")
    @ApiModelProperty(name = "标题")
    private String title;

    @Column(name = "status")
    @ApiModelProperty(name = "状态")
    private String status;

    @Column(name = "remark")
    @ApiModelProperty(name = "备注")
    private String remark;

    @Column(name = "cus_sort")
    @ApiModelProperty(name = "排序")
    private Integer sort;

    public void copy(Cus source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
