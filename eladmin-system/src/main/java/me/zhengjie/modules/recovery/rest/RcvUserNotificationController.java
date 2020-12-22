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
import me.zhengjie.modules.recovery.domain.RcvUserNotification;
import me.zhengjie.modules.recovery.service.RcvUserNotificationService;
import me.zhengjie.modules.recovery.service.dto.RcvUserNotificationQueryCriteria;
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
@Api(tags = "患者预约提醒管理")
@RequestMapping("/api/recovery/rcvUserNotification")
public class RcvUserNotificationController {

    private final RcvUserNotificationService rcvUserNotificationService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rcvUserNotification:list')")
    public void download(HttpServletResponse response, RcvUserNotificationQueryCriteria criteria) throws IOException {
        rcvUserNotificationService.download(rcvUserNotificationService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询患者预约提醒")
    @ApiOperation("查询患者预约提醒")
    @PreAuthorize("@el.check('rcvUserNotification:list')")
    public ResponseEntity<Object> query(RcvUserNotificationQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(rcvUserNotificationService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增患者预约提醒")
    @ApiOperation("新增患者预约提醒")
    @PreAuthorize("@el.check('rcvUserNotification:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RcvUserNotification resources) {
        return new ResponseEntity<>(rcvUserNotificationService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改患者预约提醒")
    @ApiOperation("修改患者预约提醒")
    @PreAuthorize("@el.check('rcvUserNotification:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody RcvUserNotification resources) {
        rcvUserNotificationService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除患者预约提醒")
    @ApiOperation("删除患者预约提醒")
    @PreAuthorize("@el.check('rcvUserNotification:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        rcvUserNotificationService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}