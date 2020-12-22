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
import me.zhengjie.modules.recovery.domain.RcvItemTimeSetting;
import me.zhengjie.modules.recovery.service.RcvItemTimeSettingService;
import me.zhengjie.modules.recovery.service.dto.RcvItemTimeSettingQueryCriteria;
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
@Api(tags = "套餐时长设置管理")
@RequestMapping("/api/recovery/rcvItemTimeSetting")
public class RcvItemTimeSettingController {

    private final RcvItemTimeSettingService rcvItemTimeSettingService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rcvItemTimeSetting:list')")
    public void download(HttpServletResponse response, RcvItemTimeSettingQueryCriteria criteria) throws IOException {
        rcvItemTimeSettingService.download(rcvItemTimeSettingService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询套餐时长设置")
    @ApiOperation("查询套餐时长设置")
    @PreAuthorize("@el.check('rcvItemTimeSetting:list')")
    public ResponseEntity<Object> query(RcvItemTimeSettingQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(rcvItemTimeSettingService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增套餐时长设置")
    @ApiOperation("新增套餐时长设置")
    @PreAuthorize("@el.check('rcvItemTimeSetting:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RcvItemTimeSetting resources) {
        return new ResponseEntity<>(rcvItemTimeSettingService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改套餐时长设置")
    @ApiOperation("修改套餐时长设置")
    @PreAuthorize("@el.check('rcvItemTimeSetting:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody RcvItemTimeSetting resources) {
        rcvItemTimeSettingService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除套餐时长设置")
    @ApiOperation("删除套餐时长设置")
    @PreAuthorize("@el.check('rcvItemTimeSetting:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        rcvItemTimeSettingService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}