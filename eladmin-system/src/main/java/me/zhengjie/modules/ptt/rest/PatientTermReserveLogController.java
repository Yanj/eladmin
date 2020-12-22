package me.zhengjie.modules.ptt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.ptt.domain.PatientTermReserveLog;
import me.zhengjie.modules.ptt.service.PatientTermReserveLogService;
import me.zhengjie.modules.ptt.service.dto.PatientTermReserveLogCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 患者项目预约日志
 *
 * @author yanjun
 * @date 2020-12-01 14:46
 */
@Api(tags = "患者项目预约日志")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/ptt/patientTermReserveLog")
public class PatientTermReserveLogController {

    private final PatientTermReserveLogService patientTermReserveLogService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('patient:list')")
    public void download(HttpServletResponse response, PatientTermReserveLogCriteria criteria) throws IOException {
        patientTermReserveLogService.download(patientTermReserveLogService.queryAll(criteria), response);
    }

    @Log("查询患者项目预约日志")
    @ApiOperation("查询患者项目预约日志")
    @GetMapping
    @PreAuthorize("@el.check('patient:list')")
    public ResponseEntity<Object> query(PatientTermReserveLogCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(patientTermReserveLogService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增患者项目预约日志")
    @ApiOperation("新增患者项目预约日志")
    @PreAuthorize("@el.check('patient:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PatientTermReserveLog resources) {
        return new ResponseEntity<>(patientTermReserveLogService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改患者项目预约日志")
    @ApiOperation("修改患者项目预约日志")
    @PreAuthorize("@el.check('patient:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody PatientTermReserveLog resources) {
        patientTermReserveLogService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除患者项目预约")
    @ApiOperation("删除患者项目预约")
    @PreAuthorize("@el.check('patient:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        patientTermReserveLogService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
