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
import me.zhengjie.modules.recovery.domain.RcvPrinterSetting;
import me.zhengjie.modules.recovery.service.RcvPrinterSettingService;
import me.zhengjie.modules.recovery.service.dto.RcvPrinterSettingQueryCriteria;
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
@Api(tags = "打印机设置管理")
@RequestMapping("/api/recovery/rcvPrinterSetting")
public class RcvPrinterSettingController {

    private final RcvPrinterSettingService rcvPrinterSettingService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rcvPrinterSetting:list')")
    public void download(HttpServletResponse response, RcvPrinterSettingQueryCriteria criteria) throws IOException {
        rcvPrinterSettingService.download(rcvPrinterSettingService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询打印机设置")
    @ApiOperation("查询打印机设置")
    @PreAuthorize("@el.check('rcvPrinterSetting:list')")
    public ResponseEntity<Object> query(RcvPrinterSettingQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(rcvPrinterSettingService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增打印机设置")
    @ApiOperation("新增打印机设置")
    @PreAuthorize("@el.check('rcvPrinterSetting:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RcvPrinterSetting resources) {
        return new ResponseEntity<>(rcvPrinterSettingService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改打印机设置")
    @ApiOperation("修改打印机设置")
    @PreAuthorize("@el.check('rcvPrinterSetting:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody RcvPrinterSetting resources) {
        rcvPrinterSettingService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除打印机设置")
    @ApiOperation("删除打印机设置")
    @PreAuthorize("@el.check('rcvPrinterSetting:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        rcvPrinterSettingService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}