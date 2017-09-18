package com.lei.lib.java.rxhttp.subscriber;

import android.util.MalformedJsonException;

import com.lei.lib.java.rxhttp.exception.ApiException;
import com.lei.lib.java.rxhttp.exception.RxException;
import com.lei.lib.java.rxhttp.util.NetworkUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import retrofit2.HttpException;

/**
 * 作者：lei on 2017/9/18.
 * 邮箱：fighting_our_life@foxmail.com
 * 版本：1.0.0
 */

public abstract class RxObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof NullPointerException) {
            _onFail(RxException.CODE_NULL, RxException.MSG_NULL);
        } else if (e instanceof HttpException) {
            if (404 == ((HttpException) e).code()) {
                _onFail(404, "您请求的地址已经不存在了");
            } else if (500 == ((HttpException) e).code()) {
                _onFail(500, "服务器出了点问题，请您稍后再试吧...");
            } else _onFail(((HttpException) e).code(), ((HttpException) e).message());
        } else if (e instanceof SocketTimeoutException) {
            _onFail(RxException.CODE_TIMEOUT, RxException.MSG_TIMEOUT);
        } else if (e instanceof ConnectException) {
            _onFail(RxException.CODE_UN_CONNECT, RxException.MSG_TIMEOUT);
        } else if (e instanceof UnknownHostException) {
            _onFail(RxException.CODE_UN_CONNECT, RxException.MSG_TIMEOUT);
        } else if (e instanceof MalformedJsonException) {
            _onFail(RxException.CODE_MALFORM_JSON, RxException.MSG_MALFORM_JSON);
        } else if (e instanceof CompositeException) {
            CompositeException compositeE = (CompositeException) e;
            for (Throwable throwable : compositeE.getExceptions()) {
                if (throwable instanceof SocketTimeoutException) {
                    _onFail(RxException.CODE_TIMEOUT, RxException.MSG_TIMEOUT);
                } else if (throwable instanceof ConnectException) {
                    _onFail(RxException.CODE_UN_CONNECT, RxException.MSG_TIMEOUT);
                } else if (throwable instanceof UnknownHostException) {
                    _onFail(RxException.CODE_UN_CONNECT, RxException.MSG_TIMEOUT);
                } else if (throwable instanceof MalformedJsonException) {
                    _onFail(RxException.CODE_MALFORM_JSON, RxException.MSG_MALFORM_JSON);
                } else {
                    _onFail(RxException.CODE_DEFAULT, e.getMessage() == null ? e.toString() : e.getMessage());
                }
            }
        } else if (e instanceof ApiException) {
            _onFail(((ApiException) e).getCode(), ((ApiException) e).getMsg());
        } else {
            String msg = e.getMessage();
            int code;
            if (msg != null && !msg.isEmpty()) {
                if (msg.contains("#")) {
                    code = Integer.parseInt(msg.split("#")[0]);
                    _onFail(code, msg.split("#")[1]);
                } else {
                    code = RxException.CODE_DEFAULT;
                    _onFail(code, msg);
                }
            } else {
                code = RxException.CODE_DEFAULT;
                _onFail(code, e.toString());
            }
        }
    }

    public abstract void _onFail(int code, String message);
}
