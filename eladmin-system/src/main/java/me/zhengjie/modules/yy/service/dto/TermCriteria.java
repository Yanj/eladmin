package me.zhengjie.modules.yy.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.utils.enums.YesNoEnum;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 12:49
 */
@Data
public class TermCriteria extends BaseCriteria implements Serializable {

    @Query
    private Long id;

    @Query(blurry = "code,name")
    private String blurry;

    @Query
    private String code;

    @Query
    private String name;

    @Query
    private YesNoEnum status;

}
