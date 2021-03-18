package com.wall.ssm;

import com.wall.ssm.domain.User;
import com.wall.ssm.mapper.UserMapper;
import com.wall.ssm.utils.MyBatisUtil;
import lombok.Cleanup;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class App {

    public static void main(String[] args) {
        System.out.println("hello");
    }

    @Test
    public void testJUnit() {
        System.out.println("JUnit...");
    }

    @Test
    public void testInsert() {
        @Cleanup
        SqlSession sqlSession = MyBatisUtil.getSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = new User(null, "john", new BigDecimal("1300"));
        userMapper.insert(user);
        sqlSession.commit();
        System.out.println(user);
    }

    @Test

    public void testQueryById() {
        @Cleanup
        SqlSession sqlSession = MyBatisUtil.getSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        User user = userMapper.queryById(2L);
        System.out.println(user);
    }

    @Test
    public void testQueryAll() {
        @Cleanup
        SqlSession sqlSession = MyBatisUtil.getSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        userMapper.queryAll().forEach(System.out::println);
    }

    @Test
    public void tesJedis() {
        Jedis jedis = new Jedis("localhost", 6379);
        jedis.set("name", "wall");
        jedis.set("gender", "man");

        System.out.println(jedis.get("gender"));
        System.out.println(jedis.keys("*"));
    }

    @Test
    public void tesInet() throws UnknownHostException {
        InetAddress inetAddress = Inet6Address.getByName("127.0.0.1");
        InetAddress inetAddress1 = Inet6Address.getByName("www.baidu.com");
        InetAddress inetAddress2 = Inet6Address.getByName("www.google.com");
        InetAddress inetAddress3 = Inet6Address.getLocalHost();
        System.out.println(inetAddress);
        System.out.println(inetAddress1);
        System.out.println(inetAddress2);
        System.out.println(inetAddress3);
        System.out.println(Arrays.toString(inetAddress2.getAddress()));
        System.out.println(inetAddress2.getCanonicalHostName());
        System.out.println(inetAddress2.getHostName());
        System.out.println(inetAddress2.getHostAddress());
    }
}
