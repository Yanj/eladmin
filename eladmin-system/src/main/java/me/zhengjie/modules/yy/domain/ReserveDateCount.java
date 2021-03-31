package me.zhengjie.modules.yy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 预约统计
 *
 * @author yanjun
 * @date 2021-03-29 14:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveDateCount implements Serializable {

    private String date;

    private ReserveVerifyStatus verifyStatus;

    private long count;

}
