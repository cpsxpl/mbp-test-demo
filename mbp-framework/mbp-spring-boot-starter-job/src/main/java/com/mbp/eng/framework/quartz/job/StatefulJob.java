package com.mbp.eng.framework.quartz.job;

import com.mbp.eng.framework.common.Constants;
import com.mbp.eng.framework.common.util.date.DateUtil;
import org.apache.http.message.BasicNameValuePair;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 同步任务工厂
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class StatefulJob extends AbstactJob implements Job {
    private static Logger logger = LoggerFactory.getLogger(StatefulJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("================================================================================");
        String previousFireTime = DateUtil.toStrDateFromDateByFormat(jobExecutionContext.getPreviousFireTime(), "yyyy-MM-dd hh:mm:ss");
        String currentTime = DateUtil.getFormatTime(System.currentTimeMillis());
        String nextFireTime = DateUtil.toStrDateFromDateByFormat(jobExecutionContext.getNextFireTime(), "yyyy-MM-dd hh:mm:ss");
        logger.info("StatefulJob.execute previousFireTime:{} currentTime:{} nextFireTime:{}", previousFireTime, currentTime, nextFireTime);

        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String user = jobDataMap.getString(Constants.CRON_JOB_USERPIN_KEY);
        String jobId = jobDataMap.getString(Constants.CRON_JOB_JOBID_KEY);
        String planId = jobDataMap.getString(Constants.CRON_JOB_PLANID_KEY);
        super.doTask(user, jobId, planId);
    }

    public void call(String user, String jobId, String planId) throws Exception {
        List list = new ArrayList();
        list.add(new BasicNameValuePair("user", user));
        list.add(new BasicNameValuePair("jobId", jobId));
        list.add(new BasicNameValuePair("planId", planId));
        //logger.info("StatefulJob.call list:{}", JsonUtil.toJSON(list));

        Random random = new Random(jobId.hashCode());
        int sleeptime = random.nextInt(20) * 50;
        logger.info("StatefulJob.call sleeptime:{}", sleeptime);
        TimeUnit.MILLISECONDS.sleep(sleeptime);
    }
}
