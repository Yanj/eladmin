package me.zhengjie.modules.yy.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.yy.domain.PatientTerm;
import me.zhengjie.modules.yy.service.PatientTermService;
import me.zhengjie.modules.yy.service.dto.PatientTermCriteria;
import me.zhengjie.modules.yy.service.dto.PatientTermTimesCountCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yanjun
 * @date 2020-12-24 15:06
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "患者套餐管理")
@RequestMapping("/api/yy/patientTerm")
public class PatientTermController {

    private final PatientTermService patientTermService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @AnonymousAccess
    public void download(HttpServletResponse response, PatientTermCriteria criteria) throws IOException {
        patientTermService.download(patientTermService.queryAll(criteria), response);
    }

    @Log("查询患者套餐使用情况")
    @ApiOperation("查询患者套餐使用情况")
    @GetMapping("/patientTermTimesCount")
    @AnonymousAccess
    public ResponseEntity<Object> queryPatientTermTimesCount(PatientTermTimesCountCriteria criteria) {
        return new ResponseEntity<>(patientTermService.queryPatientTermTimesCount(criteria), HttpStatus.OK);
    }

    @Log("查询单个患者套餐")
    @ApiOperation("查询单个患者套餐")
    @GetMapping("/{id}")
    @AnonymousAccess
    public ResponseEntity<Object> query(@PathVariable Long id) {
        return new ResponseEntity<>(patientTermService.findById(id), HttpStatus.OK);
    }

    @Log("查询患者套餐")
    @ApiOperation("查询患者套餐")
    @GetMapping
    @AnonymousAccess
    public ResponseEntity<Object> query(PatientTermCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(patientTermService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增免费患者套餐")
    @ApiOperation("新增免费患者套餐")
    @PostMapping("/freeOne")
    @AnonymousAccess
    public ResponseEntity<Object> createFreeOne(@Validated @RequestBody PatientTerm resources) {
        return new ResponseEntity<>(patientTermService.createFreeOne(resources), HttpStatus.CREATED);
    }

    @Log("新增免费患者套餐")
    @ApiOperation("新增免费患者套餐")
    @PostMapping("/freeTwo")
    @AnonymousAccess
    public ResponseEntity<Object> createFreeTwo(@Validated @RequestBody PatientTerm resources) {
        return new ResponseEntity<>(patientTermService.createFreeTwo(resources), HttpStatus.CREATED);
    }

    @Log("新增患者套餐")
    @ApiOperation("新增患者套餐")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody PatientTerm resources) {
        return new ResponseEntity<>(patientTermService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改患者套餐")
    @ApiOperation("修改患者套餐")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity<Object> update(@Validated @RequestBody PatientTerm resources) {
        patientTermService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除患者套餐")
    @ApiOperation("删除患者套餐")
    @DeleteMapping
    @AnonymousAccess
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        patientTermService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
