package com.xls.springbootrediscluster.service.impl;

import com.xls.springbootrediscluster.entity.User;
import com.xls.springbootrediscluster.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserService userService;
    @Test
    public void selectList() {
        System.out.println(userService.selectList());
    }


    @Test
    public void add() {
        User user=new User(null,"xls","123456");
        System.out.println(userService.add(user));
    }

    @Test
    public void delete() {
        System.out.println(userService.delete(3));
    }

    @Test
    public void update() {
        User user=new User(3,"demo","1111");
        System.out.println(userService.update(user));
    }
}