package com.mbp.eng.framework.tenant.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.mbp.eng.framework.common.biz.system.tenant.TenantCommonApi;
import com.mbp.eng.framework.common.enums.WebFilterOrderEnum;
import com.mbp.eng.framework.common.util.date.DateUtil;
import com.mbp.eng.framework.mybatis.core.util.MyBatisUtils;
import com.mbp.eng.framework.redis.config.MbpCacheProperties;
import com.mbp.eng.framework.security.core.service.SecurityFrameworkService;
import com.mbp.eng.framework.tenant.core.aop.TenantIgnore;
import com.mbp.eng.framework.tenant.core.aop.TenantIgnoreAspect;
import com.mbp.eng.framework.tenant.core.db.TenantDatabaseInterceptor;
import com.mbp.eng.framework.tenant.core.job.TenantJobAspect;
import com.mbp.eng.framework.tenant.core.mq.rabbitmq.TenantRabbitMQInitializer;
import com.mbp.eng.framework.tenant.core.mq.redis.TenantRedisMessageInterceptor;
import com.mbp.eng.framework.tenant.core.mq.rocketmq.TenantRocketMQInitializer;
import com.mbp.eng.framework.tenant.core.redis.TenantRedisCacheManager;
import com.mbp.eng.framework.tenant.core.security.TenantSecurityWebFilter;
import com.mbp.eng.framework.tenant.core.service.TenantFrameworkService;
import com.mbp.eng.framework.tenant.core.service.TenantFrameworkServiceImpl;
import com.mbp.eng.framework.tenant.core.web.TenantContextWebFilter;
import com.mbp.eng.framework.tenant.core.web.TenantVisitContextInterceptor;
import com.mbp.eng.framework.web.config.WebProperties;
import com.mbp.eng.framework.web.core.handler.GlobalExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.BatchStrategies;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import javax.annotation.Resource;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;

import static com.mbp.eng.framework.common.util.collection.CollectionUtils.convertList;

@AutoConfiguration
@ConditionalOnProperty(prefix = "mbp.tenant", value = "enable", matchIfMissing = true)
// 允许使用 mbp.tenant.enable=false 禁用多租户
@EnableConfigurationProperties(TenantProperties.class)
public class MbpTenantAutoConfiguration {
    private static Logger logger = LoggerFactory.getLogger(MbpTenantAutoConfiguration.class);

    // 1. 定义私有的、线程安全的原子计数器
    //private final AtomicInteger invokeCount = new AtomicInteger(0);

    private final LongAdder invokeCount = new LongAdder();

    @Resource
    private ApplicationContext applicationContext;

    /**
     * {@link TenantFrameworkService}
     *
     * @param tenantCommonApi (多租户的 API 接口) {@link TenantCommonApi}
     * @return
     */
    @Bean
    public TenantFrameworkService tenantFrameworkService(TenantCommonApi tenantCommonApi) {
        long time = System.currentTimeMillis();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        invokeCount.increment();
        logger.info("类=====:{} 方法=====:{} 加载顺序为=====:{} time: is {}", this.getClass().getSimpleName(), methodName, invokeCount, DateUtil.getFormatTime(time));

        return new TenantFrameworkServiceImpl(tenantCommonApi);
    }

    // ========== AOP ==========

    /**
     * {@link TenantIgnoreAspect}
     */
    @Bean
    public TenantIgnoreAspect tenantIgnoreAspect() {
        return new TenantIgnoreAspect();
    }

    // ========== DB ==========

    /**
     * MyBatis-Plus 内置的多租户数据隔离插件，它能够自动将租户 ID 过滤条件嵌入到 SQL 语句中
     *
     * @param tenantProperties (多租户配置 mbp.tenant) {@link TenantProperties}
     * @param mybatisPlusInterceptor
     * @return
     */
    @Bean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor(TenantProperties tenantProperties,
                                                                 MybatisPlusInterceptor mybatisPlusInterceptor) {
        long time = System.currentTimeMillis();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        invokeCount.increment();
        logger.info("类=====:{} 方法=====:{} 加载顺序为=====:{} time: is {}", this.getClass().getSimpleName(), methodName, invokeCount, DateUtil.getFormatTime(time));

        TenantLineInnerInterceptor inner = new TenantLineInnerInterceptor(new TenantDatabaseInterceptor(tenantProperties));
        // 添加到 interceptor 中
        // 需要加在首个,主要是为了在分页插件前面。这个是 MyBatis Plus 的规定
        MyBatisUtils.addInterceptor(mybatisPlusInterceptor, inner, 0);
        return inner;
    }

    // ========== WEB ==========

