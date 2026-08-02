package com.mbp.test.eng.quartz.config;

import com.mbp.test.eng.quartz.plan.impl.PlanTaskImpl;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;

import java.util.Properties;

@Configuration
public class SchedulerConfig {
    // ==========================================
    // 1. 数据源属性注入
    // ==========================================
    @Value("${spring.quartz.properties.org.quartz.dataSource.driver}")
    private String driverClassName;

    @Value("${spring.quartz.properties.org.quartz.dataSource.url}")
    private String dbUrl;

    @Value("${spring.quartz.properties.org.quartz.dataSource.username}")
    private String username;

    @Value("${spring.quartz.properties.org.quartz.dataSource.password}")
    private String password;

    @Value("${spring.quartz.properties.org.quartz.dataSource.maxActive}")
    private int maxActive;

    @Value("${spring.quartz.properties.org.quartz.dataSource.validationQuery}")
    private String validationQuery;

    @Value("${spring.quartz.properties.org.quartz.dataSource.connectionProperties}")
    private String connectionProperties;

    @Value("${spring.quartz.properties.org.quartz.dataSource.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.quartz.properties.org.quartz.dataSource.minIdle}")
    private int minIdle;

    @Value("${spring.quartz.properties.org.quartz.dataSource.maxWait}")
    private long maxWait;

    @Value("${spring.quartz.properties.org.quartz.dataSource.maxIdle}")
    private int maxIdle;

    @Value("${spring.quartz.properties.org.quartz.dataSource.defaultAutoCommit}")
    private boolean defaultAutoCommit;

    @Value("${spring.quartz.properties.org.quartz.dataSource.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;

    @Value("${spring.quartz.properties.org.quartz.dataSource.minEvictableIdleTimeMillis}")
    private long minEvictableIdleTimeMillis;

    // ==========================================
    // 2. Quartz 属性注入
    // ==========================================
    @Value("${spring.quartz.properties.org.quartz.scheduler.instanceName}")
    private String instanceName;

    @Value("${spring.quartz.properties.org.quartz.scheduler.instanceId}")
    private String instanceId;

    @Value("${spring.quartz.properties.org.quartz.threadPool.class}")
    private String threadPoolClass;

    @Value("${spring.quartz.properties.org.quartz.threadPool.threadCount}")
    private String threadCount;

    @Value("${spring.quartz.properties.org.quartz.threadPool.threadPriority}")
    private String threadPriority;

    @Value("${spring.quartz.properties.org.quartz.jobStore.class}")
    private String jobStoreClass;

    @Value("${spring.quartz.properties.org.quartz.jobStore.driverDelegateClass}")
    private String driverDelegateClass;

    @Value("${spring.quartz.properties.org.quartz.jobStore.useProperties}")
    private String useProperties;

    @Value("${spring.quartz.properties.org.quartz.jobStore.isClustered}")
    private String isClustered;

    @Value("${spring.quartz.properties.org.quartz.jobStore.clusterCheckinInterval}")
    private String clusterCheckinInterval;

    @Value("${spring.quartz.properties.org.quartz.jobStore.maxMisfiresToHandleAtATime}")
    private String maxMisfiresToHandleAtATime;

    @Value("${spring.quartz.properties.org.quartz.jobStore.misfireThreshold}")
    private String misfireThreshold;

    @Value("${spring.quartz.properties.org.quartz.jobStore.tablePrefix}")
    private String tablePrefix;

    @Value("${spring.quartz.properties.org.quartz.startupDelay}")
    private int startupDelay;

    @Value("${spring.quartz.properties.org.quartz.applicationContextSchedulerContextKey}")
    private String appContextKey;

    @Value("${spring.quartz.properties.org.quartz.overwriteExistingJobs}")
    private boolean overwriteExistingJobs;

    @Value("${spring.quartz.properties.org.quartz.autoStartup}")
    private boolean autoStartup;

    // ==========================================
    // 3. Bean 定义
    // ==========================================

    /**
     * 对应原 XML 中的 <bean id="planTask" ...>
     */
    @Bean(name = "planTask")
    public PlanTaskImpl planTask() {
        return new PlanTaskImpl();
    }

    /**
     * 对应原 XML 中的 <bean id="dataSourceQuartz" ...>
     * 使用 destroyMethod = "close" 确保应用关闭时释放连接池
     */
    @Bean(name = "dataSourceQuartz", destroyMethod = "close")
    public DataSource dataSourceQuartz() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(driverClassName);
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);
        basicDataSource.setMaxActive(maxActive);
        basicDataSource.setValidationQuery(validationQuery);
        basicDataSource.setConnectionProperties(connectionProperties);
        basicDataSource.setTestWhileIdle(testWhileIdle);
        basicDataSource.setMinIdle(minIdle);
        basicDataSource.setMaxWait(maxWait);
        basicDataSource.setMaxIdle(maxIdle);
        basicDataSource.setDefaultAutoCommit(defaultAutoCommit);
        basicDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        basicDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        return basicDataSource;
    }

    /**
     * 对应原 XML 中的 <bean id="scheduler" ...>
     */
    @Bean(name = "scheduler")
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSourceQuartz) {
        // 组装 Quartz 原生属性
        Properties properties = new Properties();
        properties.setProperty("org.quartz.scheduler.instanceName", instanceName);
        properties.setProperty("org.quartz.scheduler.instanceId", instanceId);
        properties.setProperty("org.quartz.threadPool.class", threadPoolClass);
        properties.setProperty("org.quartz.threadPool.threadCount", threadCount);
        properties.setProperty("org.quartz.threadPool.threadPriority", threadPriority);
        properties.setProperty("org.quartz.jobStore.class", jobStoreClass);
        properties.setProperty("org.quartz.jobStore.driverDelegateClass", driverDelegateClass);
        properties.setProperty("org.quartz.jobStore.useProperties", useProperties);
        properties.setProperty("org.quartz.jobStore.isClustered", isClustered);
        properties.setProperty("org.quartz.jobStore.clusterCheckinInterval", clusterCheckinInterval);
        properties.setProperty("org.quartz.jobStore.maxMisfiresToHandleAtATime", maxMisfiresToHandleAtATime);
        properties.setProperty("org.quartz.jobStore.misfireThreshold", misfireThreshold);
        properties.setProperty("org.quartz.jobStore.tablePrefix", tablePrefix);

        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        // 注入上面定义的数据源
        schedulerFactoryBean.setDataSource(dataSourceQuartz);
        schedulerFactoryBean.setQuartzProperties(properties);
        // 设置其他常规属性
        schedulerFactoryBean.setSchedulerName(instanceName);
        schedulerFactoryBean.setStartupDelay(startupDelay);
        schedulerFactoryBean.setApplicationContextSchedulerContextKey(appContextKey);
        schedulerFactoryBean.setOverwriteExistingJobs(overwriteExistingJobs);
        schedulerFactoryBean.setAutoStartup(autoStartup);
        return schedulerFactoryBean;
    }
}
