package me.zhengjie.modules.yy.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.yy.domain.Patient;
import me.zhengjie.modules.yy.service.PatientService;
import me.zhengjie.modules.yy.service.dto.PatientCriteria;
import me.zhengjie.modules.yy.service.dto.PatientSync;
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
@Api(tags = "患者管理")
@RequestMapping("/api/yy/patient")
public class PatientController {

    private final PatientService patientService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @AnonymousAccess
    public void download(HttpServletResponse response, PatientCriteria criteria) throws IOException {
        patientService.download(patientService.queryAll(criteria), response);
    }

    @Log("查询患者")
    @ApiOperation("查询患者")
    @GetMapping
    @AnonymousAccess
    public ResponseEntity<Object> query(PatientCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(patientService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("本地同步患者")
    @ApiOperation("本地同步患者")
    @GetMapping("/syncLocal")
    @AnonymousAccess
    public ResponseEntity<Object> syncLocal(PatientSync patientSync) throws Exception {
        return new ResponseEntity<>(patientService.syncLocal(patientSync), HttpStatus.OK);
    }

    @Log("同步患者")
    @ApiOperation("同步患者")
    @GetMapping("/sync")
    @AnonymousAccess
    public ResponseEntity<Object> sync(PatientCriteria criteria) throws Exception {
        return new ResponseEntity<>(patientService.sync(criteria), HttpStatus.OK);
    }

    @Log("新增患者")
    @ApiOperation("新增患者")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody Patient resources) {
        return new ResponseEntity<>(patientService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改患者")
    @ApiOperation("修改患者")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity<Object> update(@Validated @RequestBody Patient resources) {
        patientService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除患者")
    @ApiOperation("删除患者")
    @DeleteMapping
    @AnonymousAccess
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        patientService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
