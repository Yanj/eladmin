package me.zhengjie.modules.yy.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.yy.domain.ReserveLog;
import me.zhengjie.modules.yy.service.ReserveLogService;
import me.zhengjie.modules.yy.service.dto.ReserveLogCriteria;
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
@Api(tags = "预约日志")
@RequestMapping("/api/yy/reserveLog")
public class ReserveLogController {

    private final ReserveLogService reserveLogService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @AnonymousAccess
    public void download(HttpServletResponse response, ReserveLogCriteria criteria) throws IOException {
        reserveLogService.download(reserveLogService.queryAll(criteria), response);
    }

    @Log("查询预约日志")
    @ApiOperation("查询预约日志")
    @GetMapping
    @AnonymousAccess
    public ResponseEntity<Object> query(ReserveLogCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(reserveLogService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增预约日志")
    @ApiOperation("新增预约日志")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody ReserveLog resources) {
        return new ResponseEntity<>(reserveLogService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改预约日志")
    @ApiOperation("修改预约日志")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity<Object> update(@Validated @RequestBody ReserveLog resources) {
        reserveLogService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除预约日志")
    @ApiOperation("删除预约日志")
    @DeleteMapping
    @AnonymousAccess
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        reserveLogService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
