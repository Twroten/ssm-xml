package com.wall.ssm.mapper;

import com.wall.ssm.domain.User;

import java.util.List;

public interface UserMapper {

    void insert(User user);

    int deleteById(Long id);

    int updateById(Long id);

    User queryById(Long id);

    List<User> queryAll();
}
