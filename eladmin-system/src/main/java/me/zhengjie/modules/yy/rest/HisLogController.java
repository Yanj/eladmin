package me.zhengjie.modules.yy.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.yy.domain.HisLog;
import me.zhengjie.modules.yy.service.HisLogService;
import me.zhengjie.modules.yy.service.dto.HisLogCriteria;
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
@Api(tags = "HIS日志")
@RequestMapping("/api/yy/hisLog")
public class HisLogController {

    private final HisLogService hisLogService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @AnonymousAccess
    public void download(HttpServletResponse response, HisLogCriteria criteria) throws IOException {
        hisLogService.download(hisLogService.queryAll(criteria), response);
    }

    @Log("查询HIS日志")
    @ApiOperation("查询HIS日志")
    @GetMapping("/list")
    @AnonymousAccess
    public ResponseEntity<Object> query(HisLogCriteria criteria) {
        return new ResponseEntity<>(hisLogService.queryAll(criteria), HttpStatus.OK);
    }

    @Log("查询HIS日志")
    @ApiOperation("查询HIS日志")
    @GetMapping
    @AnonymousAccess
    public ResponseEntity<Object> query(HisLogCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(hisLogService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增HIS日志")
    @ApiOperation("新增HIS日志")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody HisLog resources) {
        return new ResponseEntity<>(hisLogService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改HIS日志")
    @ApiOperation("修改HIS日志")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity<Object> update(@Validated @RequestBody HisLog resources) {
        hisLogService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除HIS日志")
    @ApiOperation("删除HIS日志")
    @DeleteMapping
    @AnonymousAccess
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        hisLogService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
