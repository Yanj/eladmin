package me.zhengjie.modules.ptt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.ptt.domain.Term;
import me.zhengjie.modules.ptt.service.TermService;
import me.zhengjie.modules.ptt.service.dto.TermCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 套餐管理
 *
 * @author yanjun
 * @date 2020-11-28 16:55
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "套餐管理")
@RequestMapping("/api/ptt/term")
public class TermController {

    private final TermService termService;

    // 查询 医院-套餐-日期-预约时段列表
    // 09:00-09:30, 1/10
    // 09:30-10:00, 1/10
    // ...
    @Log("查询套餐预约时段信息")
    @ApiOperation("查询套餐预约时段信息")
    @GetMapping("/timeList")
    @AnonymousAccess
    public ResponseEntity<Object> queryTermTimeList(TermCriteria criteria) {
        return new ResponseEntity<>(termService.queryReserveTime(criteria), HttpStatus.OK);
    }

    // 查询 医院-套餐-日期-预约列表
    // 09:00-09:30, xxx
    // 09:30-10:00, xxx
    // ...
    @Log("查询套餐预约信息")
    @ApiOperation("查询套餐预约信息")
    @GetMapping("/reserveList")
    @AnonymousAccess
    public ResponseEntity<Object> queryReserveList(TermCriteria criteria) {
        return new ResponseEntity<>(termService.queryReserveList(criteria), HttpStatus.OK);
    }

    // 新增 医院-套餐-日期-预约
    // 09:00-09:30, xxx

    // 作废 医院-套餐-日期-预约
    // 09:00-09:30, xxx

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('term:list')")
    public void download(HttpServletResponse response, TermCriteria criteria) throws IOException {
        termService.download(termService.queryAll(criteria), response);
    }

    @Log("查询套餐信息")
    @ApiOperation("查询套餐信息")
    @GetMapping
    @PreAuthorize("@el.check('term:list')")
    public ResponseEntity<Object> query(TermCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(termService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增套餐信息")
    @ApiOperation("新增套餐信息")
    @PreAuthorize("@el.check('term:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Term resources) {
        return new ResponseEntity<>(termService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改套餐信息")
    @ApiOperation("修改套餐信息")
    @PreAuthorize("@el.check('term:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Term resources) {
        termService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除套餐信息")
    @ApiOperation("删除套餐信息")
    @PreAuthorize("@el.check('term:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        termService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
