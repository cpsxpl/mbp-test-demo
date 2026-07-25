package com.mbp.eng.framework.common.exception;

public class MbpException extends RuntimeException {
    private Integer status;
    private Object[] params;

    public MbpException() {
    }

    public MbpException(Integer code, String msg, Object... params) {
        super(msg);
        this.status = code;
        this.params = params;
    }

    public MbpException(Integer code, String msg, Exception e, Object... parms) {
        super(msg, e);
        this.status = code;
        this.params = parms;
    }

    public MbpException(Integer code, String msg, Throwable t, Object... params) {
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
