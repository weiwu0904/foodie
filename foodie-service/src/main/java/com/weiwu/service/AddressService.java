package com.weiwu.service;

import com.weiwu.pojo.UserAddress;
import com.weiwu.pojo.bo.AddressBO;

import java.util.List;

public interface AddressService {

    /**
     * 查询所有地址信息
     * @param userId
     * @return
     */
    List<UserAddress> queryAll(String userId);

    /**
     * 用户新增收货地址
     * @param addressBO
     */
    void addNewUserAddress(AddressBO addressBO);

    /**
     * 用户修改地址信息
     * @param addressBO
     */
    void updateUserAddress(AddressBO addressBO);

    /**
     * 删除用户地址信息
     * @param userId
     * @param addressId
     */
    void deleteUserAddress(String userId, String addressId);


    /**
     * 修改默认地址
     * @param addressId
     */
    void updateUserAddressToBeDefault(String userId, String addressId);

    /**
     * 根据用户ID和地址查询 地址信息
     * @param userId
     * @param addressId
     * @return
     */
    UserAddress queryUserAddress(String userId, String addressId);
}
