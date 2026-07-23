package com.mbp.eng.framework.quartz;

import com.mbp.eng.framework.common.Constants;
import com.mbp.eng.framework.common.util.date.DateUtil;
import com.mbp.eng.framework.quartz.job.StatefulWfJob;
import com.mbp.eng.framework.quartz.job.StatefulJob;
import com.mbp.eng.framework.quartz.job.StatelessJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * quartz scheduler管理类
 */
public final class TaskSchedulerContainer {
    private static final Logger logger = LoggerFactory.getLogger(TaskSchedulerContainer.class);
    //private JobBuilder jobBuilder;
    //private TriggerBuilder triggerBuilder;
    private Scheduler scheduler;
    private static TaskSchedulerContainer taskSchedulerContainer;

    private TaskSchedulerContainer(Scheduler scheduler) {
        //this.jobBuilder = new JobBuilder();
        //this.triggerBuilder = new TriggerBuilder();
        this.scheduler = scheduler;
    }

    public static TaskSchedulerContainer getTaskSchedulerContainer(Scheduler scheduler) {
        long time = System.currentTimeMillis();
        logger.info("TaskSchedulerContainer.getTaskSchedulerContainer time:{}", DateUtil.getFormatTime(time));
        if (null == taskSchedulerContainer) {
            synchronized (TaskSchedulerContainer.class) {
                if (null == taskSchedulerContainer) {
                    taskSchedulerContainer = new TaskSchedulerContainer(scheduler);
                }
            }
        }
        return taskSchedulerContainer;
    }

    /**
     * 添加quartz task for job
     *
     * @param user
     * @param planId
     * @param jobId
     * @param cronExpression
     * @throws SchedulerException
     */
    public void addTask(String user, String planId, String jobId, String cronExpression) throws SchedulerException {
        logger.info("TaskSchedulerContainer.addTask user:{} planId:{} jobId:{} cronExpression:{}", user, planId, jobId, cronExpression);
        Integer priotity = 5;
        Integer selfDenpendence = 1;
//        JobDetail jobDetail = this.jobBuilder.buildJob(planId, jobId, selfDenpendence);
        Job job = null;
        if (1 == selfDenpendence) {
            job = new StatefulJob();
        } else {
            job = new StatelessJob();
        }
        JobDetail jobDetail = newJob(job.getClass()).withIdentity(jobId, planId).build();
        jobDetail.getJobDataMap().put(Constants.CRON_JOB_JOBID_KEY, jobId);
        jobDetail.getJobDataMap().put(Constants.CRON_JOB_USERPIN_KEY, user);
        jobDetail.getJobDataMap().put(Constants.CRON_JOB_PLANID_KEY, planId);
        //jobDetail.getJobDataMap().put(Constants.CRON_JOB_CLUSTERID_KEY, clusterId);
//        Trigger trigger = this.triggerBuilder.buildTrigger(planId, jobId, cronExpression, priotity);
        Trigger trigger = newTrigger()
                .withIdentity(jobId, planId)
                .withSchedule(cronSchedule(cronExpression).withMisfireHandlingInstructionFireAndProceed())
                .withPriority(priotity).build();
        addToSchedulerContianer(jobDetail, trigger);
    }

    /**
     * 添加quartz task for workflow
     *
     * @param user
     * @param wfId
     * @param cronExpression
     * @throws SchedulerException
     */
    public void addWfTask(String user, String wfId, String cronExpression) throws SchedulerException {
        logger.info("TaskSchedulerContainer.addWfTask user:{} wfId:{} cronExpression:{}", user, wfId, cronExpression);
        Integer priotity = 5;
        Integer selfDenpendence = 1;
        Job job = null;
        if (1 == selfDenpendence) {
            job = new StatefulWfJob();
        } else {
            job = new StatelessJob();
        }
        JobDetail jobDetail = newJob(job.getClass()).withIdentity(wfId, wfId).build();
        jobDetail.getJobDataMap().put(Constants.CRON_JOB_USERPIN_KEY, user);
        jobDetail.getJobDataMap().put(Constants.CRON_WF_WFID_KEY, wfId);

        Trigger trigger = newTrigger()
                .withIdentity(wfId, wfId)
                .withSchedule(cronSchedule(cronExpression).withMisfireHandlingInstructionFireAndProceed())
                .withPriority(priotity).build();
        addToSchedulerContianer(jobDetail, trigger);
    }

