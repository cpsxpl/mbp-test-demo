package com.mbp.eng.framework.cache.redis;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mbp.eng.framework.common.util.date.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Tuple;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service("redisClient")
public class RedisClient {
    private Logger logger = LoggerFactory.getLogger(RedisClient.class);

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private int maxWait;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;

    public static final String LOCK_SUCCESS = "OK"; // 获取分布式锁返回
    public static final Long LOCK_RELEASE_SUCCESS = 1L;
    public static final String SET_IF_NOT_EXIST = "NX";
    public static final String SET_WITH_EXPIRE_TIME = "PX";

    private ShardedJedisPool shardedJedisPool;

    @PostConstruct
    public void init() {
        // 根据配置文件,创建shared池实例
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(minIdle);

        JedisShardInfo jedisShardInfo = new JedisShardInfo(host, port);
        if (StringUtils.isNotEmpty(password)) {
            jedisShardInfo.setPassword(password);
        }
        List<JedisShardInfo> list = new LinkedList<>();
        list.add(jedisShardInfo);

        shardedJedisPool = new ShardedJedisPool(jedisPoolConfig, list);
    }

    public String get(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            String value = shardedJedis.get(key);
            logger.debug("RedisClient get, key {}, value {}.", key, value);
            if (StringUtils.isNotEmpty(value)) {
                return URLDecoder.decode(value, "utf-8");
            }
        } catch (Throwable t) {
            logger.error("RedisClient get is error!", t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
        return "";
    }

    public List<String> mget(String... keys) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            List<String> list = new ArrayList<String>();

            for (String key : keys) {
                String value = shardedJedis.get(key);

                if (StringUtils.isNotEmpty(value)) {
                    value = URLDecoder.decode(value, "utf-8");
                }

                list.add(value);
            }
            logger.debug("RedisClient get, key {}, value {}.", keys, list);
            return list;
        } catch (Throwable t) {
            logger.error("RedisClient get is error!", t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }

        return null;
    }

