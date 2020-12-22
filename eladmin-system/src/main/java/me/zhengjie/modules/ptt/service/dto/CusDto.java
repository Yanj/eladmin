package me.zhengjie.modules.ptt.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;
import me.zhengjie.modules.system.service.dto.DeptDto;
import me.zhengjie.modules.system.service.dto.DictDto;

import java.io.Serializable;
import java.util.Objects;

/**
 * 自定义信息
 *
 * @author yanjun
 * @date 2020-11-28 11:09
 */
@Getter
@Setter
public class CusDto extends BaseDTO implements Serializable {

    private Long id;

    private DeptDto dept;

    private String type;

    private DictDto dict;

    private String title;

    private String status;

    private String remark;

    private Integer sort;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CusDto cusDto = (CusDto) o;

        return Objects.equals(id, cusDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
