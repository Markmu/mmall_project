package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;


@Slf4j
public class RedisShardedPoolUtil {



    public static String setex(String key, String value, int time) {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getResource();
            result = jedis.setex(key, time, value);
        } catch (Exception e) {
            log.error("[setex key:{} time:{} value:{} error]", key, time, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
        } finally {
            RedisShardedPool.returnResource(jedis);
        }
        return result;
    }

    public static Long expire(String key, int seconds) {
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getResource();
            result = jedis.expire(key, seconds);
        } catch (Exception e) {
            log.error("[expire key:{} seconds:{} error]", key, seconds, e);
            RedisShardedPool.returnBrokenResource(jedis);
        } finally {
            RedisShardedPool.returnResource(jedis);
        }
        return result;
    }

    public static String set(String key, String value) {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getResource();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("[set key:{} value:{} error]", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
        } finally {
            RedisShardedPool.returnResource(jedis);
            return result;
        }
    }

    public static String get(String key) {
        ShardedJedis jedis = null;
        String result = null;
        try {
            jedis = RedisShardedPool.getResource();
            result = jedis.get(key);
        } catch (Exception e) {
            log.error("[get key:{} error]", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
        } finally {
            RedisShardedPool.returnResource(jedis);
            return result;
        }
    }

    public static Long del(String key) {
        ShardedJedis jedis = null;
        Long result = null;
        try {
            jedis = RedisShardedPool.getResource();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("[get key:{} error]", key, e);
            RedisShardedPool.returnBrokenResource(jedis);
        } finally {
            RedisShardedPool.returnResource(jedis);
            return result;
        }
    }
}
