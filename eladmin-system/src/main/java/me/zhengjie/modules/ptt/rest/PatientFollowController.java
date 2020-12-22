package me.zhengjie.modules.ptt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.ptt.domain.PatientFollow;
import me.zhengjie.modules.ptt.service.PatientFollowService;
import me.zhengjie.modules.ptt.service.dto.PatientFollowCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 患者跟进
 *
 * @author yanjun
 * @date 2020-11-28 16:55
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "患者跟进")
@RequestMapping("/api/ptt/patientFollow")
public class PatientFollowController {

    private final PatientFollowService patientFollowService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('patient:list')")
    public void download(HttpServletResponse response, PatientFollowCriteria criteria) throws IOException {
        patientFollowService.download(patientFollowService.queryAll(criteria), response);
    }

    @Log("查询患者跟进")
    @ApiOperation("查询患者跟进")
    @GetMapping
    @PreAuthorize("@el.check('patient:list')")
    public ResponseEntity<Object> query(PatientFollowCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(patientFollowService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增患者跟进")
    @ApiOperation("新增患者跟进")
    @PreAuthorize("@el.check('patient:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PatientFollow resources) {
        return new ResponseEntity<>(patientFollowService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改患者跟进")
    @ApiOperation("修改患者跟进")
    @PreAuthorize("@el.check('patient:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody PatientFollow resources) {
        patientFollowService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除患者跟进")
    @ApiOperation("删除患者跟进")
    @PreAuthorize("@el.check('patient:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        patientFollowService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
