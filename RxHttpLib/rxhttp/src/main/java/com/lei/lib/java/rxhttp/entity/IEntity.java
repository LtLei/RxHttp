package com.lei.lib.java.rxhttp.entity;

/**
 * Created by rymyz on 2017/9/13.
 */

public interface IEntity<T> {
    int getCode();

    String getMsg();

    T getData();

    boolean isOk();
}
