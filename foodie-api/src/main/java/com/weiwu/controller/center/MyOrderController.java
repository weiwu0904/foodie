package com.weiwu.controller.center;

import com.weiwu.controller.BaseController;
import com.weiwu.service.center.MyOrderService;
import com.weiwu.utils.IMOOCJSONResult;
import com.weiwu.utils.PagedGridResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
}
