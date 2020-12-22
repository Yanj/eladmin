/**
 * eladmin
 * <p>
 * Copyright 2014 Acooly.cn, Inc. All rights reserved.
 *
 * @author yanjun
 * @date 2020-10-22 17:05
 */
package me.zhengjie.modules.recovery.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.recovery.domain.HisSetting;
import me.zhengjie.modules.recovery.service.HisSettingService;
import me.zhengjie.modules.recovery.service.dto.HisSettingCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author yanjun
 * @date 2020-10-22 17:05
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "康护: HIS设置")
@RequestMapping("/api/recovery/hisSetting")
public class HisSettingController {

    private final HisSettingService hisSettingService;

    @ApiOperation("导出HIS设置数据")
    @GetMapping("/download")
    @PreAuthorize("@el.check('hisSetting:list')")
    public void download(HttpServletResponse response, HisSettingCriteria criteria) throws IOException {
        hisSettingService.download(hisSettingService.queryAll(criteria), response);
    }

    @Log("查询HIS设置")
    @ApiOperation("查询HIS设置")
    @GetMapping
    @PreAuthorize("@el.check('hisSetting:list')")
//    @AnonymousAccess
    public ResponseEntity<Object> query(HisSettingCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(hisSettingService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增HIS配置")
    @ApiOperation("新增HIS配置")
    @PostMapping
    @PreAuthorize("@el.check('hisSetting:add')")
//    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody HisSetting hisSetting) {
        hisSettingService.create(hisSetting);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Log("修改HIS配置")
    @ApiOperation("修改HIS配置")
    @PutMapping
    @PreAuthorize("@el.check('hisSetting:edit')")
//    @AnonymousAccess
    public ResponseEntity<Object> update(@Validated(HisSetting.Update.class) @RequestBody HisSetting hisSetting) {
        hisSettingService.update(hisSetting);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除HIS配置")
    @ApiOperation("删除HIS配置")
    @DeleteMapping
    @PreAuthorize("@el.check('hisSetting:del')")
//    @AnonymousAccess
    public ResponseEntity<Object> delete(@RequestBody Set<Long> ids) {
        hisSettingService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
