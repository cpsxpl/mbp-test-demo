package com.mbp.eng.framework.jdbc.sql.table;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class LogModel {
    public long logid = -1L;
    public long logtime;
    public String funccode;
    public String funcname;
    public String service;
    public String enterprisecode;
    public String usercode;
    public String userip;
    public String hostip;
    public String sql;
    public long starttime;
    public long endtime = -1L;
    public long costtime;
    public String invokemethod;
    public String invokeejb;
    public String invokehandler;
    public Throwable exception;
}