package com.lei.lib.java.rxhttp.exception;

/**
 * Created by rymyz on 2017/9/12.
 */

public class RxException extends Exception {
    public static final int CODE_DEFAULT = Integer.MAX_VALUE;
    public static final int CODE_HTTP = CODE_DEFAULT-1;

    public static final int CODE_TIMEOUT = CODE_DEFAULT-2;
    public static final int CODE_UN_CONNECT = CODE_DEFAULT-3;
    public static final String MSG_TIMEOUT = "网络链接超时，请检查您的网络状态或稍后重试...";

    public static final int CODE_MALFORM_JSON = CODE_DEFAULT-4;
    public static final String MSG_MALFORM_JSON = "数据解析错误";

    public static final int CODE_NULL = CODE_DEFAULT-5;
    public static final String MSG_NULL = "数据为空";

    private int code;
    private String msg;

    public RxException(int code, String msg) {
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
