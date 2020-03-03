package com.weiwu.service;

import com.weiwu.pojo.Users;
import com.weiwu.pojo.bo.UserBO;

public interface UserService {

    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    boolean queryUsernameIsExsit(String username);

    /**
     * 用户注册 创建用户
     * @param userBO
     * @return
     */
    Users createUser(UserBO userBO);

    /**
     * 检索用户名和密码是否匹配，用于登陆
     * @param username
     * @param password
     * @return
     */
    Users queryUserForLogin(String username, String password);
}
