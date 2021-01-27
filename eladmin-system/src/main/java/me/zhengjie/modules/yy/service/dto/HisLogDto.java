package me.zhengjie.modules.yy.service.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
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
 * @date 2020-12-24 12:51
 */
@Data
public class HisLogDto implements Serializable {

    private Long id;

    private Long patItemId;

    private Long visitId;

    private Long patientId;

    private String name;

    private String mobilePhone;

    private String mrn;

    private String visitDept;

    private Timestamp visitDate;

    private String itemCode;

    private String itemName;

    private Long price;

    private Long amount;

    private String unit;

    private Long costs;

    private Long actualCosts;

    private String createBy;

    private String updatedBy;

    private Timestamp createTime;

    private Timestamp updateTime;

    private String remark;

}
