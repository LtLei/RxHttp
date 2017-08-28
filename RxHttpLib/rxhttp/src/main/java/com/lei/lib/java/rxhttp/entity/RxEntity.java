package com.lei.lib.java.rxhttp.entity;

/**
 * Created by rymyz on 2017/8/28.
 */

public class RxEntity<T> {
    private int code;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "RxEntity{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public void setData(T data) {
        this.data = data;
    }
}
