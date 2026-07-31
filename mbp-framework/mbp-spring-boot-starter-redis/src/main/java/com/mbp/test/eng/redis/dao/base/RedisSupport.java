package com.mbp.test.eng.redis.dao.base;

import com.mbp.eng.framework.common.exception.AppException;
import com.mbp.eng.framework.common.util.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;

/**
 * 对redis的支持
 * spring配置文件需定义stringRedisTemplate与batchStringRedisTemplate
 */
abstract class RedisSupport {
    protected static final Logger logger = LoggerFactory.getLogger(RedisSupport.class);
    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    private StringRedisTemplate batchStringRedisTemplate;

    /**
     * StringRedisTemplate
     *
     * @param batch    是否批处理
     * @param readonly 是否只读
     * @return
     */
    protected StringRedisTemplate getStringRedisTemplate(boolean batch, boolean readonly) {
        if (batch) {
            return batchStringRedisTemplate;
        }
        return stringRedisTemplate;
    }

    /**
     * 新增对象
     *
     * @param parameter
     * @return
     */
    protected int insertRedis(String statement, String key, Object parameter) {
        int res = 0;
        try {
            if (parameter != null) {
                getStringRedisTemplate(false, false).opsForValue().set(key, JsonUtils.toJsonString(parameter));
                res = res + 1;
            }
        } catch (Exception ex) {
            throw new AppException("Redis执行新增异常", ex);
        }
        return res;
    }

    /**
     * 删除对象
     *
     * @param parameter
     * @return
     */
    protected int delete(String statement, Object parameter) {
        int res = 0;
        try {
            getStringRedisTemplate(false, false).delete(parameter.toString());
            res = res + 1;
        } catch (Exception ex) {
            throw new AppException("Redis执行删除异常", ex);
        }
        return res;
    }

    /**
     * 删除对象
     *
     * @param parameter
     * @return
     */
    protected int deleteByKeyRedis(String statement, Object parameter) {
        int res = 0;
        try {
            getStringRedisTemplate(false, false).delete(parameter.toString());
            res = res + 1;
        } catch (Exception ex) {
            throw new AppException("Redis执行删除异常", ex);
        }
        return res;
    }

    /**
     * 查询一条记录
     *
     * @param parameter
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    protected <T> T selectRedis(String statement, Object parameter) {
        T obj = null;
        try {
            obj = (T) getStringRedisTemplate(false, true).opsForValue().get(parameter);
        } catch (Exception ex) {
            throw new AppException("Redis执行单条查询异常", ex);
        }
        return obj;
    }
}
