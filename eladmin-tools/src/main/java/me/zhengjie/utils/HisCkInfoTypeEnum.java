/**
 * eladmin
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author yanjun
 * @date 2020-11-13 16:10
 */
package me.zhengjie.utils;

/**
 * @author yanjun
 * @date 2020-11-13 16:10
 */
public enum HisCkInfoTypeEnum {

    PHONE("电话"),
    MRN("病案号"),
    NAME("姓名");

    private final String value;

    HisCkInfoTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
