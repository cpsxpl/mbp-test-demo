package com.mbp.eng.framework.quartz.job;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstactJob {
    private String className = this.getClass().getName();
    private String simpleClassName = this.getClass().getSimpleName();
    private static Logger logger = LoggerFactory.getLogger(AbstactJob.class);

    public void doTask(String user, String jobId, String planId) {
        try {
            if (!StringUtils.isBlank(jobId)) {
                call(user, jobId, planId);
            } else {
                logger.error("taskInstanceId=" + jobId);
                throw new IllegalArgumentException("Repeat task id is null.");
            }
        } catch (Exception e) {
            logger.error(jobId + " start Repeat task failed, will try a again.", e);
            try {
                call(user, jobId, planId);
            } catch (Exception e1) {
                logger.error(jobId + " second time try to repeat task failed, will teminal task.", e1);
            }
        }
    }

    public abstract void call(String user, String jobId, String planId) throws Exception;
}
