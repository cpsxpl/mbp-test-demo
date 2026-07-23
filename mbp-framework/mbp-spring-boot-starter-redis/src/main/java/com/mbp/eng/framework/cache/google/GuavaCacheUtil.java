package com.mbp.eng.framework.cache.google;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.commons.lang.RandomStringUtils;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @note 可以参考  https://www.jianshu.com/p/f4b99b70bd76
 */
public class GuavaCacheUtil {
    //最大缓存数据量
    private static int maximumSize = 200000;
    //初始容量
    private static int initialCapacity = 100000;

    /*
     *
     * 相对地,Guava Cache为了限制内存占用,通常都设定为自动回收元素。在某些场景下,尽管LoadingCache
     * 不回收元素,它也是很有用的,因为它会自动加载缓存。
     *
     * 通常来说,Guava Cache适用于：
     *
     * 你愿意消耗一些内存空间来提升速度。
     * 你预料到某些键会被查询一次以上。
     * 缓存中存放的数据总量不会超出内存容量。
     * （GuavaCache是单个应用运行时的本地缓存。它不把数据存放到文件或外部服务器。如果这不符合你的需求,请尝试 Memcached 这类工具）
     * memcached是一套分布式的高速缓存系统
     * 如果你的场景符合上述的每一条,Guava Cache就适合你。
     * 注：如果你不需要Cache中的特性,使用ConcurrentHashMap有更好的内存效率——但Cache的大多数特性都很难基于旧有的ConcurrentMap复制,甚至根本不可能做到。
     *
     */

    // 新建了一个线程池,用来执行缓存刷新任务。并且重写了CacheLoader的reload方法,在该方法中建立缓存刷新的任务并提交到线程池。
    private static ListeningExecutorService listeningExecutorService = MoreExecutors
            .listeningDecorator(Executors.newFixedThreadPool(20));

    private static Cache<String, String> localCache = CacheBuilder.newBuilder()
            .maximumSize(maximumSize)// 最大缓存数据量
            .initialCapacity(initialCapacity)// 初始容量
            .recordStats()// 用来开启Guava Cache的统计功能
            .expireAfterWrite(5, TimeUnit.SECONDS)// 过期清除
            /*
             * 1.缓存值定时刷新：更新线程调用load方法更新该缓存,其他请求线程返回该缓存的旧值。这里的定时并不是真正意义上的定时。
             * 2.Guava
             * cache的刷新需要依靠用户请求线程,让该线程去进行load方法的调用,所以如果一直没有用户尝试获取该缓存值,则该缓存也并不会刷新
             * 3.这样对于某个key的缓存来说,只会有一个线程被阻塞,用来生成缓存值,而其他的线程都返回旧的缓存值,不会被阻塞。
             */
            .refreshAfterWrite(50, TimeUnit.SECONDS)// 每隔十s缓存值则会被刷新。防止缓存穿透
            .removalListener(new RemovalListener<String, String>() { // 设置监听事件,就是在
                // 删除key的时候触发这个事件
                @Override
                public void onRemoval(
                        RemovalNotification<String, String> notification) {
                    String email = notification.getValue();
                    String key = notification.getKey();
                    RemovalCause removalCause = notification.getCause();
                    System.out.println("结果====" + email);
                    System.out.println("key====" + key);
                    System.out.println("cause====" + removalCause);
                }
            }).build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    return generateValueByKey(key);
                }
                /*
                 * Guava Cache异步刷新：
                 *
                 * 如上的使用方法,解决了同一个key的缓存过期时会让多个线程阻塞的问题,只会让用来执行刷新缓存操作的一个用户线程会被阻塞。
                 * 由此可以想到另一个问题,当缓存的key很多时,高并发条件下大量线程同时获取不同key对应的缓存,此时依然会造成大量线程阻塞
                 * ,并且给数据库带来很大压力。
                 * 这个问题的解决办法就是将刷新缓存值的任务交给后台线程,所有的用户请求线程均返回旧的缓存值,这样就不会有用户线程被阻塞了。
                 * 详细做法如下：
                 */

                // 注意此时缓存的刷新依然需要靠用户线程来驱动,只不过和上面不同之处在于该用户线程触发刷新操作之后,会立马返回旧的缓存值。
                @Override
                public ListenableFuture<String> reload(final String key, String oldValue) throws Exception {
                    return listeningExecutorService
                            .submit(new Callable<String>() {
                                @Override
                                public String call() throws Exception {
                                    return generateValueByKey(key);
                                }
                            });
                }
            });

    /**
     *
     */
    private static void add() {
        localCache.put("username", "xushuai");
        localCache.put("username2", "xushuai");
        System.out.println("1>>>>>" + localCache.getIfPresent("username"));
    }

    /**
     *
     */
    private static void add(String key, String value) {
        localCache.put(key, value);
    }

    /**
     * 清除
     */
    private static void del() {
        // 个别清除：Cache.invalidate(key)
        // 批量清除：Cache.invalidateAll(keys)
        // 清除所有缓存项：Cache.invalidateAll()
        localCache.invalidate("username2");
    }

    /**
     * 清除
     */
    private static void del(String key) {
        // 个别清除：Cache.invalidate(key)
        // 批量清除：Cache.invalidateAll(keys)
        // 清除所有缓存项：Cache.invalidateAll()
        localCache.invalidate(key);
    }

    /**
     *
     */
    private static void asMap() {
        ConcurrentMap<String, String> concurrentMap = localCache.asMap();
        Set<String> set = concurrentMap.keySet();
        for (String s : set) {
            System.out.println(localCache.getIfPresent(s));
        }
    }

    /**
     * 获取
     */
    private static void get() {
        for (int i = 0; i <= 100; i++) {
            if (i % 20 == 0) {
                System.out.println(
                        i + ">>>>>" + localCache.getIfPresent("username"));
            }
        }
    }

    /**
     * Guava Cache统计
     */
    private static void tongji() {
        CacheStats cacheStats = localCache.stats();
        // 缓存命中率；
        System.out.println("缓存命中率:" + cacheStats.hitRate());
        // 缓存项被回收的总数,不包括显式清除。
        System.out.println("缓存项被回收的总数,不包括显式清除:" + cacheStats.evictionCount());
    }

    public static String generateValueByKey(String key) {
        return key + ":缓存值定时刷新 防止有缓存穿透"
                + RandomStringUtils.randomAlphabetic(10);
    }

    public static void main(String[] args) {
        String key1 = "username";
        String value1 = "xushuai";
        String key2 = "username";
        String value2 = "xushuai";
        add(key1, value1);
        add(key2, value2);
        asMap();
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tongji();
        System.out.println("2>>>>>" + localCache.getIfPresent("username"));

        /*
         * 结果： 1>>>>>xushuai 2>>>>>null
         */

        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        get();

        try {
            Thread.sleep(15000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tongji();
        String username2 = "username2";
        del(username2);
    }
}
