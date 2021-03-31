package me.zhengjie.modules.yy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 患者套餐余额
 *
 * @author yanjun
 * @date 2021-03-31 17:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientTermTimesCount implements Serializable {

    private Long termId;

    private String termName;

    private long termPrice;

    private long totalTimes;

    private long times;

}
