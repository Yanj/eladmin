package me.zhengjie.modules.yy.domain;

import java.io.Serializable;

/**
 * 患者套餐类型
 *
 * @author yanjun
 * @date 2021-03-22 11:40
 */
public enum PatientTermType implements Serializable {

    his("HIS"),
    free("免费"),
    ;

    private final String label;

    PatientTermType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
