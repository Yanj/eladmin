package me.zhengjie.modules.yy.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.yy.domain.Reserve;
import me.zhengjie.modules.yy.service.ReserveService;
import me.zhengjie.modules.yy.service.dto.ReserveCountCriteria;
import me.zhengjie.modules.yy.service.dto.ReserveCriteria;
import me.zhengjie.modules.yy.service.dto.UserReserveCountCriteria;
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
@Api(tags = "预约管理")
@RequestMapping("/api/yy/reserve")
public class ReserveController {

    private final ReserveService reserveService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @AnonymousAccess
    public void download(HttpServletResponse response, ReserveCriteria criteria) throws IOException {
        reserveService.download(reserveService.queryAll(criteria), response);
    }

    @Log("查询今日预约统计")
    @ApiOperation("查询今日预约统计")
    @GetMapping("/todayCount")
    @AnonymousAccess
    public ResponseEntity<Object> queryTodayCount(ReserveCriteria criteria) {
        return new ResponseEntity<>(reserveService.queryTodayCount(criteria), HttpStatus.OK);
    }

    @Log("查询周预约统计")
    @ApiOperation("查询周预约统计")
    @GetMapping("/weekCount")
    @AnonymousAccess
    public ResponseEntity<Object> queryWeekCount(ReserveCriteria criteria) {
        return new ResponseEntity<>(reserveService.queryWeekCount(criteria), HttpStatus.OK);
    }

    @Log("查询今日工作时段预约统计")
    @ApiOperation("查询今日工作时段预约统计")
    @GetMapping("/workTimeCount")
    @AnonymousAccess
    public ResponseEntity<Object> queryWorkTimeCount(@RequestParam("deptId") Long deptId) {
        return new ResponseEntity<>(reserveService.queryTodayCountGroupByWorkTime(deptId), HttpStatus.OK);
    }

    @Log("查询工作量统计")
    @ApiOperation("查询工作量统计")
    @GetMapping("/userWorkCount")
    @AnonymousAccess
    public ResponseEntity<Object> queryUserWorkCount(UserReserveCountCriteria criteria) {
        return new ResponseEntity<>(reserveService.queryUserReserveCount(criteria), HttpStatus.OK);
    }

    @Log("查询预约")
    @ApiOperation("查询预约")
    @GetMapping
    @AnonymousAccess
    public ResponseEntity<Object> query(ReserveCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(reserveService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @ApiOperation("查询预约统计")
    @GetMapping("/termCount")
    @AnonymousAccess
    public ResponseEntity<Object> queryTermCount(ReserveCountCriteria criteria) {
        return new ResponseEntity<>(reserveService.queryTermCount(criteria.getDeptId(), criteria.getDate(), criteria.getDays()), HttpStatus.OK);
    }

    @ApiOperation("查询预约统计")
    @GetMapping("/reserveCount")
    @AnonymousAccess
    public ResponseEntity<Object> queryReserveCount(ReserveCountCriteria criteria) {
        return new ResponseEntity<>(reserveService.queryReserveCount(criteria), HttpStatus.OK);
    }

    @ApiOperation("查询预约资源")
    @GetMapping("/resources")
    @AnonymousAccess
    public ResponseEntity<Object> queryResources(@RequestParam Long id) {
        return new ResponseEntity<>(reserveService.queryResources(id), HttpStatus.OK);
    }

    @Log("预约取消")
    @ApiOperation("预约取消")
    @PostMapping("/verify")
    @AnonymousAccess
    public ResponseEntity<Object> verify(@RequestBody Reserve resources) throws Exception {
        return new ResponseEntity<>(reserveService.verify(resources), HttpStatus.OK);
    }

    @Log("预约取消")
    @ApiOperation("预约取消")
    @PostMapping("/cancel")
    @AnonymousAccess
    public ResponseEntity<Object> cancel(@RequestBody Reserve resources) throws Exception {
        return new ResponseEntity<>(reserveService.cancel(resources), HttpStatus.OK);
    }

    @Log("预约签到")
    @ApiOperation("预约签到")
    @PostMapping("/checkIn")
    @AnonymousAccess
    public ResponseEntity<Object> checkIn(@RequestBody Reserve resources) {
        return new ResponseEntity<>(reserveService.checkIn(resources), HttpStatus.OK);
    }

    @Log("新增预约")
    @ApiOperation("新增预约")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody Reserve resources) throws Exception {
        return new ResponseEntity<>(reserveService.create(resources), HttpStatus.CREATED);
    }

    @Log("新增预约")
    @ApiOperation("新增预约")
    @PostMapping("/list")
    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody Reserve[] resources) throws Exception {
        return new ResponseEntity<>(reserveService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改预约")
    @ApiOperation("修改预约")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity<Object> update(@Validated @RequestBody Reserve resources) {
        reserveService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除预约")
    @ApiOperation("删除预约")
    @DeleteMapping
    @AnonymousAccess
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        reserveService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
