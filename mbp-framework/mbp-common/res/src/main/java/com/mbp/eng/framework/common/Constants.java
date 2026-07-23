package com.mbp.eng.framework.common;

import java.util.regex.Pattern;

public class Constants {
    public static final String SUCCESS = "0";
    public static final String SUCCESS_CODE = "SUCCESS";
    public static final String SUCCESS_MSG_ADD = "增加:%s 行数据";
    public static final String SUCCESS_MSG_DEL = "删除:%s 行数据";
    public static final String SUCCESS_MSG_UPDATE = ":%s 行数据被修改";
    public static final String FAILED = "-1";
    public static final String FAILED_CODE = "FAILED";
    public static final String FAILED_ERROR = "ERRPR";
    public static final String PARAM_NUlL = "请输入 %s 参数";
    public static final String PARAM_ILLEGAL = "%s 参数不合法";
    public static final String IS_APPLY = "%s 您好,数据库 %s 数据表 %s 已申请,中不能重复申请";
    public static final String PARAM_FILE = "%s 文件不存在";
    public static final String PARAM_DBNAME = "%s 数据库不存在";
    public static final String PARAM_DBNAME_NULL = "没有查询到相关数据库";
    public static final String PARAM_TABLE = "%s 表不存在";
    public static final String PARAM_TABLE_NULL = "%s 数据库,没有查询到表";
    public static final String PARAM_TABLE_METAINFO = "%s 数据库中 %s 数据表,没有查询到表信息";
    public static final String FAILED_EXCEPTION = "系统异常:";
    public static final Pattern pattern_regex_d = Pattern.compile(",");
    public static final Pattern pattern_regex_k = Pattern.compile(" ");
    public static final String REDIS_PREFIX_KEY = "mbp-service_";

    /**
     * Test
     */
    public static final String TEST_CREATE_LOCK_KEY = REDIS_PREFIX_KEY + "TEST_CREATE_LOCK_KEY";

    //----- 本地缓存 相关的Key
    public static final String CACHE_KEY_ADDRESS = REDIS_PREFIX_KEY + "CACHEKEY_ADDRESS";
    public static final String CACHE_KEY_ACTIVE = REDIS_PREFIX_KEY + "CACHEKEY_ACTIVE";
    public static final String TIP = "Value is:[%s]";

    public static final String COLON = ":";

    public static final String UNDERLINE = "_";

    public static final String EQUAL = "=";

    public static final String COMMA = ",";

    public static final String SQL = ".sql";

    /**
     * quartz 相关
     */
    /** 任务调度的参数key */
    public static final String JOB_PARAM_KEY = "jobParam";

    public static final String CRON_JOB_JOBID_KEY = "jobId_key";

    public static final String CRON_JOB_USERPIN_KEY = "userPin_key";

    public static final String CRON_JOB_PLANID_KEY = "planId_key";

    public static final String CRON_JOB_CLUSTERID_KEY = "clusterId_key";

    public static final String CRON_WF_WFID_KEY = "wfId_key";
}
