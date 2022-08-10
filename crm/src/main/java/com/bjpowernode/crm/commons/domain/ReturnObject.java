package com.bjpowernode.crm.commons.domain;

/**
 * @Author 临渊
 * @Date 2022-08-10 10:37
 */
public class ReturnObject {
    private String code;    // 处理成功或失败的标签  1  成功  0 失败
    private String message; // 提示信息
    private Object retData; // 其他数据

    public ReturnObject() {
    }

    public ReturnObject(String code, String message, Object retData) {
        this.code = code;
        this.message = message;
        this.retData = retData;
    }

    @Override
    public String toString() {
        return "ReturnObject{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", retData=" + retData +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }
}
