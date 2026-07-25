package com.mbp.eng.framework.common.exception.auth;

public class AuthException extends RuntimeException {
    private final Integer status;
    private Object[] params;

    public AuthException(Integer code, String msg) {
        super(msg + "(" + code + ")");
        this.status = code;
        this.params = null;
    }

    public AuthException(Integer code, String msg, Exception e) {
        super(msg + "(" + code + ")", e);
        this.status = code;
        this.params = null;
    }

    public AuthException(Integer code, String msg, Throwable t) {
        super(msg + "(" + code + ")", t);
        this.status = code;
        this.params = null;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Object[] getParams() {
        return this.params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
