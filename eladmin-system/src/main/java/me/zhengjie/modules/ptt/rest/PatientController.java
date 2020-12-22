package me.zhengjie.modules.ptt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.vo.HisCkItemVo;
import me.zhengjie.modules.ptt.domain.Patient;
import me.zhengjie.modules.ptt.domain.PatientWithCus;
import me.zhengjie.modules.ptt.service.PatientService;
import me.zhengjie.modules.ptt.service.dto.CusCriteria;
import me.zhengjie.modules.ptt.service.dto.PatientCriteria;
import me.zhengjie.modules.ptt.service.dto.PatientDto;
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
 * 患者管理
 *
 * @author yanjun
 * @date 2020-11-28 16:55
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "患者管理")
@RequestMapping("/api/ptt/patient")
public class PatientController {

    private final PatientService patientService;

    @ApiOperation("获取患者(不同部门)")
    @GetMapping(value = "/full")
//    @PreAuthorize("@el.check('patient:list')")
    @AnonymousAccess
    public ResponseEntity<Object> queryFull(PatientCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(patientService.findFull(criteria, pageable), HttpStatus.OK);
    }

    @ApiOperation("获取单个患者自定义数据(不同部门)")
    @GetMapping(value = "/fullCols")
//    @PreAuthorize("@el.check('patient:list')")
    @AnonymousAccess
    public ResponseEntity<Object> queryFullCols(PatientCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(patientService.findFullCols(criteria, pageable), HttpStatus.OK);
    }

    @PutMapping("/fullCols")
    @Log("修改患者信息")
    @ApiOperation("修改患者自定义数据")
//    @PreAuthorize("@el.check('patient:edit')")
    @AnonymousAccess
    public ResponseEntity<Object> updateCus(@Validated @RequestBody PatientWithCus resources) {
        patientService.updateCol(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("查询患者信息")
    @ApiOperation("查询患者信息")
    @GetMapping(value = "/query")
    @PreAuthorize("@el.check('patient:list')")
    public ResponseEntity<Object> query(HisCkItemVo vo) throws Exception {
        // 获取当前用户部门
        UserDetails currentUser = SecurityUtils.getCurrentUser();
        // 同步查询
        return new ResponseEntity<>(patientService.syncData((JwtUserDto) currentUser, vo), HttpStatus.OK);
    }

    @PutMapping("/dept")
    @Log("修改患者信息")
    @ApiOperation("修改患者信息")
    @PreAuthorize("@el.check('patient:edit')")
    public ResponseEntity<Object> updateDept(@Validated @RequestBody Patient resources) {
        PatientDto patient = patientService.findById(resources.getId());
        patientService.updateDept(resources, patient);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('patient:list')")
    public void download(HttpServletResponse response, PatientCriteria criteria) throws IOException {
        patientService.download(patientService.queryAll(criteria), response);
    }

    @Log("查询患者信息")
    @ApiOperation("查询患者信息")
    @GetMapping
//    @PreAuthorize("@el.check('patient:list')")
    @AnonymousAccess
    public ResponseEntity<Object> query(PatientCriteria criteria, Pageable pageable) {
        if (criteria.getDeptId() != null) { // 如果根据医院 id 查询, 则不分页, 查所有
            return new ResponseEntity<>(patientService.queryByDeptId(criteria.getDeptId()), HttpStatus.OK);
        }
        return new ResponseEntity<>(patientService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    /**
     * 查询患者列表
     * - 带有自定义数据
     */
    @Log("查询患者信息")
    @ApiOperation("查询患者信息")
    @GetMapping("/cols")
    @AnonymousAccess
    public ResponseEntity<Object> cols(PatientCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(patientService.queryCols(criteria, pageable), HttpStatus.OK);
    }

    /**
     * 更新患者自定义信息
     */
    @Log("更新患者信息")
    @ApiOperation("更新患者信息")
    @PutMapping("/cols")
    @AnonymousAccess
    public ResponseEntity<Object> updateCols(@RequestBody PatientDto resources) {
        patientService.updateCols(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("查询患者信息")
    @ApiOperation("查询患者信息")
    @GetMapping("/listWithDept")
    @AnonymousAccess
    public ResponseEntity<Object> listWithDept(PatientCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(patientService.queryWithDept(criteria, pageable), HttpStatus.OK);
    }

    @Log("查询患者信息")
    @ApiOperation("查询患者信息")
    @GetMapping("/listWithCus")
    @AnonymousAccess
    public ResponseEntity<Object> cus(PatientCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(patientService.queryCusByPatientId(criteria.getDeptId(), criteria.getPatientId(), pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增患者信息")
    @ApiOperation("新增患者信息")
    @PreAuthorize("@el.check('patient:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Patient resources) {
        return new ResponseEntity<>(patientService.create(resources), HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改患者信息")
    @ApiOperation("修改患者信息")
    @PreAuthorize("@el.check('patient:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Patient resources) {
        patientService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除患者信息")
    @ApiOperation("删除患者信息")
    @PreAuthorize("@el.check('patient:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        patientService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
