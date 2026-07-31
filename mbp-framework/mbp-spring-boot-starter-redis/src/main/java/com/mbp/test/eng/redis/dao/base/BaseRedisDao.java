package com.mbp.test.eng.redis.dao.base;

import java.io.Serializable;


/**
 * RedisDAO
 */
public interface BaseRedisDao<T, KEY extends Serializable> {

    /**
     * 添加对象
     *
     * @param s
     * @param t
     * @return
     */
    int insertEntryRedis(String s, T... t);

    /**
     * 删除对象,主键
     *
     * @param key
     * @return 影响条数
     */
    int deleteByKey(KEY... key);

    int deleteByKeyRedis(String... key);

    /**
     * 删除对象,条件
     *
     * @param condtion
     * @return 影响条数
     */
    int deleteByKeyR(T condtion);
}
