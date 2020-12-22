package me.zhengjie.modules.ptt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.ptt.domain.PatientTermLog;
import me.zhengjie.modules.ptt.service.PatientTermLogService;
import me.zhengjie.modules.ptt.service.dto.PatientTermLogCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 患者项目日志
 *
 * @author yanjun
 * @date 2020-11-28 16:55
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "患者项目日志")
@RequestMapping("/api/ptt/patientTermLog")
public class PatientTermLogController {

    private final PatientTermLogService patientTermLogService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('patient:list')")
    public void download(HttpServletResponse response, PatientTermLogCriteria criteria) throws IOException {
        patientTermLogService.download(patientTermLogService.queryAll(criteria), response);
    }

    @Log("查询患者项目日志")
    @ApiOperation("查询患者项目日志")
    @GetMapping
    @PreAuthorize("@el.check('patient:list')")
    public ResponseEntity<Object> query(PatientTermLogCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(patientTermLogService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增患者项目日志")
    @ApiOperation("新增患者项目日志")
    @PreAuthorize("@el.check('patient:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PatientTermLog resources) {
        return new ResponseEntity<>(patientTermLogService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改患者项目日志")
    @ApiOperation("修改患者项目日志")
    @PreAuthorize("@el.check('patient:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody PatientTermLog resources) {
        patientTermLogService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除患者项目日志")
    @ApiOperation("删除患者项目日志")
    @PreAuthorize("@el.check('patient:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        patientTermLogService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
