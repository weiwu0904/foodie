package com.weiwu.controller;

import com.weiwu.pojo.bo.ShopcartBO;
import com.weiwu.utils.IMOOCJSONResult;
import com.weiwu.utils.JsonUtils;
import com.weiwu.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/shopcart")
@Api(value = "购物车接口",tags = {"购物车接口相关的API"})
public class ShopcartController extends BaseController {

    @Autowired
    private RedisOperator redisOperator;


    @PostMapping("/add")
    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车",httpMethod = "POST")
    public IMOOCJSONResult add(@RequestParam String userId,
                               @RequestBody ShopcartBO shopcartBO,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }

        List<ShopcartBO> shopcartBOList = null;

//        System.out.println("shopcartBO = " + shopcartBO);
        // 前端用户在登陆的情况下，添加商品到购物车，会同时在后端同步购物车到redis
        // 当前购物车已经存在了商品，存在即累加商品数量
        String shopcartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        if (StringUtils.isNotBlank(shopcartJson)) {

            boolean isHaving = false;
            // redis 有购物车数据了
            shopcartBOList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);

            for (ShopcartBO sc  : shopcartBOList) {
                if (sc.getSpecId().equals(shopcartBO.getSpecId())) {
                    sc.setBuyCounts(sc.getBuyCounts() + shopcartBO.getBuyCounts());
                    isHaving = true;
                    break;
                }

            }
            if (!isHaving) {
                shopcartBOList.add(shopcartBO);
            }

        } else {
            //redis 没有购物车数据
            shopcartBOList = new ArrayList<>();
            shopcartBOList.add(shopcartBO);
        }

        redisOperator.set(String.format("%s:%s",FOODIE_SHOPCART, userId),JsonUtils.objectToJson(shopcartBOList),3000);

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
