package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author yanjun
 * @date 2020-12-24 22:25
 */
@Getter
@Setter
public class SmsDto implements Serializable {

    private Long id;

    private Long busId;

    private String busType;

    private String mobile;

    private String content;

    private String cell;

    private String status;

    private String remark;

    private Timestamp sendTime;

    private String createBy;

    private String updateBy;

    private Timestamp createTime;

    private Timestamp updateTime;

}
