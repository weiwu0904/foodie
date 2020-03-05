package com.weiwu.service.center;

import com.weiwu.pojo.Users;

public interface CenterUserService {

    /**
     * 根据用户ID查询用户信息
     * @param userId
     * @return
     */
    Users queryUserInfo(String userId);
}
