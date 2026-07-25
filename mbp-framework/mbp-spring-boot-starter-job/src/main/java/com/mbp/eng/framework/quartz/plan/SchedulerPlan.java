package com.mbp.eng.framework.quartz.plan;

public interface SchedulerPlan {
    /**
     * 创建定时任务
     *
     * @param user
     * @param planId
     * @param jobId
     * @param cronExpression
     * @throws Exception
     */
    void createPlan(String user, String planId, String jobId, String cronExpression) throws Exception;

    /**
     * 删除定时任务
     *
     * @param planId
     * @param jobId
     * @throws Exception
     */
    void deletePlan(String planId, String jobId) throws Exception;

    /**
     * 更新定时任务
     * 只能修改运行的时间,参数、同异步等无法修改
     *
     * @param planId
     * @param jobId
     * @param cronExpression
     */
    void updatePlan(String planId, String jobId, String cronExpression);

    /**
     * 运行一次任务(单次执行任务运行一次后状态变为失效)
     *
     * @param planId
     * @param jobId
     * @throws Exception
     */
    void runOnce(String planId, String jobId) throws Exception;

    /**
     * 暂停任务
     *
     * @param planId
     * @param jobId
     * @throws Exception
     */
    void pauseScheduleJob(String planId, String jobId) throws Exception;

    /**
     * 恢复任务
     *
     * @param planId
     * @param jobId
     * @throws Exception
     */
    void resumeScheduleJob(String planId, String jobId) throws Exception;
}
