package com.xls.springbootrediscluster.service;
import com.xls.springbootrediscluster.entity.User;
import java.util.List;

public interface UserService {
    List<User> selectList();
    Integer add(User user);
    Integer delete(Integer userId);
    Integer update(User user);


}
