package me.zhengjie.modules.yy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2021-03-29 15:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReserveWorkTimeCount implements Serializable {

    private Long workTimeId;

    private ReserveVerifyStatus verifyStatus;

    private long count;

}
