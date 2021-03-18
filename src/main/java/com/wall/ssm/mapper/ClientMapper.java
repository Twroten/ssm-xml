package com.wall.ssm.mapper;


import com.wall.ssm.domain.Client;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface ClientMapper {

    //Client login_1(LoginVO vo);

    Client login_2(Map<String, String> map);

    Client login_3(@Param("username") String username, @Param("password") String password);

}
