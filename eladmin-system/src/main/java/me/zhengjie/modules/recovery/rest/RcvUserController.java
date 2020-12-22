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
import me.zhengjie.domain.vo.HisCkItemVo;
import me.zhengjie.modules.recovery.domain.RcvUser;
import me.zhengjie.modules.recovery.service.RcvUserService;
import me.zhengjie.modules.recovery.service.dto.RcvUserQueryCriteria;
import me.zhengjie.modules.security.service.dto.JwtUserDto;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
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
@Api(tags = "患者信息管理")
@RequestMapping("/api/recovery/rcvUser")
public class RcvUserController {

    private final RcvUserService rcvUserService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('rcvUser:list')")
    public void download(HttpServletResponse response, RcvUserQueryCriteria criteria) throws IOException {
        rcvUserService.download(rcvUserService.queryAll(criteria), response);
    }

    @Log("查询患者信息")
    @ApiOperation("查询患者信息")
    @GetMapping(value = "/user")
//    @PreAuthorize("@el.check('rcvUser:list')")
    @AnonymousAccess
    public ResponseEntity<Object> query(HisCkItemVo vo) throws Exception {
        // 获取当前用户部门
        UserDetails currentUser = SecurityUtils.getCurrentUser();
        return new ResponseEntity<>(rcvUserService.syncData((JwtUserDto) currentUser, vo), HttpStatus.OK);
    }

    @GetMapping
    @Log("查询患者信息")
    @ApiOperation("查询患者信息")
    @PreAuthorize("@el.check('rcvUser:list')")
    public ResponseEntity<Object> query(RcvUserQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(rcvUserService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增患者信息")
    @ApiOperation("新增患者信息")
    @PreAuthorize("@el.check('rcvUser:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody RcvUser resources) {
        return new ResponseEntity<>(rcvUserService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改患者信息")
    @ApiOperation("修改患者信息")
    @PreAuthorize("@el.check('rcvUser:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody RcvUser resources) {
        rcvUserService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除患者信息")
    @ApiOperation("删除患者信息")
    @PreAuthorize("@el.check('rcvUser:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        rcvUserService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}