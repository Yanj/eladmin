package me.zhengjie.modules.yy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.zhengjie.modules.system.domain.User;

import java.io.Serializable;

/**
 * 用户预约统计
 *
 * @author yanjun
 * @date 2021-03-31 16:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReserveCount implements Serializable {

    private Long userId;

    private String userName;

    private Long termId;

    private String termName;

    private String date;

    private long count;

}
