package com.sunsulei.spider.jd_spider.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class TestRedisUtil {

    public static Jedis getJedis(){
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(),"l.sunsulei.com",6379,0,"YvmFzRaQlSDDnGYybNVEj9MYQT4AkaBOEvKdDJSzKeXZTzgmjNS9BQWpvXnFKAguvZ7gNjpyn2jVv3rl");
        return jedisPool.getResource();
    }

}
