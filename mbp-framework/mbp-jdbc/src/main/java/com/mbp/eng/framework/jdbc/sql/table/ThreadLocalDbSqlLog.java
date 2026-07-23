package com.mbp.eng.framework.jdbc.sql.table;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class ThreadLocalDbSqlLog {
    private static ThreadLocal threadLocal = new ThreadLocal();

    public static void set(LogModel logModel) {
        threadLocal.set(logModel);
    }

    public static LogModel get() {
        LogModel user = (LogModel) threadLocal.get();

        return user;
    }

    public static void remove() {
        threadLocal.remove();
    }

    public static LogModel copy() {
        LogModel logModel = get();

        if (logModel == null) {
            return logModel;
        }

        LogModel logModel2 = new LogModel();
        logModel2.logid = logModel.logid;
        logModel2.logtime = logModel.logtime;
        logModel2.funccode = logModel.funccode;
        logModel2.funcname = logModel.funcname;
        logModel2.service = logModel.service;
        logModel2.enterprisecode = logModel.enterprisecode;
        logModel2.usercode = logModel.usercode;
        logModel2.userip = logModel.userip;
        logModel2.hostip = logModel.hostip;
        logModel2.sql = logModel.sql;
        logModel2.starttime = logModel.starttime;
        logModel2.endtime = logModel.endtime;
        logModel2.costtime = logModel.costtime;
        logModel2.invokemethod = logModel.invokemethod;
        logModel2.invokeejb = logModel.invokeejb;
        logModel2.invokehandler = logModel.invokehandler;
        logModel2.exception = logModel.exception;

        return logModel2;
    }
}