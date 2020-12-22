package me.zhengjie.modules.ptt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.ptt.service.OrgService;
import me.zhengjie.modules.ptt.service.dto.OrgCriteria;
import me.zhengjie.modules.ptt.service.dto.OrgDto;
import me.zhengjie.modules.ptt.service.mapstruct.OrgMapper;
import me.zhengjie.modules.system.domain.Dept;
import me.zhengjie.modules.system.domain.Menu;
import me.zhengjie.modules.system.service.dto.MenuDto;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 医院管理
 *
 * @author yanjun
 * @date 2020-11-30 17:20
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "医院管理")
@RequestMapping("/api/ptt/org")
public class OrgController {

    private final OrgService orgService;
    private final OrgMapper orgMapper;

    @ApiOperation("根据菜单ID返回所有子节点ID，包含自身ID")
    @GetMapping(value = "/child")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public ResponseEntity<Object> child(@RequestParam Long id) {
        Set<Dept> menuSet = new HashSet<>();
        List<OrgDto> menuList = orgService.getOrgs(id);
        menuSet.add(orgService.findOne(id));
        menuSet = orgService.getChildMenus(orgMapper.toEntity(menuList), menuSet);
        Set<Long> ids = menuSet.stream().map(Dept::getId).collect(Collectors.toSet());
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('org:list')")
    public void download(HttpServletResponse response, OrgCriteria criteria) throws IOException {
        orgService.download(orgService.queryAll(criteria), response);
    }

    @Log("查询医院信息")
    @ApiOperation("查询医院信息")
    @GetMapping("/list")
//    @PreAuthorize("@el.check('org:list')")
    @AnonymousAccess
    public ResponseEntity<Object> queryList(OrgCriteria criteria) {
        return new ResponseEntity<>(orgService.queryList(criteria), HttpStatus.OK);
    }

    @Log("查询医院信息")
    @ApiOperation("查询医院信息")
    @GetMapping
    @PreAuthorize("@el.check('org:list')")
    public ResponseEntity<Object> query(OrgCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(orgService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增医院信息")
    @ApiOperation("新增医院信息")
    @PreAuthorize("@el.check('org:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Dept resources) {
        return new ResponseEntity<>(orgService.create(resources), HttpStatus.CREATED);
    }

    @Log("修改医院信息")
    @ApiOperation("修改医院信息")
    @PutMapping
    @PreAuthorize("@el.check('org:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Dept resources) {
        orgService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除医院信息")
    @ApiOperation("删除医院信息")
    @DeleteMapping
    @PreAuthorize("@el.check('org:del')")
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        orgService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