    /**
     * 加载到quartz调度器中
     *
     * @param jobDetail
     * @param trigger
     * @throws SchedulerException
     */
    private void addToSchedulerContianer(JobDetail jobDetail, Trigger trigger) throws SchedulerException {
        long time = System.currentTimeMillis();
        logger.info("TaskSchedulerContainer.addToSchedulerContianer time:{}", DateUtil.getFormatTime(time));
        JobKey jobKey = jobDetail.getKey();
        boolean isJobExisting = isJobExisting(jobKey);
        TriggerKey triggerKey = trigger.getKey();
        boolean isTriggerExisting = scheduler.checkExists(triggerKey);
        if (isJobExisting && isTriggerExisting) {
            logger.warn("Job and trigger has already in scheduler container.");
            throw new RuntimeException("JobName=" + jobDetail.getKey().getName() + ", job groupName=" + jobDetail.getKey().getGroup());
        }

        if (isTriggerExisting) {
            logger.warn("Trigger has already in scheduler container, please reselect trigger ID");
            this.scheduler.resumeTrigger(trigger.getTriggerBuilder().forJob(jobDetail.getKey().getName(), jobDetail.getKey().getGroup()).build().getKey());
            return;
        }

        if (isJobExisting) {
            trigger = trigger.getTriggerBuilder().forJob(jobDetail.getKey().getName(), jobDetail.getKey().getGroup()).build();
            this.scheduler.scheduleJob(trigger);
            return;
        }

        // 1）注册对特定作业的 JobListener
        //scheduler.getListenerManager().addJobListener(new MyJobListener(), KeyMatcher.keyEquals(jobKey));
        //2）注册对特定组的所有作业的 JobListener
        //scheduler.getListenerManager().addJobListener(new MyJobListener(), GroupMatcher.jobGroupEquals("group1"));
        //3）注册对两个特定组的所有作业的 JobListener
        //scheduler.getListenerManager().addJobListener(new MyJobListener(), OrMatcher.or(GroupMatcher.jobGroupEquals("group1"), GroupMatcher.jobGroupEquals("group2")));
        //4）注册一个对所有作业的 JobListener
        //scheduler.getListenerManager().addJobListener(new MyJobListener(), EverythingMatcher.allJobs());

        scheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 删除定时任务
     *
     * @return
     * @throws SchedulerException
     */
    public boolean deleteScheduleJob(String planId, String jobId) throws SchedulerException {
        logger.info("TaskSchedulerContainer.deleteScheduleJob planId:{}", planId);
        JobKey jobKey = getJobKey(planId, jobId);
        if (!isJobExisting(jobKey)) {
            logger.warn("Job not found in container, planId=" + planId);
            return false;
        }
        //停止触发器
        scheduler.pauseTrigger(getTriggerKey(planId, jobId));
        //移除触发器
        scheduler.unscheduleJob(getTriggerKey(planId, jobId));
        return this.scheduler.deleteJob(jobKey); //
    }

    /**
     * 删除定时任务 for workflow
     *
     * @return
     * @throws SchedulerException
     */
    public boolean deleteWfScheduleJob(String wfId) throws SchedulerException {
        logger.info("TaskSchedulerContainer.deleteWfScheduleJob wfId:{}", wfId);
        JobKey jobKey = getJobKey(wfId, wfId);
        if (!isJobExisting(jobKey)) {
            logger.warn("@deleteWfScheduleJob: Job not found in container, wfId=" + wfId);
            return false;
        }
        //停止触发器
        scheduler.pauseTrigger(getTriggerKey(wfId, wfId));
        //移除触发器
        scheduler.unscheduleJob(getTriggerKey(wfId, wfId));
        return this.scheduler.deleteJob(jobKey); //
    }

    /**
     * 运行一次任务
     */
    public void runOnce(String planId, String jobId) throws Exception {
        logger.info("TaskSchedulerContainer.runOnce planId:{} jobId:{}", planId, jobId);
        JobKey jobKey = getJobKey(planId, jobId);
        if (!isJobExisting(jobKey)) {
            logger.warn("Job not found in container, planId:{} jobId:{}", planId, jobId);
            return;
        }
        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            logger.error("运行一次定时任务失败", e);
            throw new RuntimeException("运行一次定时任务失败");
        }
    }

