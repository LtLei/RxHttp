package com.lei.lib.java.rxhttp.exception;

/**
 * Created by rymyz on 2017/9/12.
 */

public class ApiException extends Exception {

    private int code;
    private String msg;

    public ApiException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String getMessage() {
        return code + "#" + msg;
    }
}