    public void set(String key, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            logger.debug("RedisClient set, key {}, value {}.", key, value);
            shardedJedis.set(key, URLEncoder.encode(value, "utf-8"));
        } catch (Throwable t) {
            logger.error("RedisClient set is error!", t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    /**
     * 支持过期时间的设置
     *
     * @param key
     * @param seconds
     * @param value
     */
    public void set(String key, String value, int seconds) {
        try {
            setex(key, seconds, value);
        } catch (Throwable t) {
            logger.error("RedisClient set expire value is error!", t);
        }
    }

    public Long delete(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            logger.debug("RedisClient delete, key {}.", key);
            return shardedJedis.del(key);
        } catch (Throwable t) {
            logger.error("RedisClient delete is error!", t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
        return -1L;
    }

    public boolean exists(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            logger.debug("RedisClient delete, key {}.", key);
            return shardedJedis.exists(key);
        } catch (Throwable t) {
            logger.error("RedisClient delete is error!", t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
        return false;
    }

    public boolean hexists(String key, String field) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            logger.debug("hexists, key {}.", key);
            return shardedJedis.hexists(key, field);
        } catch (Throwable t) {
            logger.error("RedisClient delete is error!", t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
        return false;
    }

    public List<String> hvals(String key) {
        if (StringUtils.isEmpty(key)) {
            return Lists.newArrayList();
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            logger.debug("hvals, key {}.", key);
            return shardedJedis.hvals(key);
        } catch (Throwable t) {
            logger.error("RedisClient delete is error!", t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
        return Lists.newArrayList();
    }

    /**
     * 支持过期
     *
     * @param key
     * @param secend 过期时间，置0为即时失效
     * @return
     */
    public Long expire(String key, Integer secend) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            logger.debug("RedisClient expire, key {}.", key);
            return shardedJedis.expire(key, secend);
        } catch (Throwable t) {
            logger.error("RedisClient expire is error!", t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
        return -1L;
    }

    public boolean hset(String key, String field, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            // logger.debug("RedisClient hset, key {}. field {}. value {}", key,
            // field,
            // value);
            Long result = shardedJedis.hset(key, field, URLEncoder.encode(value, "utf-8"));
            return result > 0;
        } catch (Throwable t) {
            logger.error("RedisClient hset is error!", t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
        return false;
    }

    public String hget(String key, String field) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            String value = shardedJedis.hget(key, field);
            logger.debug("RedisClient hget, key {}. field {}. value {}", key, field, value);
            if (StringUtils.isNotEmpty(value)) {
                return URLDecoder.decode(value, "utf-8");
            }
        } catch (Throwable t) {
            logger.error("RedisClient hget is error!", t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
        return "";
    }

    public List<String> hmget(String key, String... field) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            logger.debug("RedisClient hmget, key {}. field {}", key, field);
            List<String> value = shardedJedis.hmget(key, field);
            return value;
        } catch (Throwable t) {
            logger.error("RedisClient hget is error!", t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
        return Lists.newArrayList();
    }

    public boolean hdel(String key, String... field) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            Long result = shardedJedis.hdel(key, field);
            logger.debug("RedisClient hdel, key:{},field:{},result:{}", key, field, result);
            return result > 0;
        } catch (Throwable t) {
            logger.error("RedisClient hdel is error!", t);
        }
        return false;
    }

    public Map<String, String> hgetAll(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            Map<String, String> value = shardedJedis.hgetAll(key);
            logger.debug("RedisClient hgetAll, key {}.", key);
            Map<String, String> result = Maps.newHashMap();
            value.forEach((k, v) -> {
                try {
                    result.put(k, URLDecoder.decode(v, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            });
            return result;
        } catch (Throwable t) {
            logger.error("RedisClient hgetAll is error!", t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
        return Maps.newHashMap();
    }

    public Set<Object> zrange(final String key, final long start, final long stop) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            Set<Tuple> value = shardedJedis.zrangeWithScores(key, start, stop);
            logger.debug(" zrange, key {}.", key);
            Set<Object> result = Sets.newHashSet();
            value.forEach(t -> {
                result.add(t.getElement());
            });
            return result;
        } catch (Throwable t) {
            logger.error("RedisClient hgetAll is error!", t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
        return Sets.newHashSet();
    }

    public boolean hmset(String key, Map<String, String> strMap) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            // logger.debug("RedisClient hset, key {}. field {}. value {}", key,
            // field,
            // value);
            String statusCode = shardedJedis.hmset(key, strMap);
            if ("ok".equalsIgnoreCase(statusCode)) {
                return true;
            }
            return false;
        } catch (Throwable t) {
            logger.error("RedisClient hset is error!", t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
        return false;
    }

    /**
     * Set value with given expire time in seconds.
     *
     * @param key
     * @param seconds
     * @param value
     */
    public void setex(String key, int seconds, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            logger.debug("RedisClient setex, key {}, value {}.", key, value);
            shardedJedis.setex(key, seconds, URLEncoder.encode(value, "utf-8"));
        } catch (Throwable t) {
            logger.error("RedisClient setex is error!", t);
            throw new RuntimeException(t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    /**
     * Set value with given expire time in seconds.
     *
     * @param key
     * @param value
     */
    public boolean setnx(String key, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            logger.debug("RedisClient setnx, key {}, value {}.", key, value);
            long rs = shardedJedis.setnx(key, URLEncoder.encode(value, "utf-8"));
            return rs > 0;
        } catch (Throwable t) {
            logger.error("RedisClient setnx is error!", t);
            throw new RuntimeException(t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    /**
     * add to Set
     *
     * @param key
     * @param members
     */
    public Long sadd(String key, String... members) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            logger.debug("addSet redis, key {}, value {}.", key, members);
            return shardedJedis.sadd(key, members);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public Long zcard(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            logger.debug("addSet redis, key {}.", key);
            return shardedJedis.zcard(key);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public Long zadd(String key, double score, String member) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            logger.debug("addSet redis, key {}, member {}.", key, member);
            return shardedJedis.zadd(key, score, member);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public Long zrem(String key, String... members) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            logger.debug("zrem redis, key {}, member {}.", key, members);
            return shardedJedis.zrem(key, members);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public Long zadd(String key, Map<String, Double> scoreMembers) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            logger.debug("zadd redis, key {}, scoreMembers {}.", key, scoreMembers);
            return shardedJedis.zadd(key, scoreMembers);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    /**
     * 返回集合 key 中的所有成员
     *
     * @param key
     * @return
     */
    public Set<String> smembers(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            logger.debug("smembers redis, key {}, value {}.", key);
            return shardedJedis.smembers(key);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public Long ttl(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            logger.debug("ttl redis, key {}, value {}.", key);
            return shardedJedis.ttl(key);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public boolean sismember(String key, String member) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            logger.debug("sismember redis, key {}, value {}.", key);
            return shardedJedis.sismember(key, member);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public Long srem(String key, String... members) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            logger.debug("smembers redis, key {}, value {}.", key);
            return shardedJedis.srem(key, members);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public String set(String key, String value, String nxxx, String expx, long time) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            logger.info("==========ShardInfo:{}", shardedJedis.getShardInfo(key));
            logger.info("==========redis set,key:{},value:{},nxxx:{},expx:{},time:{}", key, value, nxxx, expx, time);
            return shardedJedis.set(key, value, nxxx, expx, time);
        } catch (Throwable t) {
            logger.error("redis set error,key:{},value:{},nxxx:{},expx:{},time:{},errorMsg:{}", key, value, nxxx, expx, time, t.getMessage());
            throw new RuntimeException(t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public boolean eval(String key, String value, String script) {
        Jedis jedis = null;
        Long releaseSuccess = 1L;
        try {
            jedis = shardedJedisPool.getResource().getShard(key);
            Object object = jedis.eval(script, Collections.singletonList(key), Collections.singletonList(value));
            if (releaseSuccess.equals(object)) {
                return true;
            }
            return false;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 获取锁
     *
     * @param lockKey
     * @param milliseconds
     * @return
     */
    public boolean getLock(String lockKey, String value, long milliseconds) {
        try {
            String lockRes = set(lockKey, value, RedisClient.SET_IF_NOT_EXIST, RedisClient.SET_WITH_EXPIRE_TIME, milliseconds);
            logger.info("==========getLock result:{}", lockRes);
            if (!RedisClient.LOCK_SUCCESS.equals(lockRes)) {
                logger.info("==========未获取到锁:{}", lockKey);
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.info("==========未获取到锁 {}, {}", lockKey, e.getMessage());
            return false;
        }
    }

    /**
     * 重试获取锁
     *
     * @param lockKey
     * @param value
     * @param milliseconds
     * @param times
     * @param interval
     * @return
     */
    public boolean getLockRetry(String lockKey, String value, long milliseconds, int times, long interval) {
        times = times > 0 ? times : 1;

        for (int i = 0; i < times; i++) {
            boolean locked = getLock(lockKey, value, milliseconds);

            if (locked) {
                return true;
            }

            try {
                TimeUnit.MILLISECONDS.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 释放分布式锁
     */
    public boolean releaseLock(String lockKey, String value) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        return eval(lockKey, value, script);
    }

    public Long incr(String key, Long value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            logger.debug("RedisClient incr, key {}, value {}.", key, value);
            return shardedJedis.incrBy(key, value);
        } catch (Throwable t) {
            logger.error("RedisClient incr is error!", t);
            throw new RuntimeException(t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    public Long incr(String key, int expireSecond) {
        ShardedJedis shardedJedis = null;
        Long result = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            result = shardedJedis.incr(key);
            Long ttl = shardedJedis.ttl(key);
            if (ttl <= 0 || ttl > expireSecond) {
                shardedJedis.expire(key, expireSecond);
            }
        } catch (Throwable t) {
            logger.error("RedisClient incr 1 param is error!", t);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
        return result;
    }

    public void setbit(String key, long id) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis.setbit(key, id, true);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    //总数
    public long bitcount(String key) {
        ShardedJedis shardedJedis = null;
        try {
            return shardedJedis.bitcount(key);
        } finally {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        }
    }

    @PreDestroy
    public void destroy() {
        long time = System.currentTimeMillis();
        logger.info("RedisClient.destroy time:{}", DateUtil.getFormatTime(time));
        shardedJedisPool.destroy();
    }
}
