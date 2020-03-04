package com.weiwu.controller;


import com.weiwu.pojo.Users;
import com.weiwu.pojo.bo.UserBO;
import com.weiwu.service.UserService;
import com.weiwu.utils.CookieUtils;
import com.weiwu.utils.IMOOCJSONResult;
import com.weiwu.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "注册登陆",tags = {"用于注册登陆的相关接口"})
@RestController
@RequestMapping("/passport")
public class PassportController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在",httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username) {

        if (StringUtils.isBlank(username)) {
            return IMOOCJSONResult.errorMsg("用户名不能为空");
        }

        boolean isExsit = userService.queryUsernameIsExsit(username);

        if (isExsit) {
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }
        return IMOOCJSONResult.ok();
    }


    @ApiOperation(value = "用户注册", notes = "用户注册",httpMethod = "POST")
    @PostMapping("/regist")
    public IMOOCJSONResult regist(@RequestBody UserBO userBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getPassword();

        //0. 判断用户名和密码不能为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPassword)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }

        //1. 查询用户名是否存在
        boolean isExsit = userService.queryUsernameIsExsit(username);
        if (isExsit) {
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }
        //2. 密码长度不能小于6位
        if (StringUtils.length(password) < 6) {
            return IMOOCJSONResult.errorMsg("密码长度不能小于6");
        }

        //3. 判断2次密码是否一致
        if (!password.equals(confirmPassword)) {
            return IMOOCJSONResult.errorMsg("两次密码不一致");
        }
        //4. 实现注册
        Users userResult = userService.createUser(userBO);

        userResult = setNullProperty(userResult);

        CookieUtils.setCookie(request,response,
                "user",
                JsonUtils.objectToJson(userResult), true);

        //TODO 生成用户token，存入redis会话
        //TODO 同步购物车数据

        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户登陆", notes = "用户登陆",httpMethod = "POST")
    @PostMapping("/login")
    public IMOOCJSONResult login(@RequestBody UserBO userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        String username = userBO.getUsername();
        String password = userBO.getPassword();

        //0. 判断用户名和密码不能为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return IMOOCJSONResult.errorMsg("用户名或密码不能为空");
        }
        //1. 实现登陆
        Users userResult = userService.queryUserForLogin(username, password);

        if (userResult == null) {
            return IMOOCJSONResult.errorMsg("用户名或密码不正确");
        }

        userResult = setNullProperty(userResult);

        CookieUtils.setCookie(request,response,
                "user",
                JsonUtils.objectToJson(userResult), true);

        //TODO 生成用户token，存入redis会话
        //TODO 同步购物车数据

        return IMOOCJSONResult.ok(userResult);
    }

    @ApiOperation(value = "用户退出", notes = "用户退出",httpMethod = "POST")
    @PostMapping("/logout")
    public IMOOCJSONResult logout(@RequestParam String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {


        //1. 清除用户相关的cookie信息
        CookieUtils.deleteCookie(request,response,"user");

        //TODO: 用户退出登陆需要清空购物车
        //TODO: 分布式会话中需要清除用户数据

        return IMOOCJSONResult.ok();
    }

    private Users setNullProperty(Users users) {
        users.setPassword(null);
        users.setEmail(null);
        users.setMobile(null);
        users.setCreatedTime(null);
        users.setUpdatedTime(null);
        users.setBirthday(null);
        return users;
    }
}
