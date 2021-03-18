package com.wall.ssm;

import com.wall.ssm.domain.User;
import com.wall.ssm.mapper.UserMapper;
import com.wall.ssm.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SpringApp {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IUserService userService;

    @Test
    public void testSave(){
       User user = new User(null,"薛禅",new BigDecimal("15000"));
       userService.insert(user);
    }

    @Test
    public void testSpringMapper() throws Exception {
        userMapper.queryAll().forEach(System.out::println);
    }

    @Test
    public void testSpringService() throws Exception {
        userService.queryAll().forEach(System.out::println);
    }
}
