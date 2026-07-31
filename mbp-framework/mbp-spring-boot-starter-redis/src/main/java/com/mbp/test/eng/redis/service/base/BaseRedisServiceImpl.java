package com.mbp.test.eng.redis.service.base;

import com.mbp.test.eng.redis.dao.base.BaseRedisDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * service实现类
 *
 * @param <T> 实体
 * @param <K> 主键
 */
public abstract class BaseRedisServiceImpl<T, K extends Serializable> implements BaseRedisService<T, K> {
    protected static final Logger logger = LoggerFactory.getLogger(BaseRedisServiceImpl.class);

    /**
     * 获取DAO操作类
     */
    public abstract BaseRedisDao<T, K> getDao();

    public int insertEntryRedis(String key, T... t) {
        return getDao().insertEntryRedis(key, t);
    }

    public int deleteByKey(K... key) {
        return getDao().deleteByKey(key);
    }

    public int deleteByKeyRedis(String... key) {
        return getDao().deleteByKeyRedis(key);
    }

    public int deleteByCondtion(T condtion) {
        return getDao().deleteByKeyR(condtion);
    }

    @SuppressWarnings("unchecked")
    public int saveOrUpdate(String key, T... t) {
        Integer id = 0;
        try {
            Class<?> clz = t.getClass();
            id = (Integer) clz.getMethod("getId").invoke(t);
        } catch (Exception e) {
            logger.warn("获取对象主键值失败!");
        }
        if (id != null && id > 0) {
            //return this.updateByKey(t);
        }
        return this.insertEntryRedis(key, t);
    }
}
