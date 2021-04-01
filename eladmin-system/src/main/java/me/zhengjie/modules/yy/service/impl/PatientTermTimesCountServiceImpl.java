package me.zhengjie.modules.yy.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.modules.yy.domain.PatientTermTimesCount;
import me.zhengjie.modules.yy.repository.PatientTermRepository;
import me.zhengjie.modules.yy.service.PatientTermTimesCountService;
import me.zhengjie.modules.yy.service.dto.PatientTermTimesCountCriteria;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author yanjun
 * @date 2021-04-01 11:45
 */
@Slf4j
@Service
@AllArgsConstructor
public class PatientTermTimesCountServiceImpl implements PatientTermTimesCountService {

    private final PatientTermRepository patientTermRepository;

    @Override
    public List<PatientTermTimesCount> queryPatientTermTimesCount(PatientTermTimesCountCriteria criteria) {
        Long comId = criteria.getComId();
        JwtUserDto user = (JwtUserDto) SecurityUtils.getCurrentUser();
        if (!user.isAdmin()) {
            comId = user.getComId();
        }
        if (comId == null) {
            throw new BadRequestException("comId不能为空");
        }
        return patientTermRepository.queryPatientTermTimesCount(comId);
    }

}
