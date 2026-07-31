package com.mbp.test.eng.redis.dao.base;

import java.io.Serializable;

/**
 * dao实现类
 *
 * @param <T> 实体
 * @param <K> 主键
 */
public abstract class BaseRedisDaoImpl<T, K extends Serializable> extends RedisSupport implements BaseRedisDao<T, K> {
    private static final String DEFAULT_INSERT_KEY = "insertEntry";

    private static final String DEFAULT_DELETE_ARRAY_KEY = "deleteByArrayKey";
    private static final String DEFAULT_DELETE_CONDTION = "deleteByCondtion";

    private static final String DEFAULT_SELECT_ARRAY_KEY = "selectEntryArray";
    private static final String DEFAULT_SELECT_CONDTION = "selectEntryList";


    /**
     * 获取命名空前前缀
     *
     * @param statement
     * @return
     */
    public abstract String getNameSpace(String statement);

    public int insertEntryRedis(String keys, T... t) {
        int result = 0;
        if (t == null || t.length <= 0) {
            return result;
        }
        for (T o : t) {
            if (o != null) {
                result += this.insertRedis(getNameSpace(DEFAULT_INSERT_KEY), keys, o);
            }
        }
        return result;
    }

    public int deleteByKey(K... key) {
        return this.delete(getNameSpace(DEFAULT_DELETE_ARRAY_KEY), key);
    }

    public int deleteByKeyRedis(String... key) {
        int result = 0;
        if (key == null || key.length <= 0) {
            return result;
        }
        for (String o : key) {
            if (o != null) {
                result += this.deleteByKeyRedis(getNameSpace(DEFAULT_DELETE_ARRAY_KEY), o);
            }
        }
        return result;
    }

    public int deleteByKeyR(T t) {
        return this.delete(getNameSpace(DEFAULT_DELETE_CONDTION), t);
    }
}
