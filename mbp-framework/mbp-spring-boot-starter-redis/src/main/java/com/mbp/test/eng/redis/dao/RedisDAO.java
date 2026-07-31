package com.mbp.test.eng.redis.dao;

import lombok.SneakyThrows;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * RedisDAO
 */
@Service("redisDAO")
public class RedisDAO {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 根据组合 key 从 Redis 中获取
     * @param keyConstants
     * @param key
     * @return
     */
    public String get(String keyConstants, String key) {
        String redisKey = formatKey(keyConstants, key);
        return stringRedisTemplate.opsForValue().get(redisKey);
    }

    /**
     * 根据组合 key 写入 Redis
     * @param keyConstants
     * @param key
     * @return
     */
    @SneakyThrows
    public <T> void set(String keyConstants, String key, String value) {
        String redisKey = formatKey(keyConstants, String.valueOf(key));
        stringRedisTemplate.opsForValue().set(redisKey, value);
    }

    /**
     * 根据组合 key 从Redis中删除
     * @param keyConstants
     * @param key
     * @return
     */
    public void delete(String keyConstants, String key) {
        String redisKey = formatKey(keyConstants, key);
        stringRedisTemplate.delete(redisKey);
    }

    /*public void deleteList(Collection<String> kens) {
        List<String> redisKeys = CollectionUtils.convertList(kens, RedisDAO::formatKey);
        stringRedisTemplate.delete(redisKeys);
    }*/

    private static String formatKey(String keyConstants, String ken) {
        return String.format(keyConstants, ken);
    }

    /**
     * 泛型方法的基本介绍
     *
     * @param tClass 传入的泛型实参
     * @return T 返回值为T类型
     */
    @SneakyThrows
    public <T> T genericMethod(Class<T> tClass) {
        /*T t1 = tClass.getDeclaredConstructor().newInstance();
        Class tClass1 = tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];*/
        T instance = tClass.newInstance();
        return instance;
    }
}
