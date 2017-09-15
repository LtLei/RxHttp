package com.lei.lib.java.rxhttp.entity;

import java.io.Serializable;

/**
 * Created by rymyz on 2017/9/13.
 */

public class RxResponse<T> implements Serializable {
    private static final long serialVersionUID = 4729638326639758240L;
    private boolean isLocal;
    private T data;

    public RxResponse(boolean isLocal) {
        this.isLocal = isLocal;
    }

    public RxResponse(boolean isLocal, T data) {
        this.isLocal = isLocal;
        this.data = data;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
