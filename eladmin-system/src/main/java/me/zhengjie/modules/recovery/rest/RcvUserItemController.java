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
import me.zhengjie.modules.recovery.domain.RcvUserItem;
import me.zhengjie.modules.recovery.service.RcvUserItemService;
import me.zhengjie.modules.recovery.service.dto.RcvUserItemQueryCriteria;
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
@Api(tags = "用户套餐管理")
@RequestMapping("/api/recovery/rcvUserItem")
public class RcvUserItemController {

    private final RcvUserItemService rcvUserItemService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rcvUserItem:list')")
    public void download(HttpServletResponse response, RcvUserItemQueryCriteria criteria) throws IOException {
        rcvUserItemService.download(rcvUserItemService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询用户套餐")
    @ApiOperation("查询用户套餐")
    @PreAuthorize("@el.check('rcvUserItem:list')")
    public ResponseEntity<Object> query(RcvUserItemQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(rcvUserItemService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增用户套餐")
    @ApiOperation("新增用户套餐")
    @PreAuthorize("@el.check('rcvUserItem:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RcvUserItem resources) {
        return new ResponseEntity<>(rcvUserItemService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改用户套餐")
    @ApiOperation("修改用户套餐")
    @PreAuthorize("@el.check('rcvUserItem:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody RcvUserItem resources) {
        rcvUserItemService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除用户套餐")
    @ApiOperation("删除用户套餐")
    @PreAuthorize("@el.check('rcvUserItem:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        rcvUserItemService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}