package com.wall.ssm.service.impl;

import com.wall.ssm.domain.User;
import com.wall.ssm.mapper.UserMapper;
import com.wall.ssm.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void insert(User user) {
        userMapper.insert(user);
        //测试事务
        //int a = 1 / 0;
    }

    @Override
    public User queryById(Long id) {
        return userMapper.queryById(id);
    }

    @Override
    public List<User> queryAll() {
        return userMapper.queryAll();
    }
}
