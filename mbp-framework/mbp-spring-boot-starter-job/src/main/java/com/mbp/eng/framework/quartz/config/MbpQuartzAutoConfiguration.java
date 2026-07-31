package com.mbp.eng.framework.quartz.config;

import com.mbp.eng.framework.common.util.date.DateUtil;
import com.mbp.eng.framework.quartz.core.scheduler.SchedulerManager;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Optional;

/**
 * 定时任务 Configuration
 */
@AutoConfiguration
@EnableScheduling // 开启 Spring 自带的定时任务
@Slf4j
public class MbpQuartzAutoConfiguration {
    private static Logger logger = LoggerFactory.getLogger(MbpQuartzAutoConfiguration.class);
    @Bean
    public SchedulerManager schedulerManager(Optional<Scheduler> scheduler) {
        long time = System.currentTimeMillis();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("类=====:{} 方法=====:{} time: is {}", this.getClass().getSimpleName(), methodName, DateUtil.getFormatTime(time));

        if (!scheduler.isPresent()) {
            log.info("[定时任务 - 已禁用][参考 https://doc.google.cn/job/ 开启]");
            return new SchedulerManager(null);
        }
        return new SchedulerManager(scheduler.get());
    }

}
