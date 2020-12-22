package me.zhengjie.modules.ptt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.ptt.domain.PatientTerm;
import me.zhengjie.modules.ptt.service.PatientTermService;
import me.zhengjie.modules.ptt.service.dto.PatientTermCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 患者项目管理
 *
 * @author yanjun
 * @date 2020-11-28 16:55
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "患者项目管理")
@RequestMapping("/api/ptt/patientTerm")
public class PatientTermController {

    private final PatientTermService patientTermService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('patient:list')")
    public void download(HttpServletResponse response, PatientTermCriteria criteria) throws IOException {
        patientTermService.download(patientTermService.queryAll(criteria), response);
    }

    @Log("查询患者项目")
    @ApiOperation("查询患者项目")
    @GetMapping
    @PreAuthorize("@el.check('patient:list')")
    public ResponseEntity<Object> query(PatientTermCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(patientTermService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增患者项目")
    @ApiOperation("新增患者项目")
    @PreAuthorize("@el.check('patient:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PatientTerm resources) {
        return new ResponseEntity<>(patientTermService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改患者项目")
    @ApiOperation("修改患者项目")
    @PreAuthorize("@el.check('patient:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody PatientTerm resources) {
        patientTermService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除患者项目")
    @ApiOperation("删除患者项目")
    @PreAuthorize("@el.check('patient:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        patientTermService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
