package me.zhengjie.modules.yy.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.yy.domain.Term;
import me.zhengjie.modules.yy.service.TermService;
import me.zhengjie.modules.yy.service.dto.ResourceGroupDto;
import me.zhengjie.modules.yy.service.dto.TermCriteria;
import me.zhengjie.modules.yy.service.dto.TermDto;
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
@Api(tags = "套餐管理")
@RequestMapping("/api/yy/term")
public class TermController {

    private final TermService termService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @AnonymousAccess
    public void download(HttpServletResponse response, TermCriteria criteria) throws IOException {
        termService.download(termService.queryAll(criteria), response);
    }

    @ApiOperation("查询单个套餐")
    @GetMapping("/{id}")
    @AnonymousAccess
    public ResponseEntity<Object> query(@PathVariable Long id) {
        return new ResponseEntity<>(termService.findById(id), HttpStatus.OK);
    }

    @ApiOperation("修改分组关联")
    @PutMapping(value = "/resourceGroup")
    @AnonymousAccess
    public ResponseEntity<Object> updateResourceGroup(@RequestBody Term resources) {
        TermDto termDto = termService.findById(resources.getId());
        termService.updateResourceCategory(resources, termDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("查询所有套餐")
    @ApiOperation("查询所有套餐")
    @GetMapping("/list")
    @AnonymousAccess
    public ResponseEntity<Object> query(TermCriteria criteria) {
        return new ResponseEntity<>(termService.queryAll(criteria), HttpStatus.OK);
    }

    @Log("查询套餐")
    @ApiOperation("查询套餐")
    @GetMapping
    @AnonymousAccess
    public ResponseEntity<Object> query(TermCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(termService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增套餐")
    @ApiOperation("新增套餐")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody Term resources) {
        return new ResponseEntity<>(termService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改套餐")
    @ApiOperation("修改套餐")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity<Object> update(@Validated @RequestBody Term resources) {
        termService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除套餐")
    @ApiOperation("删除套餐")
    @DeleteMapping
    @AnonymousAccess
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        termService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
