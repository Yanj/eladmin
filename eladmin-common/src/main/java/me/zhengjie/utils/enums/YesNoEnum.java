package me.zhengjie.utils.enums;

import org.springframework.lang.NonNullApi;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2021-03-19 11:31
 */
public enum YesNoEnum implements Serializable {

    NO("否"),
    YES("是");

    private final String label;

    YesNoEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
