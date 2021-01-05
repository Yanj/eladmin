package me.zhengjie.modules.yy.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.yy.domain.ReserveResource;
import me.zhengjie.modules.yy.service.ReserveResourceService;
import me.zhengjie.modules.yy.service.dto.ReserveResourceCriteria;
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
@Api(tags = "预约资源")
@RequestMapping("/api/yy/reserveResource")
public class ReserveResourceController {

    private final ReserveResourceService reserveResourceService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @AnonymousAccess
    public void download(HttpServletResponse response, ReserveResourceCriteria criteria) throws IOException {
        reserveResourceService.download(reserveResourceService.queryAll(criteria), response);
    }

    @Log("查询预约资源统计")
    @ApiOperation("查询预约资源统计")
    @GetMapping("/reserveCount")
    @AnonymousAccess
    public ResponseEntity<Object> reserveCount(@RequestParam("deptId") Long deptId) {
        return new ResponseEntity<>(reserveResourceService.queryReserveCount(deptId), HttpStatus.OK);
    }

    @Log("查询预约资源")
    @ApiOperation("查询预约资源")
    @GetMapping
    @AnonymousAccess
    public ResponseEntity<Object> query(ReserveResourceCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(reserveResourceService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增预约资源")
    @ApiOperation("新增预约资源")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody ReserveResource resources) {
        return new ResponseEntity<>(reserveResourceService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改预约资源")
    @ApiOperation("修改预约资源")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity<Object> update(@Validated @RequestBody ReserveResource resources) {
        reserveResourceService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除预约资源")
    @ApiOperation("删除预约资源")
    @DeleteMapping
    @AnonymousAccess
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        reserveResourceService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
