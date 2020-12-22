package me.zhengjie.modules.ptt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.ptt.domain.TermResourceType;
import me.zhengjie.modules.ptt.service.TermResourceTypeService;
import me.zhengjie.modules.ptt.service.dto.TermResourceTypeCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 套餐资源类型管理
 *
 * @author yanjun
 * @date 2020-11-28 16:55
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "套餐资源类型管理")
@RequestMapping("/api/ptt/termResourceType")
public class TermResourceTypeController {

    private final TermResourceTypeService termResourceTypeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('termResourceType:list')")
    public void download(HttpServletResponse response, TermResourceTypeCriteria criteria) throws IOException {
        termResourceTypeService.download(termResourceTypeService.queryAll(criteria), response);
    }

    @Log("查询套餐资源类型")
    @ApiOperation("查询套餐资源类型")
    @GetMapping
    @PreAuthorize("@el.check('termResourceType:list')")
    public ResponseEntity<Object> query(TermResourceTypeCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(termResourceTypeService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增套餐资源类型")
    @ApiOperation("新增套餐资源类型")
    @PreAuthorize("@el.check('termResourceType:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody TermResourceType resources) {
        return new ResponseEntity<>(termResourceTypeService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改套餐资源类型")
    @ApiOperation("修改套餐资源类型")
    @PreAuthorize("@el.check('termResourceType:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody TermResourceType resources) {
        termResourceTypeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除套餐资源类型")
    @ApiOperation("删除套餐资源类型")
    @PreAuthorize("@el.check('termResourceType:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        termResourceTypeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
