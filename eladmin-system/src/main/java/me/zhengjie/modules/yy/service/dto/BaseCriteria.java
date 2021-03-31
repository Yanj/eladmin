package me.zhengjie.modules.yy.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2021-03-22 11:00
 */
@Data
public class BaseCriteria implements Serializable {

    @Query
    private Long orgId;

    @Query
    private Long comId;

    @Query
    private Long deptId;

    public void setUser(UserDetails user) {
        setUser((JwtUserDto) user);
    }

    public void setUser(JwtUserDto user) {
        if (null == getOrgId()) {
            setOrgId(user.getOrgId());
        }
        if (null == getComId()) {
            setComId(user.getComId());
            // 只有没有传入公司 ID 的时候才判断部门 ID
            if (null == getDeptId()) {
                setDeptId(user.getDeptId());
            }
        }
    }

}
