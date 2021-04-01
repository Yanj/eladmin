package me.zhengjie.modules.yy.service;

import me.zhengjie.modules.yy.domain.PatientTermTimesCount;
import me.zhengjie.modules.yy.service.dto.PatientTermTimesCountCriteria;

import java.util.List;

/**
 * @author yanjun
 * @date 2021-04-01 11:44
 */
public interface PatientTermTimesCountService {

    /**
     * 查询患者套餐使用情况
     *
     * @param criteria .
     * @return .
     */
    List<PatientTermTimesCount> queryPatientTermTimesCount(PatientTermTimesCountCriteria criteria);

}
