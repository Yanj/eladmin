package me.zhengjie.modules.yy.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.yy.service.WorkTimeReserveListService;
import me.zhengjie.modules.yy.service.dto.WorkTimeCriteria;
import me.zhengjie.modules.yy.service.dto.WorkTimeReserveListCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yanjun
 * @date 2021-04-01 10:30
 */

@RestController
@RequiredArgsConstructor
@Api(tags = "预约统计")
@RequestMapping("/api/yy/workTimeReserveList")
public class WorkTimeReserveListController {

    private final WorkTimeReserveListService workTimeReserveListService;

    @Log("查询工作时段")
    @ApiOperation("查询工作时段")
    @GetMapping("/workTimeList")
    @AnonymousAccess
    public ResponseEntity<Object> queryWorkTimeList(WorkTimeReserveListCriteria criteria) {
        return new ResponseEntity<>(workTimeReserveListService.queryWorkTimeList(criteria), HttpStatus.OK);
    }

    @Log("查询工作时段预约统计")
    @ApiOperation("查询工作时段预约统计")
    @GetMapping("/reserveList")
    @AnonymousAccess
    public ResponseEntity<Object> queryWorkTimeReserveList(WorkTimeReserveListCriteria criteria) {
        return new ResponseEntity<>(workTimeReserveListService.queryWorkTimeReserveList(criteria), HttpStatus.OK);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @AnonymousAccess
    public void download(HttpServletResponse response, WorkTimeReserveListCriteria criteria) throws IOException {
        workTimeReserveListService.download(workTimeReserveListService.queryWorkTimeList(criteria),
                workTimeReserveListService.queryWorkTimeReserveList(criteria), true, response);
    }

}
