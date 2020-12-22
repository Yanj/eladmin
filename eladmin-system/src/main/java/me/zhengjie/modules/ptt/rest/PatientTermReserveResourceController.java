package me.zhengjie.modules.ptt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.ptt.domain.PatientTermReserveResource;
import me.zhengjie.modules.ptt.service.PatientTermReserveResourceService;
import me.zhengjie.modules.ptt.service.dto.PatientTermReserveResourceCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 患者项目预约资源管理
 *
 * @author yanjun
 * @date 2020-12-01 14:46
 */
@Api(tags = "患者项目预约资源")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/ptt/patientTermReserveResource")
public class PatientTermReserveResourceController {

    private final PatientTermReserveResourceService patientTermReserveResourceService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('patient:list')")
    public void download(HttpServletResponse response, PatientTermReserveResourceCriteria criteria) throws IOException {
        patientTermReserveResourceService.download(patientTermReserveResourceService.queryAll(criteria), response);
    }

    @Log("查询患者项目预约资源")
    @ApiOperation("查询患者项目预约资源")
    @GetMapping
    @PreAuthorize("@el.check('patient:list')")
    public ResponseEntity<Object> query(PatientTermReserveResourceCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(patientTermReserveResourceService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增患者项目预约资源")
    @ApiOperation("新增患者项目预约资源")
    @PreAuthorize("@el.check('patient:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PatientTermReserveResource resources) {
        return new ResponseEntity<>(patientTermReserveResourceService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改患者项目预约资源")
    @ApiOperation("修改患者项目预约资源")
    @PreAuthorize("@el.check('patient:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody PatientTermReserveResource resources) {
        patientTermReserveResourceService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除患者项目预约资源")
    @ApiOperation("删除患者项目预约资源")
    @PreAuthorize("@el.check('patient:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        patientTermReserveResourceService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
