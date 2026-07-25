package com.mbp.eng.framework.quartz.plan.impl;

import com.mbp.eng.framework.quartz.plan.SchedulerPlan;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;

public abstract class SchedulerPlanImpl implements SchedulerPlan {
    private static Logger logger = LoggerFactory.getLogger(PlanTaskImpl.class);

    @Autowired
    private Scheduler scheduler;

    @PreDestroy
    public void shutdownJobs() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询任务列表
     *
     * @param jobId
     * @return
     * @throws Exception
     */
    public String queryScheduleJob(String jobId) throws Exception {
        return null;
    }

    /**
     * 获取计划中的任务(已经添加到quartz调度器的任务)
     *
     * @return
     * @throws Exception
     */
    public String queryExecutingJobList() throws Exception {
        return null;
    }

    /**
     * 创建定时任务
     *
     * @param userpin
     * @param planId
     * @param jobId
     * @param cronExpression
     * @throws Exception
     */
    @Override
    public void createPlan(String userpin, String planId, String jobId, String cronExpression) throws Exception {
    }

    /**
     * 删除定时任务
     *
     * @param planId
     * @param jobId
     * @throws Exception
     */
    public void deletePlan(String planId, String jobId) throws Exception {
    }

    /**
     * 更新定时任务
     * 只能修改运行的时间,参数、同异步等无法修改
     *
     * @param planId
     * @param jobId
     * @param cronExpression
     */
    public void updatePlan(String planId, String jobId, String cronExpression) {
    }

    /**
     * 运行一次任务(单次执行任务运行一次后状态变为失效)
     *
     * @param planId
     */
    @Override
    public void runOnce(String planId, String jobId) throws Exception {
    }

    /**
     * 暂停任务
     *
     * @param planId
     * @param jobId
     * @throws Exception
     */
    @Override
    public void pauseScheduleJob(String planId, String jobId) throws Exception {
    }

    /**
     * 恢复任务
     *
     * @param planId
     */
    @Override
    public void resumeScheduleJob(String planId, String jobId) throws Exception {
    }
}
