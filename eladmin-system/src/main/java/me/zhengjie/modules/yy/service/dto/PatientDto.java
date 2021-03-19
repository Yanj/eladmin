package me.zhengjie.modules.yy.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author yanjun
 * @date 2020-12-24 14:36
 */
@Getter
@Setter
public class PatientDto implements Serializable {

    // ID
    private Long id;

    // 组织 ID
    private Long orgId;

    // 公司 ID
    private Long comId;

    // 部门 ID
    private Long deptId;

    // 来源
    private String source;

    // 外部系统 ID
    private String code;

    // 档案号
    private String mrn;

    // 名称
    private String name;

    // 电话
    private String phone;

    // 自定义 1
    private String col1;

    // 自定义 2
    private String col2;

    // 自定义 3
    private String col3;

    // 自定义 4
    private String col4;

    // 自定义 5
    private String col5;

    // 状态
    private String status;

    // 备注
    private String remark;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PatientDto that = (PatientDto) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