    /**
     * FilterRegistrationBean 是 Spring Boot 中用来手动注册和管理过滤器的配置类
     * Spring Boot API 工具，用于注册自定义的 Servlet 过滤器、设置特定的 URL 模式，以及定义过滤器的执行顺序。与简单的组件扫描方式相比，它提供了对过滤器如何以及何时运行的精细控制
     * {@link TenantContextWebFilter}
     * {@link WebFilterOrderEnum}
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<TenantContextWebFilter> tenantContextWebFilter() {
        long time = System.currentTimeMillis();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        invokeCount.increment();
        logger.info("类=====:{} 方法=====:{} 加载顺序为=====:{} time: is {}", this.getClass().getSimpleName(), methodName, invokeCount, DateUtil.getFormatTime(time));

        FilterRegistrationBean<TenantContextWebFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        //多租户 Context Web 过滤器(TenantContextWebFilter)
        filterFilterRegistrationBean.setFilter(new TenantContextWebFilter());
        //Web 过滤器顺序的枚举类(order 值‌从小到大‌排列过滤器执行顺序)
        filterFilterRegistrationBean.setOrder(WebFilterOrderEnum.TENANT_CONTEXT_FILTER);

        return filterFilterRegistrationBean;
    }

    /**
     * {@link TenantVisitContextInterceptor}
     *
     * @param tenantProperties         (多租户配置 mbp.tenant) {@link TenantProperties}
     * @param securityFrameworkService (Security 框架 Service 接口,定义权限相关的校验操作) {@link SecurityFrameworkService}
     * @return
     */
    @Bean
    public TenantVisitContextInterceptor tenantVisitContextInterceptor(TenantProperties tenantProperties,
                                                                       SecurityFrameworkService securityFrameworkService) {
        long time = System.currentTimeMillis();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        invokeCount.increment();
        logger.info("类=====:{} 方法=====:{} 加载顺序为=====:{} time: is {}", this.getClass().getSimpleName(), methodName, invokeCount, DateUtil.getFormatTime(time));

        return new TenantVisitContextInterceptor(tenantProperties, securityFrameworkService);
    }

    /**
     * Spring 框架中的一个 Java 配置接口，用于自定义 Spring MVC 的设置。通过在该接口上进行实现，可以添加 CORS 映射、拦截器以及资源处理器等组件
     *
     * @param tenantProperties              (多租户配置 mbp.tenant) {@link TenantProperties}
     * @param tenantVisitContextInterceptor (实现HandlerInterceptor判断用户和编号) {@link TenantVisitContextInterceptor}
     * @return
     */
    @Bean
    public WebMvcConfigurer tenantWebMvcConfigurer(TenantProperties tenantProperties,
                                                   TenantVisitContextInterceptor tenantVisitContextInterceptor) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                long time = System.currentTimeMillis();
                String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
                invokeCount.increment();
                logger.info("类=====:{} 方法=====:{} 加载顺序为=====:{} time: is {}", this.getClass().getSimpleName(), methodName, invokeCount, DateUtil.getFormatTime(time));

