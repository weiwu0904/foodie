package com.weiwu.controller;

import com.weiwu.enums.YesOrNo;
import com.weiwu.pojo.Carousel;
import com.weiwu.pojo.Category;
import com.weiwu.pojo.bo.ShopcartBO;
import com.weiwu.pojo.vo.CategoryVO;
import com.weiwu.pojo.vo.NewItemsVO;
import com.weiwu.service.CarouselService;
import com.weiwu.service.CategoryService;
import com.weiwu.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/shopcart")
@Api(value = "购物车接口",tags = {"购物车接口相关的API"})
public class ShopcartController {



    @PostMapping("/add")
    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车",httpMethod = "POST")
    public IMOOCJSONResult add(@RequestParam String userId,
                               @RequestBody ShopcartBO shopcartBO,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }

        System.out.println("shopcartBO = " + shopcartBO);

        //TODO 前端用户在登陆的情况下，添加商品到购物车，会同时在后端同步购物车到redis
        return IMOOCJSONResult.ok();
    }

    @PostMapping("/del")
    @ApiOperation(value = "删除购物车的某一个商品", notes = "删除购物车的某一个商品",httpMethod = "POST")
    public IMOOCJSONResult del(@RequestParam String userId,
                               @RequestParam String itemSpecId) {

        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }

        if (StringUtils.isBlank(itemSpecId)) {
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }

        //TODO 用户在页面删除购物车商品的数据，如果用户登陆了，则需要同步删除后端redis中的购物车商品
        return IMOOCJSONResult.ok();
    }


}
