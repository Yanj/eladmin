package me.zhengjie.modules.yy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2021-03-29 16:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveCount implements Serializable {

    private Long comId;

    private String date;

    private Long workTimeId;

    private Long termId;

    private long count;

}
