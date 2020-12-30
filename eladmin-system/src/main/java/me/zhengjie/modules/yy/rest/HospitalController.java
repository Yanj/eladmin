package me.zhengjie.modules.yy.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.yy.domain.Hospital;
import me.zhengjie.modules.yy.service.HospitalService;
import me.zhengjie.modules.yy.service.dto.HospitalCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yanjun
 * @date 2020-12-24 21:16
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "医院管理")
@RequestMapping("/api/yy/hospital")
public class HospitalController {

    private final HospitalService hospitalService;

    @Log("查询当前用户医院")
    @ApiOperation("查询当前用户医院")
    @GetMapping("/user")
    @AnonymousAccess
    public ResponseEntity<Object> getUserAllHospitals(HospitalCriteria criteria) {
        return new ResponseEntity<>(hospitalService.querySelf(criteria), HttpStatus.OK);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @AnonymousAccess
    public void download(HttpServletResponse response, HospitalCriteria criteria) throws IOException {
        hospitalService.download(hospitalService.queryAll(criteria), response);
    }

    @Log("查询医院")
    @ApiOperation("查询医院")
    @GetMapping
    @AnonymousAccess
    public ResponseEntity<Object> query(HospitalCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(hospitalService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增医院")
    @ApiOperation("新增医院")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody Hospital resources) {
        return new ResponseEntity<>(hospitalService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改医院")
    @ApiOperation("修改医院")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity<Object> update(@Validated @RequestBody Hospital resources) {
        hospitalService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除医院")
    @ApiOperation("删除医院")
    @DeleteMapping
    @AnonymousAccess
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        hospitalService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
