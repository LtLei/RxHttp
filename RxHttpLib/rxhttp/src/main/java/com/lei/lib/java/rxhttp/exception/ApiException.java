package com.lei.lib.java.rxhttp.exception;

/**
 * Created by rymyz on 2017/9/12.
 */

public class ApiException extends Exception {
    public static final int CODE_DEFAULT = -1;
    public static final int CODE_HTTP = 10000;

    public static final int CODE_TIMEOUT = 10001;
    public static final int CODE_UN_CONNECT = 10002;
    public static final String MSG_TIMEOUT = "网络链接超时，请检查您的网络状态或稍后重试...";

    public static final int CODE_MALFORM_JSON = 10003;
    public static final String MSG_MALFORM_JSON = "数据解析错误";

    public static final int CODE_NULL = 10004;
    public static final String MSG_NULL = "数据为空";

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
