package me.zhengjie.modules.ptt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.ptt.domain.PatientCus;
import me.zhengjie.modules.ptt.service.PatientCusService;
import me.zhengjie.modules.ptt.service.dto.PatientCusCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 患者自定义
 *
 * @author yanjun
 * @date 2020-11-28 16:55
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "患者自定义")
@RequestMapping("/api/ptt/patientCus")
public class PatientCusController {

    private final PatientCusService patientCusService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('patient:list')")
    public void download(HttpServletResponse response, PatientCusCriteria criteria) throws IOException {
        patientCusService.download(patientCusService.queryAll(criteria), response);
    }

    @Log("查询患者自定义")
    @ApiOperation("查询患者自定义")
    @GetMapping
    @PreAuthorize("@el.check('patient:list')")
    public ResponseEntity<Object> query(PatientCusCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(patientCusService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增患者自定义")
    @ApiOperation("新增患者自定义")
    @PreAuthorize("@el.check('patient:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PatientCus resources) {
        return new ResponseEntity<>(patientCusService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改患者自定义")
    @ApiOperation("修改患者自定义")
    @PreAuthorize("@el.check('patient:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody PatientCus resources) {
        patientCusService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除患者自定义")
    @ApiOperation("删除患者自定义")
    @PreAuthorize("@el.check('patient:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        patientCusService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
