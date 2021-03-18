package com.wall.ssm.domain.Redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

public class Redis {

    @Test
    public void tesJedis() {
        Jedis jedis = new Jedis("localhost", 6379);
        System.out.println(jedis.ping());
        System.out.println(jedis.keys("*"));
    }
}
