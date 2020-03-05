package com.weiwu.controller.center;

import com.weiwu.controller.BaseController;
import com.weiwu.pojo.Orders;
import com.weiwu.service.center.MyOrderService;
import com.weiwu.utils.IMOOCJSONResult;
import com.weiwu.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/myorders")
@Api(value = "我的订单接口", tags = "我的订单接口")
public class MyOrderController extends BaseController {


    @Autowired
    private MyOrderService myOrderService;

    @PostMapping("/query")
    @ApiOperation(value = "查询订单列表", notes = "查询订单列表", httpMethod = "POST")
    public IMOOCJSONResult query(@RequestParam String userId,
                                 @RequestParam Integer orderStatus,
                                 @RequestParam Integer page,
                                 @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("商品ID不能为空");
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = COMMENT_PAGE_SIZE;
        }

        PagedGridResult result = myOrderService.queryMyOrders(userId, orderStatus, page, pageSize);
        return IMOOCJSONResult.ok(result);
    }

    // 商家发货没有后端，所以这个接口仅仅只是用于模拟
    @ApiOperation(value = "商家发货", notes = "商家发货", httpMethod = "GET")
    @GetMapping("/deliver")
    public IMOOCJSONResult deliver(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId) throws Exception {

        if (StringUtils.isBlank(orderId)) {
            return IMOOCJSONResult.errorMsg("订单ID不能为空");
        }
        myOrderService.updateDeliverOrderStatus(orderId);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户确认收货", notes = "用户确认收货", httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public IMOOCJSONResult confirmReceive(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId) {

        IMOOCJSONResult checkResult = checkUserOrder(orderId, userId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }

        boolean result = myOrderService.updateReceiveOrderstatus(orderId);
        if (result == false) {
            IMOOCJSONResult.errorMsg("订单收货失败");
        }
        return IMOOCJSONResult.ok();
    }


    @ApiOperation(value = "用户删除订单", notes = "用户删除订单", httpMethod = "POST")
    @PostMapping("/delete")
    public IMOOCJSONResult delete(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @RequestParam String orderId,
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId) {

        IMOOCJSONResult checkResult = checkUserOrder(orderId, userId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }

        boolean result = myOrderService.deleteOrder(userId, orderId);
        if (result == false) {
            IMOOCJSONResult.errorMsg("订单删除失败");
        }
        return IMOOCJSONResult.ok();
    }

    // 检查订单是否存在，防止恶意修改
    private IMOOCJSONResult checkUserOrder(String orderId, String userId) {

        if (StringUtils.isBlank(orderId)) {
            return IMOOCJSONResult.errorMsg("订单ID不能为空");
        }

        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("用户ID不能为空");
        }

        Orders order = myOrderService.queryMyOrder(userId, orderId);
        if (order == null) {
            return IMOOCJSONResult.errorMsg("订单不存在");
        }

        return IMOOCJSONResult.ok();
    }
}
