package com.lei.lib.java.rxhttp.util;

import android.util.MalformedJsonException;

import com.lei.lib.java.rxhttp.exception.ApiException;
import com.lei.lib.java.rxhttp.exception.RxException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.exceptions.CompositeException;
import retrofit2.HttpException;

/**
 * Created by lei on 2017/9/23.
 */

public class ThrowableHandler {
    public static Throwable handleErr(Throwable e) {
        Throwable mThrowable = new RxException(RxException.CODE_DEFAULT, "");
        if (e instanceof NullPointerException) {
            mThrowable = new RxException(RxException.CODE_NULL, RxException.MSG_NULL);
        } else if (e instanceof HttpException) {
            mThrowable = new RxException(((HttpException) e).code(), "服务器出了点问题，请您稍后再试吧...");
        } else if (e instanceof SocketTimeoutException) {
            mThrowable = new RxException(RxException.CODE_TIMEOUT, RxException.MSG_TIMEOUT);
        } else if (e instanceof ConnectException) {
            mThrowable = new RxException(RxException.CODE_UN_CONNECT, RxException.MSG_TIMEOUT);
        } else if (e instanceof UnknownHostException) {
            mThrowable = new RxException(RxException.CODE_UN_CONNECT, RxException.MSG_TIMEOUT);
        } else if (e instanceof MalformedJsonException) {
            mThrowable = new RxException(RxException.CODE_MALFORM_JSON, RxException.MSG_MALFORM_JSON);
        } else if (e instanceof CompositeException) {
            CompositeException compositeE = (CompositeException) e;
            for (Throwable throwable : compositeE.getExceptions()) {
                if (throwable instanceof SocketTimeoutException) {
                    mThrowable = new RxException(RxException.CODE_TIMEOUT, RxException.MSG_TIMEOUT);
                } else if (throwable instanceof ConnectException) {
                    mThrowable = new RxException(RxException.CODE_UN_CONNECT, RxException.MSG_TIMEOUT);
                } else if (throwable instanceof UnknownHostException) {
                    mThrowable = new RxException(RxException.CODE_UN_CONNECT, RxException.MSG_TIMEOUT);
                } else if (throwable instanceof MalformedJsonException) {
                    mThrowable = new RxException(RxException.CODE_MALFORM_JSON, RxException.MSG_MALFORM_JSON);
                } else {
                    mThrowable = new RxException(RxException.CODE_DEFAULT, e.getMessage() == null ? e.toString() : e.getMessage());
                }
            }
        } else if (e instanceof ApiException) {
            mThrowable = e;
        } else {
            mThrowable = new RxException(RxException.CODE_DEFAULT, e.getMessage() == null ? e.toString() : e.getMessage());
        }

        return mThrowable;
    }
}
