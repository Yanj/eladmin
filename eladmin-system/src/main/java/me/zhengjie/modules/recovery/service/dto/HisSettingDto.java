/**
 * eladmin
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author yanjun
 * @date 2020-10-22 17:39
 */
package me.zhengjie.modules.recovery.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author yanjun
 * @date 2020-10-22 17:39
 */
@Getter
@Setter
public class HisSettingDto extends BaseDTO implements Serializable {

    private Long id;

    private String name;

    private String address;

    private Boolean enabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HisSettingDto that = (HisSettingDto) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
