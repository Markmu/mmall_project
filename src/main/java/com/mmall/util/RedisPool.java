package com.mmall.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {

    private static JedisPool jedisPool;
    // 最大连接数
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20"));
    // 最小空闲连接数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "2"));
    // 最大空闲连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "10"));
    // 在borrow一个Jedis实例时，是否进行验证操作。 true:获取的Jedis实例是一定可用的
    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));
    // 在return一个Jedis实例时，是否进行验证操作。 true:放回的Jedis实例是一定可用的
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "true"));

    private static String redisIp = PropertiesUtil.getProperty("redis.ip");

    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis.port", "6379"));

    private static void initJedis() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMinIdle(minIdle);
        config.setMaxIdle(maxIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        // 忙时阻塞
        config.setBlockWhenExhausted(true);

        jedisPool = new JedisPool(config, redisIp, redisPort, 1000*2);
    }

    static {
        initJedis();
    }

    public static Jedis getResource() {
        return jedisPool.getResource();
    }

    public static void returnResource(Jedis jedis) {
        jedisPool.returnResource(jedis);
    }

    public static void returnBrokenResource(Jedis jedis) {
        jedisPool.returnBrokenResource(jedis);
    }

}
