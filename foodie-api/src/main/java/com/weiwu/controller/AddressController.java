package com.weiwu.controller;

import com.weiwu.pojo.UserAddress;
import com.weiwu.pojo.bo.AddressBO;
import com.weiwu.pojo.bo.ShopcartBO;
import com.weiwu.service.AddressService;
import com.weiwu.utils.IMOOCJSONResult;
import com.weiwu.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/address")
@Api(value = "地址相关", tags = {"地址相关的API接口"})
public class AddressController {

    /**
     * 用户在确认订单页面，针对地址可以如下操作
     * 查询所有收货地址
     * 新增收货地址
     * 删除收货地址
     * 修改收货地址
     * 设置默认地址
     */

    @Autowired
    private AddressService addressService;

    @PostMapping("/list")
    @ApiOperation(value = "根据用户ID查询所有收货地址", notes = "根据用户ID查询所有收货地址", httpMethod = "POST")
    public IMOOCJSONResult list(@RequestParam String userId) {

        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("用户id不能为空");
        }

        List<UserAddress> list = addressService.queryAll(userId);

        return IMOOCJSONResult.ok(list);
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增收货地址", notes = "新增收货地址", httpMethod = "POST")
    public IMOOCJSONResult add(@RequestBody AddressBO addressBO) {

        IMOOCJSONResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }

        addressService.addNewUserAddress(addressBO);
        return IMOOCJSONResult.ok();
    }


    @PostMapping("/update")
    @ApiOperation(value = "更新收货地址", notes = "更新收货地址", httpMethod = "POST")
    public IMOOCJSONResult update(@RequestBody AddressBO addressBO) {

        if (StringUtils.isBlank(addressBO.getAddressId())) {
            return IMOOCJSONResult.errorMsg("addressId不能为空");
        }

        IMOOCJSONResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }
        addressService.updateUserAddress(addressBO);
        return IMOOCJSONResult.ok();
    }

    @PostMapping("/delete")
    @ApiOperation(value = "更新收货地址", notes = "更新收货地址", httpMethod = "POST")
    public IMOOCJSONResult delete(@RequestParam String userId,
                                  @RequestParam String addressId) {

        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }
        if (StringUtils.isBlank(addressId)) {
            return IMOOCJSONResult.errorMsg("");
        }

        addressService.deleteUserAddress(userId, addressId);
        return IMOOCJSONResult.ok();
    }

    @PostMapping("/setDefalut")
    @ApiOperation(value = "用户设置默认地址", notes = "用户设置默认地址", httpMethod = "POST")
    public IMOOCJSONResult setDefalut(@RequestParam String userId,
                                      @RequestParam String addressId) {

        if (StringUtils.isBlank(userId)) {
            return IMOOCJSONResult.errorMsg("");
        }
        if (StringUtils.isBlank(addressId)) {
            return IMOOCJSONResult.errorMsg("");
        }

        addressService.updateUserAddressToBeDefault(userId, addressId);
        return IMOOCJSONResult.ok();
    }

    private IMOOCJSONResult checkAddress(AddressBO addressBO) {

        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return IMOOCJSONResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return IMOOCJSONResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return IMOOCJSONResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return IMOOCJSONResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return IMOOCJSONResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return IMOOCJSONResult.errorMsg("收货地址信息不能为空");
        }

        return IMOOCJSONResult.ok();
    }
}
