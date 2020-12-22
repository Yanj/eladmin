package me.zhengjie.modules.ptt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.ptt.domain.PatientTermReserve;
import me.zhengjie.modules.ptt.domain.PatientTermReserveResource;
import me.zhengjie.modules.ptt.service.PatientTermReserveService;
import me.zhengjie.modules.ptt.service.dto.PatientTermReserveCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 患者项目预约
 *
 * @author yanjun
 * @date 2020-12-01 14:46
 */
@Api(tags = "患者项目预约")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/ptt/patientTermReserve")
public class PatientTermReserveController {

    private final PatientTermReserveService patientTermReserveService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('patient:list')")
    public void download(HttpServletResponse response, PatientTermReserveCriteria criteria) throws IOException {
        patientTermReserveService.download(patientTermReserveService.queryAll(criteria), response);
    }

    @Log("查询患者项目预约")
    @ApiOperation("查询患者项目预约")
    @GetMapping
    @PreAuthorize("@el.check('patient:list')")
    public ResponseEntity<Object> query(PatientTermReserveCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(patientTermReserveService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("查询患者项目预约")
    @ApiOperation("查询患者项目预约")
    @GetMapping("/{id}")
    @PreAuthorize("@el.check('patient:list')")
    public ResponseEntity<Object> queryOne(@PathVariable Long id) {
        return new ResponseEntity<>(patientTermReserveService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增患者项目预约")
    @ApiOperation("新增患者项目预约")
    @PreAuthorize("@el.check('patient:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PatientTermReserve resources) {
        return new ResponseEntity<>(patientTermReserveService.create(resources), HttpStatus.CREATED);
    }

    @PostMapping("/reserve")
    @Log("新增患者项目预约")
    @ApiOperation("新增患者项目预约")
    @PreAuthorize("@el.check('patient:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PatientTermReserve[] resources) {
        return new ResponseEntity<>(patientTermReserveService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改患者项目预约")
    @ApiOperation("修改患者项目预约")
    @PreAuthorize("@el.check('patient:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody PatientTermReserve resources) {
        patientTermReserveService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/checkIn")
    @Log("患者项目预约签到")
    @ApiOperation("患者项目预约签到")
    @PreAuthorize("@el.check('patient:edit')")
    public ResponseEntity<Object> checkIn(@Validated @RequestBody PatientTermReserve resources) {
        patientTermReserveService.checkIn(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/cancel")
    @Log("患者项目预约取消")
    @ApiOperation("患者项目预约取消")
    @PreAuthorize("@el.check('patient:edit')")
    public ResponseEntity<Object> cancel(@Validated @RequestBody PatientTermReserve resources) {
        patientTermReserveService.cancel(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/verify")
    @Log("患者项目预约取消")
    @ApiOperation("患者项目预约取消")
    @PreAuthorize("@el.check('patient:edit')")
    public ResponseEntity<Object> verify(@Validated @RequestBody PatientTermReserveResource[] resources) {
        patientTermReserveService.verify(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除患者项目预约")
    @ApiOperation("删除患者项目预约")
    @PreAuthorize("@el.check('patient:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        patientTermReserveService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
