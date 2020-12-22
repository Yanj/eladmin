package me.zhengjie.modules.ptt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.ptt.domain.Cus;
import me.zhengjie.modules.ptt.service.CusService;
import me.zhengjie.modules.ptt.service.dto.CusCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义管理
 *
 * @author yanjun
 * @date 2020-11-30 17:20
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "自定义管理")
@RequestMapping("/api/ptt/cus")
public class CusController {

    private final CusService cusService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('cus:list')")
    public void download(HttpServletResponse response, CusCriteria criteria) throws IOException {
        cusService.download(cusService.queryAll(criteria), response);
    }

    @Log("查询自定义")
    @ApiOperation("查询自定义")
    @GetMapping("/list")
    @AnonymousAccess
//    @PreAuthorize("@el.check('cus:list')")
    public ResponseEntity<Object> list(CusCriteria criteria) {
        return new ResponseEntity<>(cusService.queryAll(criteria), HttpStatus.OK);
    }

    @Log("查询自定义")
    @ApiOperation("查询自定义")
    @GetMapping
    @AnonymousAccess
//    @PreAuthorize("@el.check('cus:list')")
    public ResponseEntity<Object> query(CusCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(cusService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增自定义")
    @ApiOperation("新增自定义")
    @PreAuthorize("@el.check('cus:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Cus resources) {
        return new ResponseEntity<>(cusService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改自定义")
    @ApiOperation("修改自定义")
    @PutMapping
    @PreAuthorize("@el.check('cus:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Cus resources) {
        cusService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除自定义")
    @ApiOperation("删除自定义")
    @DeleteMapping
    @PreAuthorize("@el.check('cus:del')")
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        cusService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
