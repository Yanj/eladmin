package me.zhengjie.modules.yy.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author yanjun
 * @date 2020-12-24 22:25
 */
@Entity
@Table(name = "yy_sms")
@Getter
@Setter
public class Sms implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bus_id")
    private Long busId;

    @Column(name = "bus_type")
    private String busType;

    private String mobile;

    private String content;

    private String cell;

    private String status;

    private String remark;

    @Column(name = "send_time")
    private Timestamp sendTime;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "update_by")
    private String updateBy;

    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "update_time")
    private Timestamp updateTime;

    public void copy(Sms source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
