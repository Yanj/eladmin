package me.zhengjie.modules.yy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2021-03-29 15:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveTermCount implements Serializable {

    private Long termId;

    private String date;

    private long count;

}
