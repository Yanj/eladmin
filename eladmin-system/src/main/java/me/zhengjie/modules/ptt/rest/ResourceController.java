package me.zhengjie.modules.ptt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.ptt.domain.Resource;
import me.zhengjie.modules.ptt.service.ResourceService;
import me.zhengjie.modules.ptt.service.dto.ResourceCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 资源管理
 *
 * @author yanjun
 * @date 2020-11-28 16:55
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "资源管理")
@RequestMapping("/api/ptt/resource")
public class ResourceController {

    private final ResourceService resourceService;

    @Log("查询资源")
    @ApiOperation("查询资源")
    @GetMapping("/list")
    @PreAuthorize("@el.check('resource:list')")
    public ResponseEntity<Object> query(ResourceCriteria criteria) {
        return new ResponseEntity<>(resourceService.queryByDeptId(criteria), HttpStatus.OK);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('resource:list')")
    public void download(HttpServletResponse response, ResourceCriteria criteria) throws IOException {
        resourceService.download(resourceService.queryAll(criteria), response);
    }

    @Log("查询资源")
    @ApiOperation("查询资源")
    @GetMapping
    @PreAuthorize("@el.check('resource:list')")
    public ResponseEntity<Object> query(ResourceCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(resourceService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增资源")
    @ApiOperation("新增资源")
    @PreAuthorize("@el.check('resource:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Resource resources) {
        return new ResponseEntity<>(resourceService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改资源")
    @ApiOperation("修改资源")
    @PreAuthorize("@el.check('resource:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Resource resources) {
        resourceService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除资源")
    @ApiOperation("删除资源")
    @PreAuthorize("@el.check('resource:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        resourceService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
