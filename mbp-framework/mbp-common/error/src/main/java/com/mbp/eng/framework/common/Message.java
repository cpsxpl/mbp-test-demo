package com.mbp.eng.framework.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.PrintWriter;
import java.io.StringWriter;

import static com.mbp.eng.framework.common.Constants.FAILED_CODE;
import static com.mbp.eng.framework.common.Constants.FAILED_EXCEPTION;
import static com.mbp.eng.framework.common.Constants.SUCCESS_CODE;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    //private String code; // 编码
    private String status; // 状态
    private String result; // 结果
    private Object data; // 数据

    public Message() {
        // 默认构造方法
    }

    public Message(String code, String result) {
        //this.code = code;
        this.status = code;
        this.result = result;
    }

    public Message(String code, Object data) {
        //this.code = code;
        this.status = code;
        this.data = data;
    }

    /*public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }*/

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @SuppressWarnings("unchecked")
    public <E> E getData() {
        return (E) data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static Message create(String code, String msg) {
        return new Message(code, msg);
    }

    public static Message success() {
        return success(SUCCESS_CODE);
    }

    public static Message success(String msg) {
        return create(SUCCESS_CODE, msg);
    }

    public static Message failure() {
        return failure(FAILED_CODE);
    }

    public static Message failure(String msg) {
        return create(FAILED_CODE, msg);
    }

    public static Message failure(Exception exception) {
        return failure(FAILED_EXCEPTION + exception.getMessage(), exception);
    }

    public static Message failure(String message, Exception ex) {
        if (ex == null) {
            return failure();
        }
        Message msg = failure(message);
        try {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            msg.setData(sw.toString());
        } catch (Exception e) {
        }
        return msg;
    }
}
