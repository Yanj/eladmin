package me.zhengjie.modules.yy.domain;

import java.io.Serializable;

/**
 * 预约核销状态
 *
 * @author yanjun
 * @date 2021-03-22 10:44
 */
public enum ReserveVerifyStatus implements Serializable {

    init("已预约"),
    check_in("已签到"),
    verified("已核销"),
    canceled("已取消"),
    ;

    private final String label;

    ReserveVerifyStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
