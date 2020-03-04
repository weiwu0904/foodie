package com.weiwu.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.weiwu.enums.YesOrNo;
import com.weiwu.mapper.UserAddressMapper;
import com.weiwu.pojo.UserAddress;
import com.weiwu.pojo.bo.AddressBO;
import com.weiwu.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private Sid sid;

    @Resource
    private UserAddressMapper userAddressMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<UserAddress> queryAll(String userId) {

        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.select(userAddress);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addNewUserAddress(AddressBO addressBO) {

        // 是否为默认地址
        Integer isDefault = 0;
        //1. 判断当前是否存在地址，如果没有 该地址设置为 默认地址
        List<UserAddress> addressList = this.queryAll(addressBO.getUserId());
        if (addressList == null || addressList.size() == 0) {
            isDefault = 1;
        }

        String addressId = sid.nextShort();
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO,userAddress);
        userAddress.setId(addressId);
        userAddress.setIsDefault(isDefault);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());
        userAddressMapper.insert(userAddress);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserAddress(AddressBO addressBO) {

        String addressId = addressBO.getAddressId();

        UserAddress penddingAddress = new UserAddress();

        BeanUtils.copyProperties(addressBO,penddingAddress);
        penddingAddress.setId(addressId);
        penddingAddress.setUpdatedTime(new Date());

        userAddressMapper.updateByPrimaryKeySelective(penddingAddress);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserAddress(String userId, String addressId) {

        UserAddress userAddress = new UserAddress();
        userAddress.setId(addressId);
        userAddress.setUserId(userId);

        userAddressMapper.delete(userAddress);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserAddressToBeDefault(String userId, String addressId) {

        //1. 查找默认地址设置为不默认
        UserAddress queryAddress = new UserAddress();
        queryAddress.setUserId(userId);
        queryAddress.setIsDefault(YesOrNo.YES.type);

        List<UserAddress> userAddressList = userAddressMapper.select(queryAddress);
        for (UserAddress u : userAddressList) {
            u.setIsDefault(YesOrNo.NO.type);
            userAddressMapper.updateByPrimaryKeySelective(u);
        }

        //2. 设置当前ID的地址为默认地址
        queryAddress = new UserAddress();
        queryAddress.setId(addressId);
        queryAddress.setUserId(userId);
        queryAddress.setIsDefault(YesOrNo.YES.type);
        userAddressMapper.updateByPrimaryKeySelective(queryAddress);
    }
}
