package com.mbp.eng.framework.jdbc.sql.exception;

public class JDBCException extends RuntimeException {
    private Integer status;
    private Object[] params;

    public JDBCException() {
    }

    public JDBCException(Integer code, String msg, Object... params) {
        super(msg);
        this.status = code;
        this.params = params;
    }

    public JDBCException(Integer code, String msg, Exception e, Object... parms) {
        super(msg, e);
        this.status = code;
        this.params = parms;
    }

    public JDBCException(Integer code, String msg, Throwable t, Object... params) {
        super(msg, t);
        this.status = code;
        this.params = params;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Object[] getParams() {
        return this.params;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
