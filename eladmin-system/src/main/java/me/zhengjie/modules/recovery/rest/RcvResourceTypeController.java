/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.recovery.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.recovery.domain.RcvResourceType;
import me.zhengjie.modules.recovery.service.RcvResourceTypeService;
import me.zhengjie.modules.recovery.service.dto.RcvResourceTypeQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yanjun
 * @website https://el-admin.vip
 * @date 2020-10-25
 **/
@RestController
@RequiredArgsConstructor
@Api(tags = "资源类型管理")
@RequestMapping("/api/recovery/rcvResourceType")
public class RcvResourceTypeController {

    private final RcvResourceTypeService rcvResourceTypeService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rcvResourceType:list')")
    public void download(HttpServletResponse response, RcvResourceTypeQueryCriteria criteria) throws IOException {
        rcvResourceTypeService.download(rcvResourceTypeService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询资源类型")
    @ApiOperation("查询资源类型")
    @PreAuthorize("@el.check('rcvResourceType:list')")
    public ResponseEntity<Object> query(RcvResourceTypeQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(rcvResourceTypeService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping("/list")
    @Log("查询资源类型")
    @ApiOperation("查询资源类型")
    @PreAuthorize("@el.check('rcvResourceType:list')")
    public ResponseEntity<Object> query(RcvResourceTypeQueryCriteria criteria) {
        return new ResponseEntity<>(rcvResourceTypeService.queryAll(criteria), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增资源类型")
    @ApiOperation("新增资源类型")
    @PreAuthorize("@el.check('rcvResourceType:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RcvResourceType resources) {
        return new ResponseEntity<>(rcvResourceTypeService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改资源类型")
    @ApiOperation("修改资源类型")
    @PreAuthorize("@el.check('rcvResourceType:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody RcvResourceType resources) {
        rcvResourceTypeService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除资源类型")
    @ApiOperation("删除资源类型")
    @PreAuthorize("@el.check('rcvResourceType:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        rcvResourceTypeService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}