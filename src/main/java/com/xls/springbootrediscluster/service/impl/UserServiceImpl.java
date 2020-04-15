package com.xls.springbootrediscluster.service.impl;

import com.xls.springbootrediscluster.entity.User;
import com.xls.springbootrediscluster.mapper.UserMapper;
import com.xls.springbootrediscluster.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public List<User> selectList() {
        //1. 先查redis中有没有用户列表的数据，如果有就直接显示redis中的数据
        //2. 如果没有就查MySQL 中的用户数据，然后写入到redis中，并返回给控制器
        //（实际生产：会做缓存预热，【预先查MySQL中的数据，存到redis中】）
        List<User> users =  redisTemplate.opsForList().range(("users_key"),0,-1);
        System.out.println("从redis中查到的users: "+users);
        if(users.size()==0){
            users = userMapper.selectList(null);
            System.out.println("从mysql中查到的users: "+users);
            redisTemplate.opsForList().leftPushAll("users_key",users);
        }
        return users;
    }

    @Override
    public Integer add(User user) {
        Integer result=userMapper.insert(user);
        if (result>0){
            User u=userMapper.selectById(user.getUserId());
            redisTemplate.opsForList().rightPushAll("users_key",u);
        }
        return result;
    }

    @Override
    public Integer delete(Integer userId) {
        User u=userMapper.selectById(userId);
        Integer result=userMapper.deleteById(userId);
        if (result>0){
            redisTemplate.opsForList().remove("users_key",0,u);
        }
        return result;
    }

    @Override
    public Integer update(User user) {
        Integer result=userMapper.updateById(user);
        if (result>0){
            List<User> users = userMapper.selectList(null);
            redisTemplate.delete("users_key");
            redisTemplate.opsForList().rightPushAll("users_key",users);
        }
        return result;
    }

}

