package com.weiwu;

import com.weiwu.pojo.Users;
import com.weiwu.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {FoodieApplication.class})
public class FoodieApplicationTests {

    @Autowired
    private UserService service;

    @Test
    public void test() {
        Users users = service.queryUserForLogin("imooc", "123123");
        System.out.println("users = " + users);
    }
}
