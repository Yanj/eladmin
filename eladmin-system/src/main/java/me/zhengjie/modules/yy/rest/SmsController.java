package me.zhengjie.modules.yy.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.yy.domain.Sms;
import me.zhengjie.modules.yy.service.SmsService;
import me.zhengjie.modules.yy.service.dto.SmsCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yanjun
 * @date 2020-12-24 15:06
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "短信发送")
@RequestMapping("/api/yy/sms")
public class SmsController {

    private final SmsService smsService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @AnonymousAccess
    public void download(HttpServletResponse response, SmsCriteria criteria) throws IOException {
        smsService.download(smsService.queryAll(criteria), response);
    }

    @Log("查询短信发送")
    @ApiOperation("查询短信发送")
    @GetMapping
    @AnonymousAccess
    public ResponseEntity<Object> query(SmsCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(smsService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @Log("新增短信发送")
    @ApiOperation("新增短信发送")
    @PostMapping
    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody Sms resources) {
        return new ResponseEntity<>(smsService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改短信发送")
    @ApiOperation("修改短信发送")
    @PutMapping
    @AnonymousAccess
    public ResponseEntity<Object> update(@Validated @RequestBody Sms resources) {
        smsService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除短信发送")
    @ApiOperation("删除短信发送")
    @DeleteMapping
    @AnonymousAccess
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        smsService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
