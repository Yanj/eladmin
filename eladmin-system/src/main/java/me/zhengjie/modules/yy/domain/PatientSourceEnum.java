package me.zhengjie.modules.yy.domain;

import java.io.Serializable;

/**
 * 患者来源
 *
 * @author yanjun
 * @date 2021-03-18 15:17
 */
public enum PatientSourceEnum implements Serializable {

    HIS("HIS"),
    MEITUAN("美团")
    ;

    private String label;

    PatientSourceEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