                registry.addInterceptor(tenantVisitContextInterceptor)
                        .excludePathPatterns(tenantProperties.getIgnoreVisitUrls().toArray(new String[0]));
            }
        };
    }

    // ========== Security ==========

    /**
     * FilterRegistrationBean 是 Spring Boot 中用来手动注册和管理过滤器的配置类
     * Spring Boot API 工具，用于注册自定义的 Servlet 过滤器、设置特定的 URL 模式，以及定义过滤器的执行顺序。与简单的组件扫描方式相比，它提供了对过滤器如何以及何时运行的精细控制
     *
     * @param tenantProperties       (多租户配置 mbp.tenant) {@link TenantProperties}
     * @param webProperties          (mbp.web-Api属性) {@link WebProperties}
     * @param globalExceptionHandler (全局异常处理器) {@link GlobalExceptionHandler}
     * @param tenantFrameworkService (Tenant 框架 Service 接口,定义获取租户信息) {@link TenantFrameworkService}
     * @return
     */
    @Bean
    public FilterRegistrationBean<TenantSecurityWebFilter> tenantSecurityWebFilter(TenantProperties tenantProperties,
                                                                                   WebProperties webProperties,
                                                                                   GlobalExceptionHandler globalExceptionHandler,
                                                                                   TenantFrameworkService tenantFrameworkService) {
        long time = System.currentTimeMillis();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        invokeCount.increment();
        logger.info("类=====:{} 方法=====:{} 加载顺序为=====:{} time: is {}", this.getClass().getSimpleName(), methodName, invokeCount, DateUtil.getFormatTime(time));

        FilterRegistrationBean<TenantSecurityWebFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantSecurityWebFilter(webProperties, tenantProperties, getTenantIgnoreUrls(),
                globalExceptionHandler, tenantFrameworkService));
        registrationBean.setOrder(WebFilterOrderEnum.TENANT_SECURITY_FILTER);
        return registrationBean;
    }

    /**
     * 如果 Controller 接口上,有 {@link TenantIgnore} 注解,则添加到忽略租户的 URL 集合中
     *
     * @return 忽略租户的 URL 集合
     */
    private Set<String> getTenantIgnoreUrls() {
        long time = System.currentTimeMillis();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        invokeCount.increment();
        logger.info("类=====:{} 方法=====:{} 加载顺序为=====:{} time: is {}", this.getClass().getSimpleName(), methodName, invokeCount, DateUtil.getFormatTime(time));

        Set<String> ignoreUrls = new HashSet<>();
        // 获得接口对应的 HandlerMethod 集合
        RequestMappingHandlerMapping requestMappingHandlerMapping = (RequestMappingHandlerMapping)
                applicationContext.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = requestMappingHandlerMapping.getHandlerMethods();
        // 获得有 @TenantIgnore 注解的接口
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            if (!handlerMethod.hasMethodAnnotation(TenantIgnore.class) // 方法级
                    && !handlerMethod.getBeanType().isAnnotationPresent(TenantIgnore.class)) { // 接口级
                continue;
            }
            // 添加到忽略的 URL 中
            if (entry.getKey().getPatternsCondition() != null) {
                ignoreUrls.addAll(entry.getKey().getPatternsCondition().getPatterns());
            }
            if (entry.getKey().getPathPatternsCondition() != null) {
                ignoreUrls.addAll(
                        convertList(entry.getKey().getPathPatternsCondition().getPatterns(), PathPattern::getPatternString));
            }
        }
        return ignoreUrls;
    }

    // ========== MQ ==========

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "com.mbp.eng.framework.mq.redis.core.interceptor.RedisMessageInterceptor")
    public static class TenantRedisMQConfiguration {
        /**
         * {@link TenantRedisMessageInterceptor}
         *
         * @return
         */
        @Bean
        public TenantRedisMessageInterceptor tenantRedisMessageInterceptor() {
            return new TenantRedisMessageInterceptor();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "org.springframework.amqp.rabbit.core.RabbitTemplate")
    public static class TenantRabbitMQConfiguration {
        /**
         * {@link TenantRabbitMQInitializer}
         *
         * @return
         */
        @Bean
        public TenantRabbitMQInitializer tenantRabbitMQInitializer() {
            return new TenantRabbitMQInitializer();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "org.apache.rocketmq.spring.core.RocketMQTemplate")
    public static class TenantRocketMQConfiguration {
        /**
         * {@link TenantRocketMQInitializer}
         *
         * @return
         */
        @Bean
        public TenantRocketMQInitializer tenantRocketMQInitializer() {
            return new TenantRocketMQInitializer();
        }
    }

    // ========== Redis ==========

    /**
     * 提供了核心的缓存管理功能：包括 RedisCacheManager API 以及 Redis 缓存指南。该组件负责处理对单个实例的创建、读取和写入操作，这些操作都基于 Redis 数据存储进行支持
     *
     * @param redisTemplate
     * @param redisCacheConfiguration
     * @param mbpCacheProperties      (多租户配置 mbp.cache) {@link MbpCacheProperties}
     * @param tenantProperties        (多租户配置 mbp.tenant) {@link TenantProperties}
     * @return
     */
    @Bean
    @Primary // 引入租户时,tenantRedisCacheManager 为主 Bean
    public RedisCacheManager tenantRedisCacheManager(RedisTemplate<String, Object> redisTemplate,
                                                     RedisCacheConfiguration redisCacheConfiguration,
                                                     MbpCacheProperties mbpCacheProperties,
                                                     TenantProperties tenantProperties) {
        long time = System.currentTimeMillis();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        invokeCount.increment();
        logger.info("类=====:{} 方法=====:{} 加载顺序为=====:{} time: is {}", this.getClass().getSimpleName(), methodName, invokeCount, DateUtil.getFormatTime(time));

        // 创建 RedisCacheWriter 对象
        RedisConnectionFactory connectionFactory = Objects.requireNonNull(redisTemplate.getConnectionFactory());
        RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory,
                BatchStrategies.scan(mbpCacheProperties.getRedisScanBatchSize()));
        // 创建 TenantRedisCacheManager 对象
        TenantRedisCacheManager cacheManager = new TenantRedisCacheManager(cacheWriter, redisCacheConfiguration,
                tenantProperties.getIgnoreCaches());
        // 开启事务感知：@Transactional 方法内的 @CacheEvict / @CachePut 自动延迟到 afterCommit,
        //             避免事务未提交就清缓存被并发读穿写脏值；无事务时立即生效,行为不变
        cacheManager.setTransactionAware(true);
        return cacheManager;
    }

    // ========== Job ==========

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "com.mbp.eng.framework.quartz.core.handler.JobHandler")
    public static class TenantJobConfiguration {
        /**
         * {@link TenantFrameworkService}
         *
         * @return
         */
        @Bean
        public TenantJobAspect tenantJobAspect(TenantFrameworkService tenantFrameworkService) {
            return new TenantJobAspect(tenantFrameworkService);
        }
    }

    /*public int getInvokeCount() {
        return invokeCount.get();
    }*/

    public long getInvokeCountS() {
        return invokeCount.sum();
    }
}
