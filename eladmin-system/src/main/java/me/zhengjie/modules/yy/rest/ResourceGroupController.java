package me.zhengjie.modules.yy.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.yy.domain.ResourceGroup;
import me.zhengjie.modules.yy.service.ResourceGroupService;
import me.zhengjie.modules.yy.service.dto.ResourceGroupCriteria;
import me.zhengjie.modules.yy.service.dto.ResourceGroupDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yanjun
 * @date 2020-12-24 15:06
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "资源分组管理")
@RequestMapping("/api/yy/resourceGroup")
public class ResourceGroupController {

    private final ResourceGroupService resourceGroupService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @AnonymousAccess
    public void download(HttpServletResponse response, ResourceGroupCriteria criteria) throws IOException {
        resourceGroupService.download(resourceGroupService.queryAll(criteria), response);
    }

    @Log("查询套餐对应资源分组")
    @ApiOperation("查询套餐对应资源分组")
    @GetMapping("/term")
    @AnonymousAccess
    public ResponseEntity<Object> query(@RequestParam Long deptId, @RequestParam Long termId) {
        return new ResponseEntity<>(resourceGroupService.queryByDeptIdAndTermId(deptId, termId), HttpStatus.OK);
    }

    @ApiOperation("获取单个资源分组")
    @GetMapping(value = "/{id}")
    @AnonymousAccess
    public ResponseEntity<Object> query(@PathVariable Long id) {
        return new ResponseEntity<>(resourceGroupService.findById(id), HttpStatus.OK);
    }

    @ApiOperation("查询全部资源分组")
    @GetMapping("/lazy")
    @AnonymousAccess
    public ResponseEntity<Object> list(@RequestParam Long pid, @RequestParam Long deptId) {
        return new ResponseEntity<>(resourceGroupService.getResourceGroups(pid, deptId), HttpStatus.OK);
    }

    @ApiOperation("根据资源分组ID返回所有子节点ID，包含自身ID")
    @GetMapping("/child")
    @AnonymousAccess
    public ResponseEntity<Object> child(@RequestParam Long id) {
        Set<Long> ids = new HashSet<>();
        ids.add(id);
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @ApiOperation("修改分组分类")
    @PutMapping(value = "/resourceCategory")
    @AnonymousAccess
    public ResponseEntity<Object> updateResourceCategory(@RequestBody ResourceGroup resources) {
        ResourceGroupDto resourceGroupDto = resourceGroupService.findById(resources.getId());
        resourceGroupService.updateResourceCategory(resources, resourceGroupDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("查询资源分组")
    @ApiOperation("查询资源分组")
    @GetMapping("/list")
    @AnonymousAccess
    public ResponseEntity<Object> query(ResourceGroupCriteria criteria) {
        return new ResponseEntity<>(resourceGroupService.queryAll(criteria), HttpStatus.OK);
    }

    @Log("查询资源分组")
    @ApiOperation("查询资源分组")
    @GetMapping
    @AnonymousAccess
    public ResponseEntity<Object> query(ResourceGroupCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(resourceGroupService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增资源分组")
    @ApiOperation("新增资源分组")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody ResourceGroup ResourceGroups) {
        return new ResponseEntity<>(resourceGroupService.create(ResourceGroups), HttpStatus.CREATED);
    }

    @Log("修改资源分组")
    @ApiOperation("修改资源分组")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity<Object> update(@Validated @RequestBody ResourceGroup ResourceGroups) {
        resourceGroupService.update(ResourceGroups);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除资源分组")
    @ApiOperation("删除资源分组")
    @DeleteMapping
    @AnonymousAccess
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        resourceGroupService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
