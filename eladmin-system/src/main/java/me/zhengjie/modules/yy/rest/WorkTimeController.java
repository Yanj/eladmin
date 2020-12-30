package me.zhengjie.modules.yy.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.yy.domain.WorkTime;
import me.zhengjie.modules.yy.service.WorkTimeService;
import me.zhengjie.modules.yy.service.dto.WorkTimeCriteria;
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
@Api(tags = "工作时间")
@RequestMapping("/api/yy/workTime")
public class WorkTimeController {

    private final WorkTimeService workTimeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @AnonymousAccess
    public void download(HttpServletResponse response, WorkTimeCriteria criteria) throws IOException {
        workTimeService.download(workTimeService.queryAll(criteria), response);
    }

    @Log("查询工作时间")
    @ApiOperation("查询工作时间")
    @GetMapping
    @AnonymousAccess
    public ResponseEntity<Object> query(WorkTimeCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(workTimeService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增工作时间")
    @ApiOperation("新增工作时间")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody WorkTime resources) {
        return new ResponseEntity<>(workTimeService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改工作时间")
    @ApiOperation("修改工作时间")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity<Object> update(@Validated @RequestBody WorkTime resources) {
        workTimeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除工作时间")
    @ApiOperation("删除工作时间")
    @DeleteMapping
    @AnonymousAccess
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        workTimeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
