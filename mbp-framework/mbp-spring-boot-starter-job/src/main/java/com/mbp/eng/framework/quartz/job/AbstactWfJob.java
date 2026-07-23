package com.mbp.eng.framework.quartz.job;

import com.mbp.eng.framework.common.util.date.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public abstract class AbstactWfJob {
    private static Logger logger = LoggerFactory.getLogger(AbstactWfJob.class);

    public void doTask(String userPin, String wfId, Date nextFireTime) {
        long time = System.currentTimeMillis();
        logger.info("AbstactWfJob.doTask time:{}", DateUtil.getFormatTime(time));
        try {
            if (!StringUtils.isBlank(wfId)) {
                call(userPin, wfId);
                logger.info("@doTask: " + wfId + " next fire time is : " + nextFireTime);
            } else {
                logger.error("@doTask: wfId=" + wfId);
                throw new IllegalArgumentException("Repeat task id is null.");
            }
        } catch (Exception e) {
            logger.error("@doTask: " + wfId + " start Repeat task failed, will try a again.", e);
            try {
                call(userPin, wfId);
            } catch (Exception e1) {
                logger.error("@doTask: " + wfId + " second time try to repeat task failed, will teminal task.", e1);
            }
        }
    }

    public abstract void call(String userPin, String wfId) throws Exception;
}
