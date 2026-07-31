package com.mbp.test.eng.redis.service.base;

import java.io.Serializable;

/**
 * service基类<实体,主键>
 *
 * @param <T>   实体
 * @param <KEY> 主键
 */
public interface BaseRedisService<T, KEY extends Serializable> {
    /**
     * 添加对象
     *
     * @param key
     * @param t
     * @return
     */
    int insertEntryRedis(String key, T... t);

    /**
     * 删除对象,主键
     *
     * @param key 主键数组
     * @return 影响条数
     */
    int deleteByKey(KEY... key);

    int deleteByKeyRedis(String... key);

    /**
     * 按条件删除对象
     *
     * @param condtion
     * @return 影响条数
     */
    int deleteByCondtion(T condtion);

    /**
     * 保存或更新对象(条件主键Id)
     *
     * @param t 需更新的对象
     * @return 影响条数
     */
    int saveOrUpdate(String key, T... t);
}
