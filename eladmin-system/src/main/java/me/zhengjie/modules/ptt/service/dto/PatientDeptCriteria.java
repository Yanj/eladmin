package me.zhengjie.modules.ptt.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

import java.io.Serializable;

/**
 * 患者部门查询
 *
 * @author yanjun
 * @date 2020-11-28 11:12
 */
@Data
public class PatientDeptCriteria implements Serializable {

    @Query
    private Long id;

}
