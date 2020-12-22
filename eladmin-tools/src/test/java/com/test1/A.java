package com.test1;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-11-22 16:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class A implements Serializable {

    private String a1;
    private String a2;
    private B b1;
    private B b2;

    public void copy(A source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

}
