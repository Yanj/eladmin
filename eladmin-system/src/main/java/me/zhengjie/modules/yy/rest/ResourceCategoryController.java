package me.zhengjie.modules.yy.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.yy.domain.ResourceCategory;
import me.zhengjie.modules.yy.service.ResourceCategoryService;
import me.zhengjie.modules.yy.service.dto.ResourceCategoryCriteria;
import me.zhengjie.modules.yy.service.dto.ResourceCategoryDto;
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
@Api(tags = "资源分类管理")
@RequestMapping("/api/yy/resourceCategory")
public class ResourceCategoryController {

    private final ResourceCategoryService resourceCategoryService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @AnonymousAccess
    public void download(HttpServletResponse response, ResourceCategoryCriteria criteria) throws IOException {
        resourceCategoryService.download(resourceCategoryService.queryAll(criteria), response);
    }

    @ApiOperation("获取单个资源分类")
    @GetMapping(value = "/{id}")
    @AnonymousAccess
    public ResponseEntity<Object> query(@PathVariable Long id) {
        return new ResponseEntity<>(resourceCategoryService.findById(id), HttpStatus.OK);
    }

    @ApiOperation("查询全部资源分类")
    @GetMapping("/lazy")
    @AnonymousAccess
    public ResponseEntity<Object> list(@RequestParam Long pid, @RequestParam Long deptId) {
        return new ResponseEntity<>(resourceCategoryService.getResourceCategories(pid, deptId), HttpStatus.OK);
    }

    @ApiOperation("根据资源分类ID返回所有子节点ID，包含自身ID")
    @GetMapping("/child")
    @AnonymousAccess
    public ResponseEntity<Object> child(@RequestParam Long id) {
        Set<Long> ids = new HashSet<>();
        ids.add(id);
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @ApiOperation("修改分组分类")
    @PutMapping(value = "/resourceGroup")
    @AnonymousAccess
    public ResponseEntity<Object> updateResourceGroup(@RequestBody ResourceCategory resources) {
        ResourceCategoryDto resourceCategory = resourceCategoryService.findById(resources.getId());
        resourceCategoryService.updateResourceGroup(resources, resourceCategory);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("查询资源分类")
    @ApiOperation("查询资源分类")
    @GetMapping("/list")
    @AnonymousAccess
    public ResponseEntity<Object> query(ResourceCategoryCriteria criteria) {
        return new ResponseEntity<>(resourceCategoryService.queryAll(criteria), HttpStatus.OK);
    }

    @Log("查询资源分类")
    @ApiOperation("查询资源分类")
    @GetMapping
    @AnonymousAccess
    public ResponseEntity<Object> query(ResourceCategoryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(resourceCategoryService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增资源分类")
    @ApiOperation("新增资源分类")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody ResourceCategory ResourceCategorys) {
        return new ResponseEntity<>(resourceCategoryService.create(ResourceCategorys), HttpStatus.CREATED);
    }

    @Log("修改资源分类")
    @ApiOperation("修改资源分类")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity<Object> update(@Validated @RequestBody ResourceCategory ResourceCategorys) {
        resourceCategoryService.update(ResourceCategorys);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除资源分类")
    @ApiOperation("删除资源分类")
    @DeleteMapping
    @AnonymousAccess
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        resourceCategoryService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
