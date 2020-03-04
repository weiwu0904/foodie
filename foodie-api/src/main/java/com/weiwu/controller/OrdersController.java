package com.weiwu.controller;

import com.weiwu.enums.PayMethod;
import com.weiwu.pojo.bo.SubmitOrderBO;
import com.weiwu.service.OrdersService;
import com.weiwu.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@Api(value = "订单相关", tags = {"订单相关的API接口"})
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping("/create")
    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    public IMOOCJSONResult create(@RequestBody SubmitOrderBO submitOrderBO) {

        if (submitOrderBO.getPayMethod() != PayMethod.WECHAT.type &&
                submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
            return IMOOCJSONResult.errorMsg("支付方式不支持");
        }

        //1. 创建订单
        ordersService.createOrder(submitOrderBO);
        //2. 创建订单以后，移除购物车中已提交的商品
        //3. 向支付中心发送当前订单，用于保存支付中心的订单信息

        return IMOOCJSONResult.ok();
    }
}
