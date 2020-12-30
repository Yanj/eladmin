package me.zhengjie.modules.yy.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author yanjun
 * @date 2020-12-24 13:56
 */
@Entity
@Table(name = "yy_patient_term")
@Getter
@Setter
public class PatientTerm implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JSONField(serialize = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private String termCode;

    private String termName;

    private Long termPrice;

    private Long termOriginalPrice;

    private Integer termTimes;

    private String termUnit;

    private Long price;

    private Integer times;

    private String status;

    private String remark;

    @CreatedBy
    @Column(name = "create_by", updatable = false)
    @ApiModelProperty(value = "创建人", hidden = true)
    private String createBy;

    @LastModifiedBy
    @Column(name = "update_by")
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updatedBy;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Timestamp createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间", hidden = true)
    private Timestamp updateTime;

    public void copy(PatientTerm source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
