package com.weiwu.service.impl;

import com.weiwu.enums.Sex;
import com.weiwu.mapper.UsersMapper;
import com.weiwu.pojo.Users;
import com.weiwu.pojo.bo.UserBO;
import com.weiwu.service.UserService;
import com.weiwu.utils.DateUtil;
import com.weiwu.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    public static final String USER_FACE = "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1408233282,1483083519&fm=26&gp=0.jpg";

    @Autowired
    private Sid sid;

    @Resource
    private UsersMapper usersMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean queryUsernameIsExsit(String username) {

        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);

        Users users = usersMapper.selectOneByExample(example);
        return users != null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users createUser(UserBO userBO) {

        Users users = new Users();
        // 分布式全局唯一ID
        users.setId(sid.nextShort());
        users.setUsername(userBO.getUsername());
        try {
            users.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        users.setNickname(userBO.getUsername());
        users.setFace(USER_FACE);
        users.setSex(Sex.SECRET.type);
        users.setBirthday(DateUtil.stringToDate("1900-01-01"));
        users.setCreatedTime(new Date());
        users.setUpdatedTime(new Date());

        usersMapper.insert(users);
        return users;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserForLogin(String username, String password) {

        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        try {
            criteria.andEqualTo("password", MD5Utils.getMD5Str(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Users users = usersMapper.selectOneByExample(example);
        return users;
    }
}