    /**
     * 暂停任务
     *
     * @param planId
     */
    public void pauseScheduleJob(String planId, String jobId) throws Exception {
        logger.info("TaskSchedulerContainer.pauseScheduleJob planId:{} jobId:{}", planId, jobId);
        JobKey jobKey = getJobKey(planId, jobId);
        if (!isJobExisting(jobKey)) {
            logger.warn("Job not found in container, planId:{} jobId:{}", planId, jobId);
            return;
        }
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            logger.error("暂停定时任务失败", e);
            throw new RuntimeException("暂停定时任务失败");
        }
    }

    /**
     * 恢复任务
     *
     * @param planId
     */
    public void resumeScheduleJob(String planId, String jobId) throws Exception {
        logger.info("TaskSchedulerContainer.resumeScheduleJob planId:{} jobId:{}", planId, jobId);
        JobKey jobKey = getJobKey(planId, jobId);
        if (!isJobExisting(jobKey)) {
            logger.warn("Job not found in container, planId:{} jobId:{}", planId, jobId);
            return;
        }
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            logger.error("暂停定时任务失败", e);
            throw new RuntimeException("暂停定时任务失败");
        }
    }

    /**
     * 更新定时任务(只能更新cronExpression)
     *
     * @param cronExpression the cronExpression expression
     */
    public void updateScheduleJob(String planId, String jobId, String cronExpression) {
        logger.info("TaskSchedulerContainer.updateScheduleJob planId:{} jobId:{} cronExpression:{}", planId, jobId, cronExpression);
        try {
            //更新参数 实际测试中发现无法更新
//            JobDataMap jobDataMap = jobDetail.getJobDataMap();
//            jobDataMap.put(ScheduleJobVo.JOB_PARAM_KEY, param);
//            jobDetail.getJobBuilder().usingJobData(jobDataMap);

            TriggerKey triggerKey = getTriggerKey(planId, jobId);
            //表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            //按新的cronExpression表达式重新构建trigger
            cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            Trigger.TriggerState triggerState = scheduler.getTriggerState(cronTrigger.getKey());
            // 忽略状态为PAUSED的任务，解决集群环境中在其他机器设置定时任务为PAUSED状态后，集群环境启动另一台主机时定时任务全被唤醒的bug
            if (!triggerState.name().equalsIgnoreCase("PAUSED")) {
                //按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, cronTrigger);
            }
        } catch (SchedulerException e) {
            logger.error("更新定时任务失败", e);
            throw new RuntimeException("更新定时任务失败");
        }
    }

    /**
     * 获取表达式触发器
     *
     * @param
     * @return cronExpression trigger
     */
    public CronTrigger getCronTrigger(String planId, String jobId) {
        logger.info("TaskSchedulerContainer.getCronTrigger planId:{} jobId:{}", planId, jobId);
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobId, planId);
            return (CronTrigger) scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            logger.error("获取定时任务CronTrigger出现异常", e);
            throw new RuntimeException("获取定时任务CronTrigger出现异常");
        }
    }

    public boolean isJobExisting(JobKey jobKey) throws SchedulerException {
        return this.scheduler.checkExists(jobKey);
    }

    public JobKey getJobKey(String planId, String jobId) {
        logger.info("TaskSchedulerContainer.getJobKey planId:{} jobId:{}", planId, jobId);
        return new JobKey(jobId, planId);
    }

    public JobKey getJobKey(String jobId) {
        logger.info("TaskSchedulerContainer.getJobKey jobId:{}", jobId);
        return new JobKey(jobId, jobId);
    }

    /**
     * 构建trigger，TRIGGER_NAME：jobId， TRIGGER_GROUP：planId
     *
     * @param planId
     * @param jobId
     * @return
     */
    public TriggerKey getTriggerKey(String planId, String jobId) {
        return TriggerKey.triggerKey(jobId, planId);
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void printJob() throws SchedulerException {
        for (String groupName : scheduler.getJobGroupNames()) {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                String jobName = jobKey.getName();
                String jobGroup = jobKey.getGroup();
                logger.info("[jobName] : " + jobName + " [groupName] : " + jobGroup + ".");
                List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                if (triggers == null) {
                    logger.info("------->>>[jobName] : " + jobName + " [groupName] : " + jobGroup + "." + " has no trigger");
                } else {
                    for (Trigger trigger : triggers) {
                        logger.info("------->>>[jobName] : " + jobName + " [groupName] : " + jobGroup + "." + " [trigger] : " + trigger.getNextFireTime());
                    }
                }
            }
        }
    }
}