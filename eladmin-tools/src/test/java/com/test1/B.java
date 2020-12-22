package com.test1;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-11-22 16:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class B implements Serializable {

    private String name;

    public void copy(B source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
