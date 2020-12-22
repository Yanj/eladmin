package me.zhengjie.modules.ptt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.ptt.domain.PatientDept;
import me.zhengjie.modules.ptt.service.PatientDeptService;
import me.zhengjie.modules.ptt.service.dto.PatientDeptCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 患者医院
 *
 * @author yanjun
 * @date 2020-11-28 16:55
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "患者医院")
@RequestMapping("/api/ptt/patientDept")
public class PatientDeptController {

    private final PatientDeptService patientDeptService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('patient:list')")
    public void download(HttpServletResponse response, PatientDeptCriteria criteria) throws IOException {
        patientDeptService.download(patientDeptService.queryAll(criteria), response);
    }

    @Log("查询患者医院")
    @ApiOperation("查询患者医院")
    @GetMapping
//    @PreAuthorize("@el.check('patient:list')")
    @AnonymousAccess
    public ResponseEntity<Object> query(PatientDeptCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(patientDeptService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增患者医院")
    @ApiOperation("新增患者医院")
    @PreAuthorize("@el.check('patient:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PatientDept resources) {
        return new ResponseEntity<>(patientDeptService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改患者医院")
    @ApiOperation("修改患者医院")
    @PreAuthorize("@el.check('patient:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody PatientDept resources) {
        patientDeptService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除患者医院")
    @ApiOperation("删除患者医院")
    @PreAuthorize("@el.check('patient:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody PatientDept.PK[] ids) {
        patientDeptService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
