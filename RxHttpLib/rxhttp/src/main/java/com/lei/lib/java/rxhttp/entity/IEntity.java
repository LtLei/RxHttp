package com.lei.lib.java.rxhttp.entity;

/**
 * Created by rymyz on 2017/9/13.
 */

public interface IEntity<T> {
    void setCode(int code);

    int getCode();

    String getMsg();

    void setMsg(String msg);

    T getData();

    void setData(T data);

    boolean isOk();
}
