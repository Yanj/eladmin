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
import me.zhengjie.modules.recovery.domain.RcvResource;
import me.zhengjie.modules.recovery.service.RcvResourceService;
import me.zhengjie.modules.recovery.service.dto.RcvResourceQueryCriteria;
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
@Api(tags = "资源管理")
@RequestMapping("/api/recovery/rcvResource")
public class RcvResourceController {

    private final RcvResourceService rcvResourceService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rcvResource:list')")
    public void download(HttpServletResponse response, RcvResourceQueryCriteria criteria) throws IOException {
        rcvResourceService.download(rcvResourceService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询资源")
    @ApiOperation("查询资源")
    @PreAuthorize("@el.check('rcvResource:list')")
    public ResponseEntity<Object> query(RcvResourceQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(rcvResourceService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping("/list")
    @Log("查询资源")
    @ApiOperation("查询资源")
    @PreAuthorize("@el.check('rcvResource:list')")
    public ResponseEntity<Object> query(RcvResourceQueryCriteria criteria) {
        return new ResponseEntity<>(rcvResourceService.queryAll(criteria), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增资源")
    @ApiOperation("新增资源")
    @PreAuthorize("@el.check('rcvResource:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RcvResource resources) {
        return new ResponseEntity<>(rcvResourceService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改资源")
    @ApiOperation("修改资源")
    @PreAuthorize("@el.check('rcvResource:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody RcvResource resources) {
        rcvResourceService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除资源")
    @ApiOperation("删除资源")
    @PreAuthorize("@el.check('rcvResource:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        rcvResourceService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}