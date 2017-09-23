package com.lei.lib.java.rxhttp.demo;

import com.lei.lib.java.rxhttp.entity.IEntity;

/**
 * Created by lei on 2017/8/27.
 */

public class BaseBean<T> implements IEntity<T> {
    private int code;
    private String message;
    private T data;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return message;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public boolean isOk() {
        return 1000 == code;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data.toString() +
                '}';
    }
}
