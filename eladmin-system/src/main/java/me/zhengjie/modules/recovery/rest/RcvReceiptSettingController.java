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
import me.zhengjie.modules.recovery.domain.RcvReceiptSetting;
import me.zhengjie.modules.recovery.service.RcvReceiptSettingService;
import me.zhengjie.modules.recovery.service.dto.RcvReceiptSettingQueryCriteria;
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
@Api(tags = "小票设置管理")
@RequestMapping("/api/recovery/rcvReceiptSetting")
public class RcvReceiptSettingController {

    private final RcvReceiptSettingService rcvReceiptSettingService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rcvReceiptSetting:list')")
    public void download(HttpServletResponse response, RcvReceiptSettingQueryCriteria criteria) throws IOException {
        rcvReceiptSettingService.download(rcvReceiptSettingService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询小票设置")
    @ApiOperation("查询小票设置")
    @PreAuthorize("@el.check('rcvReceiptSetting:list')")
    public ResponseEntity<Object> query(RcvReceiptSettingQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(rcvReceiptSettingService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增小票设置")
    @ApiOperation("新增小票设置")
    @PreAuthorize("@el.check('rcvReceiptSetting:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RcvReceiptSetting resources) {
        return new ResponseEntity<>(rcvReceiptSettingService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改小票设置")
    @ApiOperation("修改小票设置")
    @PreAuthorize("@el.check('rcvReceiptSetting:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody RcvReceiptSetting resources) {
        rcvReceiptSettingService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除小票设置")
    @ApiOperation("删除小票设置")
    @PreAuthorize("@el.check('rcvReceiptSetting:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        rcvReceiptSettingService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}