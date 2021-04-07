package me.zhengjie.modules.yy.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.yy.service.UserWorkCountService;
import me.zhengjie.modules.yy.service.dto.UserReserveCountCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yanjun
 * @date 2021-04-01 11:38
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "用户工作量统计")
@RequestMapping("/api/yy/userWorkCount")
public class UserWorkCountController {

    private final UserWorkCountService reserveService;

    @Log("查询工作量统计")
    @ApiOperation("查询工作量统计")
    @GetMapping("/download")
    @AnonymousAccess
    public void downloadUserWorkCount(HttpServletResponse response, UserReserveCountCriteria criteria) throws IOException {
        reserveService.download(reserveService.queryUserReserveCount(criteria), response);
    }

    @Log("查询工作量统计")
    @ApiOperation("查询工作量统计")
    @GetMapping("/list")
    @AnonymousAccess
    public ResponseEntity<Object> queryUserWorkCount(UserReserveCountCriteria criteria) {
        return new ResponseEntity<>(reserveService.queryUserReserveCount(criteria), HttpStatus.OK);
    }

}
