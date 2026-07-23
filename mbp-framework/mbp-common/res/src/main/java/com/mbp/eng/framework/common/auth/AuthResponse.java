package com.mbp.eng.framework.common.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.io.Serializable;

@JsonInclude(Include.NON_EMPTY)
public class AuthResponse<T> implements Serializable {
    private Integer status = 0;
    private String errMsg;
    private T result;

    public AuthResponse() {
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

    public T getResult() {
        return this.result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public void setError(Integer status, String errMsg) {
        this.status = status;
        this.errMsg = errMsg;
    }

    public String toString() {
        return "AuthResponse{status=" + this.status + ", errMsg='" + this.errMsg + '\'' + ", result=" + this.result + '}';
    }
}
