package me.zhengjie.modules.yy.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.yy.domain.Resource;
import me.zhengjie.modules.yy.service.ResourceService;
import me.zhengjie.modules.yy.service.dto.ResourceCriteria;
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
@Api(tags = "资源管理")
@RequestMapping("/api/yy/resource")
public class ResourceController {

    private final ResourceService resourceService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @AnonymousAccess
    public void download(HttpServletResponse response, ResourceCriteria criteria) throws IOException {
        resourceService.download(resourceService.queryAll(criteria), response);
    }

    @Log("查询资源")
    @ApiOperation("查询资源")
    @GetMapping
    @AnonymousAccess
    public ResponseEntity<Object> query(ResourceCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(resourceService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增资源")
    @ApiOperation("新增资源")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody Resource resources) {
        return new ResponseEntity<>(resourceService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改资源")
    @ApiOperation("修改资源")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity<Object> update(@Validated @RequestBody Resource resources) {
        resourceService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除资源")
    @ApiOperation("删除资源")
    @DeleteMapping
    @AnonymousAccess
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        resourceService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
