package me.zhengjie.modules.yy.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.yy.domain.PatientTermLog;
import me.zhengjie.modules.yy.service.PatientTermLogService;
import me.zhengjie.modules.yy.service.dto.PatientTermLogCriteria;
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
@Api(tags = "患者套餐日志")
@RequestMapping("/api/yy/patientTermLog")
public class PatientTermLogController {

    private final PatientTermLogService patientTermLogService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @AnonymousAccess
    public void download(HttpServletResponse response, PatientTermLogCriteria criteria) throws IOException {
        patientTermLogService.download(patientTermLogService.queryAll(criteria), response);
    }

    @Log("查询患者套餐日志")
    @ApiOperation("查询患者套餐日志")
    @GetMapping
    @AnonymousAccess
    public ResponseEntity<Object> query(PatientTermLogCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(patientTermLogService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增患者套餐日志")
    @ApiOperation("新增患者套餐日志")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody PatientTermLog resources) {
        return new ResponseEntity<>(patientTermLogService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改患者套餐日志")
    @ApiOperation("修改患者套餐日志")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity<Object> update(@Validated @RequestBody PatientTermLog resources) {
        patientTermLogService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除患者套餐日志")
    @ApiOperation("删除患者套餐日志")
    @DeleteMapping
    @AnonymousAccess
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        patientTermLogService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
