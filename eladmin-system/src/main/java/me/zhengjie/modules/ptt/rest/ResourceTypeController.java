package me.zhengjie.modules.ptt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.ptt.domain.ResourceType;
import me.zhengjie.modules.ptt.service.ResourceTypeService;
import me.zhengjie.modules.ptt.service.dto.ResourceTypeCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 资源类型管理
 *
 * @author yanjun
 * @date 2020-11-28 16:55
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "资源类型管理")
@RequestMapping("/api/ptt/resourceType")
public class ResourceTypeController {

    private final ResourceTypeService resourceTypeService;

    @Log("查询资源类型")
    @ApiOperation("查询资源类型")
    @GetMapping("/list")
    @PreAuthorize("@el.check('resourceType:list')")
    public ResponseEntity<Object> query(ResourceTypeCriteria criteria) {
        return new ResponseEntity<>(resourceTypeService.queryAll(criteria), HttpStatus.OK);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('resourceType:list')")
    public void download(HttpServletResponse response, ResourceTypeCriteria criteria) throws IOException {
        resourceTypeService.download(resourceTypeService.queryAll(criteria), response);
    }

    @Log("查询资源类型")
    @ApiOperation("查询资源类型")
    @GetMapping
    @PreAuthorize("@el.check('resourceType:list')")
    public ResponseEntity<Object> query(ResourceTypeCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(resourceTypeService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增资源类型")
    @ApiOperation("新增资源类型")
    @PreAuthorize("@el.check('resourceType:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody ResourceType resources) {
        return new ResponseEntity<>(resourceTypeService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改资源类型")
    @ApiOperation("修改资源类型")
    @PreAuthorize("@el.check('resourceType:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody ResourceType resources) {
        resourceTypeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除资源类型")
    @ApiOperation("删除资源类型")
    @PreAuthorize("@el.check('resourceType:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        resourceTypeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
