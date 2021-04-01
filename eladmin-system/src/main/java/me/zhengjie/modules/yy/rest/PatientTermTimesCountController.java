package me.zhengjie.modules.yy.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.yy.service.PatientTermTimesCountService;
import me.zhengjie.modules.yy.service.dto.PatientTermTimesCountCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yanjun
 * @date 2021-04-01 11:44
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "患者套餐管理")
@RequestMapping("/api/yy/patientTermTimesCount")
public class PatientTermTimesCountController {

    private final PatientTermTimesCountService patientTermTimesCountService;

    @Log("查询患者套餐使用情况")
    @ApiOperation("查询患者套餐使用情况")
    @GetMapping("/list")
    @AnonymousAccess
    public ResponseEntity<Object> queryPatientTermTimesCount(PatientTermTimesCountCriteria criteria) {
        return new ResponseEntity<>(patientTermTimesCountService.queryPatientTermTimesCount(criteria), HttpStatus.OK);
    }

}
