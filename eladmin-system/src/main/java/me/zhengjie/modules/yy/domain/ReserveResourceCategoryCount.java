package me.zhengjie.modules.yy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2021-03-29 12:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveResourceCategoryCount implements Serializable {

    private Long workTimeId;

    private Long resourceCategoryId;

    private Long count;

}
