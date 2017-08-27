package com.lei.lib.java.rxhttp.demo;

/**
 * Created by lei on 2017/8/27.
 */

public class BaseBean<T> {
    private int code;
    private String message;
    private T data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
