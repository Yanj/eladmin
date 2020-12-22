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
import me.zhengjie.modules.recovery.domain.RcvOrg;
import me.zhengjie.modules.recovery.service.RcvOrgService;
import me.zhengjie.modules.recovery.service.dto.RcvOrgQueryCriteria;
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
@Api(tags = "机构管理")
@RequestMapping("/api/recovery/rcvOrg")
public class RcvOrgController {

    private final RcvOrgService rcvOrgService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rcvOrg:list')")
    public void download(HttpServletResponse response, RcvOrgQueryCriteria criteria) throws IOException {
        rcvOrgService.download(rcvOrgService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询机构")
    @ApiOperation("查询机构")
    @PreAuthorize("@el.check('rcvOrg:list')")
    public ResponseEntity<Object> query(RcvOrgQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(rcvOrgService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    @Log("查询机构")
    @ApiOperation("查询机构")
    @AnonymousAccess
    public ResponseEntity<Object> queryAll(RcvOrgQueryCriteria criteria) {
        return new ResponseEntity<>(rcvOrgService.queryAll(criteria), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增机构")
    @ApiOperation("新增机构")
    @PreAuthorize("@el.check('rcvOrg:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RcvOrg resources) {
        return new ResponseEntity<>(rcvOrgService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改机构")
    @ApiOperation("修改机构")
    @PreAuthorize("@el.check('rcvOrg:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody RcvOrg resources) {
        rcvOrgService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除机构")
    @ApiOperation("删除机构")
    @PreAuthorize("@el.check('rcvOrg:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        rcvOrgService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}