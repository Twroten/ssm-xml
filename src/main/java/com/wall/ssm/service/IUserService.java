package com.wall.ssm.service;

import com.wall.ssm.domain.User;

import java.util.List;

public interface IUserService {

    void insert(User user);

    User queryById(Long id);

    List<User> queryAll();

}
