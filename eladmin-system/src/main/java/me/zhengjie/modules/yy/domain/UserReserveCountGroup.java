package me.zhengjie.modules.yy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户预约统计
 *
 * @author yanjun
 * @date 2021-04-27 11:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReserveCountGroup implements Serializable {

    private Long userId;

    private String userName;

    private Long resourceGroupId;

    private String resourceGroupName;

    private String date;

    private long count;

}
