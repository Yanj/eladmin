package me.zhengjie.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yanjun
 * @date 2021-01-11 13:39
 */
@Data
@NoArgsConstructor
public class SmsVo implements Serializable {

    private List<String> mobiles;

    private String content;

    public SmsVo(String mobile, String content) {
        this.mobiles = new ArrayList<>();
        this.mobiles.add(mobile);
        this.content = content;
    }

}
