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
import me.zhengjie.modules.recovery.domain.RcvItemReserveLog;
import me.zhengjie.modules.recovery.service.RcvItemReserveLogService;
import me.zhengjie.modules.recovery.service.dto.RcvItemReserveLogQueryCriteria;
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
@Api(tags = "套餐预约记录日志管理")
@RequestMapping("/api/recovery/rcvItemReserveLog")
public class RcvItemReserveLogController {

    private final RcvItemReserveLogService rcvItemReserveLogService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rcvItemReserveLog:list')")
    public void download(HttpServletResponse response, RcvItemReserveLogQueryCriteria criteria) throws IOException {
        rcvItemReserveLogService.download(rcvItemReserveLogService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询套餐预约记录日志")
    @ApiOperation("查询套餐预约记录日志")
    @PreAuthorize("@el.check('rcvItemReserveLog:list')")
    public ResponseEntity<Object> query(RcvItemReserveLogQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(rcvItemReserveLogService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增套餐预约记录日志")
    @ApiOperation("新增套餐预约记录日志")
    @PreAuthorize("@el.check('rcvItemReserveLog:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RcvItemReserveLog resources) {
        return new ResponseEntity<>(rcvItemReserveLogService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改套餐预约记录日志")
    @ApiOperation("修改套餐预约记录日志")
    @PreAuthorize("@el.check('rcvItemReserveLog:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody RcvItemReserveLog resources) {
        rcvItemReserveLogService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除套餐预约记录日志")
    @ApiOperation("删除套餐预约记录日志")
    @PreAuthorize("@el.check('rcvItemReserveLog:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        rcvItemReserveLogService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}