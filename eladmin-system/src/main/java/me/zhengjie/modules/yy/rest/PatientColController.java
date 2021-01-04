package me.zhengjie.modules.yy.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.yy.domain.PatientCol;
import me.zhengjie.modules.yy.service.PatientColService;
import me.zhengjie.modules.yy.service.dto.PatientColCriteria;
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
@Api(tags = "患者信息")
@RequestMapping("/api/yy/patientCol")
public class PatientColController {

    private final PatientColService patientColService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @AnonymousAccess
    public void download(HttpServletResponse response, PatientColCriteria criteria) throws IOException {
        patientColService.download(patientColService.queryAll(criteria), response);
    }

    @Log("查询患者信息")
    @ApiOperation("查询患者信息")
    @GetMapping
    @AnonymousAccess
    public ResponseEntity<Object> query(PatientColCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(patientColService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增患者信息")
    @ApiOperation("新增患者信息")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody PatientCol resources) {
        return new ResponseEntity<>(patientColService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改患者信息")
    @ApiOperation("修改患者信息")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity<Object> update(@Validated @RequestBody PatientCol resources) {
        patientColService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除患者信息")
    @ApiOperation("删除患者信息")
    @DeleteMapping
    @AnonymousAccess
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        patientColService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
