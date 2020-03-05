package com.weiwu.controller.center;

import com.weiwu.pojo.Users;
import com.weiwu.service.center.CenterUserService;
import com.weiwu.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/center")
@Api(value = "用户中心",tags = "用户中心展示的相关接口")
public class CenterController {

    @Autowired
    private CenterUserService centerUserService;

    @RequestMapping("/userInfo")
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息", httpMethod = "GET")
    public IMOOCJSONResult userInfo(@RequestParam String userId) {
        Users users = centerUserService.queryUserInfo(userId);
        return IMOOCJSONResult.ok(users);
    }
}
