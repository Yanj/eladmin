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
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.recovery.domain.RcvDept;
import me.zhengjie.modules.recovery.service.RcvDeptService;
import me.zhengjie.modules.recovery.service.dto.RcvDeptQueryCriteria;
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
@Api(tags = "部门管理")
@RequestMapping("/api/recovery/rcvDept")
public class RcvDeptController {

    private final RcvDeptService rcvDeptService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rcvOrg:list')")
    public void download(HttpServletResponse response, RcvDeptQueryCriteria criteria) throws IOException {
        rcvDeptService.download(rcvDeptService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询部门")
    @ApiOperation("查询部门")
    @PreAuthorize("@el.check('rcvOrg:list')")
    public ResponseEntity<Object> query(RcvDeptQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(rcvDeptService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    @Log("查询部门")
    @ApiOperation("查询部门")
    @AnonymousAccess
    public ResponseEntity<Object> queryAll(RcvDeptQueryCriteria criteria) {
        return new ResponseEntity<>(rcvDeptService.queryAll(criteria), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增部门")
    @ApiOperation("新增部门")
    @PreAuthorize("@el.check('rcvOrg:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RcvDept resources) {
        return new ResponseEntity<>(rcvDeptService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改部门")
    @ApiOperation("修改部门")
    @PreAuthorize("@el.check('rcvOrg:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody RcvDept resources) {
        rcvDeptService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除部门")
    @ApiOperation("删除部门")
    @PreAuthorize("@el.check('rcvOrg:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        rcvDeptService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}