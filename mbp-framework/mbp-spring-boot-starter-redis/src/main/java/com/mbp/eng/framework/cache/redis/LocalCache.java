package com.mbp.eng.framework.cache.redis;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class LocalCache implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(LocalCache.class);

    @Value("${local.refresh.second:30}")
    int refresh = 30;

    @Value("${local.expire.second:60}")
    int expire = 60;

    @Autowired
    RedisClient redisClient;

    // 本地对Redis的缓存,缓存expire(默认60)秒,因此会有expire秒的误差
    private LoadingCache<String, String> loadingCache = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        loadingCache = CacheBuilder.newBuilder().refreshAfterWrite(refresh, TimeUnit.SECONDS)// 给定时间内没有被读/写访问,则回收。
                .expireAfterAccess(expire, TimeUnit.SECONDS)// 缓存过期时间和redis缓存时长一样
                .maximumSize(1000)// 设置缓存个数
                .build(new CacheLoader<String, String>() {
                    @Override
                    /** 当本地缓存命没有中时,调用load方法获取结果并将结果缓存 **/
                    public String load(String key) {
                        String value = redisClient.get(key);
                        return value;
                    }
                });
    }

    public String getFromLocal4Redis(String key) {
        try {
            String value = loadingCache.get(key);
            return value;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 本地缓存,checkpoint
    //****************************** lastCheckPointMap_start ******************************
    private static Map<String, Long> lastCheckPointMap = new HashMap<>();

    public static long getCheckPoint(String key) {
        if (lastCheckPointMap.containsKey(key)) {
            return lastCheckPointMap.get(key);
        }
        return -1;
    }

    public static void setCheckPoint(String key, Long checkpoint) {
        lastCheckPointMap.put(key, checkpoint);
    }
    //****************************** lastCheckPointMap_end ********************************

    //****************************** fourAddressMappingMap_start ******************************
    private Map<String, Object> fourAddressMappingMap = new HashMap<>();

    public Map<String, Object> getFourAddressMappingMap() {
        return fourAddressMappingMap;
    }

    public void setFourAddressMappingMap(Map<String, Object> fourAddressMappingMap) {
        this.fourAddressMappingMap = fourAddressMappingMap;
    }
    //****************************** fourAddressMappingMap_end ********************************

    //****************************** demoMap_start ******************************
    private Map<String, Object> demoMap = new HashMap<>();

    public Map<String, Object> getDemoMap() {
        return demoMap;
    }

    public void setDemoMap(Map<String, Object> demoMap) {
        this.demoMap = demoMap;
    }
    //****************************** demoMap_end ********************************
}
