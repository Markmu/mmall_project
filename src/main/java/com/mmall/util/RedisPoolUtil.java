package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;


@Slf4j
public class RedisPoolUtil {


    public static String setex(String key, String value, int time) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getResource();
            jedis.setex(key, time, value);
        } catch (Exception e) {
            log.error("[setex key:{} time:{} value:{} error]", key, time, value, e);
            RedisPool.returnBrokenResource(jedis);
        } finally {
            RedisPool.returnResource(jedis);
        }
        return result;
    }

    public static Long expire(String key, int seconds) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getResource();
            jedis.expire(key, seconds);
        } catch (Exception e) {
            log.error("[expire key:{} seconds:{} error]", key, seconds, e);
            RedisPool.returnBrokenResource(jedis);
        } finally {
            RedisPool.returnResource(jedis);
        }
        return result;
    }

    public static String set(String key, String value) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            log.error("[set key:{} value:{} error]", key, value, e);
            RedisPool.returnBrokenResource(jedis);
        } finally {
            RedisPool.returnResource(jedis);
            return result;
        }
    }

    public static String get(String key) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getResource();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("[get key:{} error]", key, e);
            RedisPool.returnBrokenResource(jedis);
        } finally {
            RedisPool.returnResource(jedis);
            return result;
        }
    }

    public static Long del(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getResource();
            jedis.del(key);
        } catch (Exception e) {
            log.error("[get key:{} error]", key, e);
            RedisPool.returnBrokenResource(jedis);
        } finally {
            RedisPool.returnResource(jedis);
            return result;
        }
    }

}
