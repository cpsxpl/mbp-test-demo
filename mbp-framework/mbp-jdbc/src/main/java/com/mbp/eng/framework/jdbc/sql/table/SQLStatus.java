package com.mbp.eng.framework.jdbc.sql.table;

import java.io.Serializable;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class SQLStatus implements Serializable {
    private String id;
    private String sql;
    private int status;
    private long startTime;
    private long endTime;
    private String result;
    private String invokeFrom;
    private String invokeEJB;
    private Throwable exception;
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISH = 1;
    public static final int STATUS_ERROR = 2;

    public SQLStatus(String id, String sql) {
        this.id = id;
        this.sql = sql;
    }

    public long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public Throwable getException() {
        return this.exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvokeEJB() {
        return this.invokeEJB;
    }

    public void setInvokeEJB(String invokeEJB) {
        this.invokeEJB = invokeEJB;
    }

    public String getInvokeFrom() {
        return this.invokeFrom;
    }

    public void setInvokeFrom(String invokeFrom) {
        this.invokeFrom = invokeFrom;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}