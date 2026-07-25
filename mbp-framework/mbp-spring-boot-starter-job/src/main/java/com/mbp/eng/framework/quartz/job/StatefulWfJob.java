package com.mbp.eng.framework.quartz.job;

import com.mbp.eng.framework.common.Constants;
import com.mbp.eng.framework.common.util.date.DateUtil;
import com.mbp.eng.framework.common.util.json.JsonUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 同步任务工厂
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class StatefulWfJob extends AbstactWfJob implements Job {
    private static Logger logger = LoggerFactory.getLogger(StatefulWfJob.class);

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("================================================================================");
        String previousFireTime = DateUtil.toStrDateFromDateByFormat(jobExecutionContext.getPreviousFireTime(), "yyyy-MM-dd hh:mm:ss");
        String currentTime = DateUtil.getFormatTime(System.currentTimeMillis());
        String nextFireTime = DateUtil.toStrDateFromDateByFormat(jobExecutionContext.getNextFireTime(), "yyyy-MM-dd hh:mm:ss");
        logger.info("StatefulWfJob.execute previousFireTime:{} currentTime:{} nextFireTime:{}", previousFireTime, currentTime, nextFireTime);

        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        String userPin = jobDataMap.getString(Constants.CRON_JOB_USERPIN_KEY);
        String wfId = jobDataMap.getString(Constants.CRON_WF_WFID_KEY);
        super.doTask(userPin, wfId, jobExecutionContext.getNextFireTime());
    }

    @Override
    public void call(String user, String wfId) throws Exception {
        logger.info("StatefulWfJob.call user:{} wfId:{}", user, wfId);

        Map<String, String> headerMaps = new HashMap<>();
        headerMaps.put("Content-type", "application/json");

        Map<String, String> paramMaps = new HashMap<>();
        paramMaps.put("id", wfId);
        paramMaps.put("user", user);

        logger.info("StatefulWfJob.call paramMaps:{}", JsonUtil.toJSON(paramMaps));
    }
}
