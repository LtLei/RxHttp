package com.lei.lib.java.rxhttp.entity;

import java.io.Serializable;

/**
 * Created by rymyz on 2017/8/28.
 */

public class RxEntity<T> implements IEntity, Serializable {

    private static final long serialVersionUID = 1119086637204832080L;
    private int code;
    private String msg;
    private T data;

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public boolean isOk() {
        return 2000 == code;
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
