package com.mbp.eng.framework.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mbp.eng.framework.common.exception.ErrorStatus;

import java.io.Serializable;

@JsonInclude(Include.NON_NULL)
public class CommonResponse<T> implements Serializable {
    private Integer status;
    private String errMsg;
    private T result;

    public CommonResponse() {
        this.status = ErrorStatus.SUCCESS;
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
}