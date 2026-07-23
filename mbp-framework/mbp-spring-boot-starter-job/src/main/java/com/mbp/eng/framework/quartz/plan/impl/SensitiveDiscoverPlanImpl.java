package com.mbp.eng.framework.quartz.plan.impl;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mbp.eng.framework.common.Constants;
import com.mbp.eng.framework.common.util.json.JsonUtil;
import com.mbp.eng.framework.quartz.TaskSchedulerContainer;
import com.mbp.eng.framework.quartz.plan.SensitiveDiscoverPlan;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

import java.util.List;

/**
 * 定时任务
 */
@Service("sensitiveDiscoverPlan")
public class SensitiveDiscoverPlanImpl extends SchedulerPlanImpl implements SensitiveDiscoverPlan {
    private static Logger logger = LoggerFactory.getLogger(SensitiveDiscoverPlanImpl.class);

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
     * @param
     * @return
     */
    @Override
    public String queryScheduleJob(String jobId) throws Exception {
        ObjectNode objectNode = JsonUtil.objectMapper.createObjectNode();
        TaskSchedulerContainer taskSchedulerContainer = TaskSchedulerContainer.getTaskSchedulerContainer(scheduler);

        JobKey jobKey = taskSchedulerContainer.getJobKey(jobId);
        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
        if (CollectionUtils.isEmpty(triggers)) {
            return JsonUtil.toJSON(objectNode);
        }
        //这里一个任务可以有多个触发器， 但是我们一个任务对应一个触发器，所以只取第一个即可，清晰明了
        Trigger trigger = triggers.iterator().next();
        objectNode.put("jobTrigger", trigger.getKey().getName());

        Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
        objectNode.put("status", triggerState.name());
        if (trigger instanceof CronTrigger) {
            CronTrigger cronTrigger = (CronTrigger) trigger;
            String cronExpression = cronTrigger.getCronExpression();
            objectNode.put("cronExpression", cronExpression);
        }
        return JsonUtil.toJSON(objectNode);
    }

    /**
     * 获取计划中的任务(已经添加到quartz调度器的任务)
     *
     * @return
     */
    @Override
    public String queryExecutingJobList() throws Exception {
        ArrayNode arrayNode = JsonUtil.objectMapper.createArrayNode();
        // 获取scheduler中的JobGroupName
        for (String group : scheduler.getJobGroupNames()) {
            // 获取JobKey 循环遍历JobKey
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.<JobKey>groupEquals(group))) {
                logger.info("SensitiveDiscoverPlanImpl.queryExecutingJobList jobKey:{}", jobKey);
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                JobDataMap jobDataMap = jobDetail.getJobDataMap();
                jobDataMap.get(Constants.JOB_PARAM_KEY);

                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                Trigger trigger = triggers.iterator().next();
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());

                ObjectNode objectNode = JsonUtil.objectMapper.createObjectNode();
                objectNode.put("jobTrigger", trigger.getKey().getName());
                objectNode.put("status", triggerState.name());

                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    objectNode.put("cronExpression", cronExpression);
                }
                logger.info("SensitiveDiscoverPlanImpl.queryExecutingJobList triggerState:{}", triggerState);
                // 获取正常运行的任务列表
                if (triggerState.name().equals("NORMAL")) {
                    arrayNode.add(objectNode);
                }
            }
        }
        return JsonUtil.toJSON(arrayNode);
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
        TaskSchedulerContainer taskSchedulerContainer = TaskSchedulerContainer.getTaskSchedulerContainer(scheduler);
        taskSchedulerContainer.addTask(userpin, planId, jobId, cronExpression);
    }

    /**
     * 删除定时任务
     *
     * @param planId
     * @param jobId
     * @throws Exception
     */
    public void deletePlan(String planId, String jobId) throws Exception {
        TaskSchedulerContainer taskSchedulerContainer = TaskSchedulerContainer.getTaskSchedulerContainer(scheduler);
        //删除运行的任务
        taskSchedulerContainer.deleteScheduleJob(planId, jobId);
    }

    /**
     * 更新定时任务
     * 只能修改运行的时间，参数、同异步等无法修改
     *
     * @param planId
     * @param jobId
     * @param cronExpression
     */
    public void updatePlan(String planId, String jobId, String cronExpression) {
        TaskSchedulerContainer taskSchedulerContainer = TaskSchedulerContainer.getTaskSchedulerContainer(scheduler);
        taskSchedulerContainer.updateScheduleJob(planId, jobId, cronExpression);
    }

    /**
     * 运行一次任务(单次执行任务运行一次后状态变为失效)
     *
     * @param planId
     */
    @Override
    public void runOnce(String planId, String jobId) throws Exception {
        TaskSchedulerContainer taskSchedulerContainer = TaskSchedulerContainer.getTaskSchedulerContainer(scheduler);
        taskSchedulerContainer.runOnce(planId, jobId);
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
        TaskSchedulerContainer taskSchedulerContainer = TaskSchedulerContainer.getTaskSchedulerContainer(scheduler);
        taskSchedulerContainer.pauseScheduleJob(planId, jobId);
    }

    /**
     * 恢复任务
     *
     * @param planId
     */
    @Override
    public void resumeScheduleJob(String planId, String jobId) throws Exception {
        TaskSchedulerContainer taskSchedulerContainer = TaskSchedulerContainer.getTaskSchedulerContainer(scheduler);
        taskSchedulerContainer.resumeScheduleJob(planId, jobId);
    }
}
