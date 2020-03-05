package com.weiwu.controller;

import com.weiwu.enums.OrderStatusEnum;
import com.weiwu.enums.PayMethod;
import com.weiwu.pojo.OrderStatus;
import com.weiwu.pojo.bo.SubmitOrderBO;
import com.weiwu.pojo.vo.MerchantOrdersVO;
import com.weiwu.pojo.vo.OrderVO;
import com.weiwu.service.OrdersService;
import com.weiwu.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/orders")
@Api(value = "订单相关", tags = {"订单相关的API接口"})
public class OrdersController extends BaseController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/create")
    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    public IMOOCJSONResult create(@RequestBody SubmitOrderBO submitOrderBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        if (submitOrderBO.getPayMethod() != PayMethod.WECHAT.type &&
                submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
            return IMOOCJSONResult.errorMsg("支付方式不支持");
        }

        //1. 创建订单
        OrderVO orderVO = ordersService.createOrder(submitOrderBO);

        //2. 创建订单以后，移除购物车中已提交的商品
        //TODO 整个redis之后，完善购物车中已结算的商品清除，并且同步到前端的cookie
        //CookieUtils.setCookie(request,response,FOODIE_SHOPCART,"",true);

        //3. 向支付中心发送当前订单，用于保存支付中心的订单信息
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);
        // 为了方便测试购买，统一改为一分钱
        merchantOrdersVO.setAmount(1);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("imoocUserId", "7994557-214438687");
        httpHeaders.add("password", "deop-w0o2-de0k-vfl3");
        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO, httpHeaders);

        ResponseEntity<IMOOCJSONResult> responseEntity = restTemplate.postForEntity(paymentServerUrl, entity, IMOOCJSONResult.class);
        IMOOCJSONResult paymentResult = responseEntity.getBody();
        if (paymentResult.getStatus() != 200) {
            IMOOCJSONResult.errorMsg("支付中心订单创建失败，请联系管理员");
        }
        return IMOOCJSONResult.ok(orderVO.getOrderId());
    }

    // 支付中心，支付完成回调
    @PostMapping("/notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        ordersService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    // 前端 轮寻查询订单状态
    @PostMapping("/getPaidOrderInfo")
    public IMOOCJSONResult getPaidOrderInfo(String orderId) {
        OrderStatus orderStatus = ordersService.queryOrderStatusInfo(orderId);
        return IMOOCJSONResult.ok(orderStatus);
    }
}
