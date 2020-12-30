package me.zhengjie.modules.yy.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.yy.domain.ReserveVerify;
import me.zhengjie.modules.yy.service.ReserveVerifyService;
import me.zhengjie.modules.yy.service.dto.ReserveVerifyCriteria;
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
@Api(tags = "预约核销")
@RequestMapping("/api/yy/reserveVerify")
public class ReserveVerifyController {

    private final ReserveVerifyService reserveVerifyService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @AnonymousAccess
    public void download(HttpServletResponse response, ReserveVerifyCriteria criteria) throws IOException {
        reserveVerifyService.download(reserveVerifyService.queryAll(criteria), response);
    }

    @Log("查询预约核销")
    @ApiOperation("查询预约核销")
    @GetMapping
    @AnonymousAccess
    public ResponseEntity<Object> query(ReserveVerifyCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(reserveVerifyService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增预约核销")
    @ApiOperation("新增预约核销")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody ReserveVerify resources) {
        return new ResponseEntity<>(reserveVerifyService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改预约核销")
    @ApiOperation("修改预约核销")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity<Object> update(@Validated @RequestBody ReserveVerify resources) {
        reserveVerifyService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除预约核销")
    @ApiOperation("删除预约核销")
    @DeleteMapping
    @AnonymousAccess
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        reserveVerifyService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
