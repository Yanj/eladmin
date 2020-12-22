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
import me.zhengjie.modules.recovery.domain.RcvHisLog;
import me.zhengjie.modules.recovery.service.RcvHisLogService;
import me.zhengjie.modules.recovery.service.dto.RcvHisLogQueryCriteria;
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
@Api(tags = "HIS查询日志管理")
@RequestMapping("/api/recovery/rcvHisLog")
public class RcvHisLogController {

    private final RcvHisLogService rcvHisLogService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rcvHisLog:list')")
    public void download(HttpServletResponse response, RcvHisLogQueryCriteria criteria) throws IOException {
        rcvHisLogService.download(rcvHisLogService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询HIS查询日志")
    @ApiOperation("查询HIS查询日志")
    @PreAuthorize("@el.check('rcvHisLog:list')")
    public ResponseEntity<Object> query(RcvHisLogQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(rcvHisLogService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增HIS查询日志")
    @ApiOperation("新增HIS查询日志")
    @PreAuthorize("@el.check('rcvHisLog:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RcvHisLog resources) {
        return new ResponseEntity<>(rcvHisLogService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改HIS查询日志")
    @ApiOperation("修改HIS查询日志")
    @PreAuthorize("@el.check('rcvHisLog:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody RcvHisLog resources) {
        rcvHisLogService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除HIS查询日志")
    @ApiOperation("删除HIS查询日志")
    @PreAuthorize("@el.check('rcvHisLog:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        rcvHisLogService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}