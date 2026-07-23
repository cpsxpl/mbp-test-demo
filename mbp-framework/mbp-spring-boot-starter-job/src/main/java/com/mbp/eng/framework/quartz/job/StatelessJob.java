package com.mbp.eng.framework.quartz.job;

import com.mbp.eng.framework.common.Constants;
import com.mbp.eng.framework.common.util.date.DateUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 异步任务工厂
 */
@PersistJobDataAfterExecution
public class StatelessJob extends AbstactJob implements Job {
    private static Logger logger = LoggerFactory.getLogger(StatelessJob.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("================================================================================");
        String previousFireTime = DateUtil.toStrDateFromDateByFormat(jobExecutionContext.getPreviousFireTime(), "yyyy-MM-dd hh:mm:ss");
        String currentTime = DateUtil.getFormatTime(System.currentTimeMillis());
        String nextFireTime = DateUtil.toStrDateFromDateByFormat(jobExecutionContext.getNextFireTime(), "yyyy-MM-dd hh:mm:ss");
        logger.info("StatelessJob.execute previousFireTime:{} currentTime:{} nextFireTime:{}", previousFireTime, currentTime, nextFireTime);

        String userPin = jobExecutionContext.getJobDetail().getJobDataMap().get(Constants.CRON_JOB_USERPIN_KEY).toString();
        String taskInstanceId = jobExecutionContext.getJobDetail().getJobDataMap().get(Constants.CRON_JOB_JOBID_KEY).toString();
        String planId = jobExecutionContext.getJobDetail().getJobDataMap().get(Constants.CRON_JOB_PLANID_KEY).toString();
        //String clusterId = jobExecutionContext.getJobDetail().getJobDataMap().get(Constants.CRON_JOB_CLUSTERID_KEY).toString();
        logger.debug("Scheduler container begin to schedule task or workflow: " + taskInstanceId);
        super.doTask(userPin, taskInstanceId, planId);
    }

    @Override
    public void call(String userPin, String jobId, String planId) throws Exception {
        logger.info("start =======" + jobId);
        Thread.sleep(1000 * 1);
        logger.info("end =======" + jobId);
    }
}