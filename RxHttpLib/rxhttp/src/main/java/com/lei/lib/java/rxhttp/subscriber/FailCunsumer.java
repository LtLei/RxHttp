package com.lei.lib.java.rxhttp.subscriber;

import android.util.MalformedJsonException;

import com.lei.lib.java.rxhttp.exception.ApiException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.exceptions.CompositeException;
import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

/**
 * Created by lei on 17-7-21.
 */

public abstract class FailCunsumer implements Consumer<Throwable> {
    @Override
    public void accept(Throwable e) throws Exception {
        if (e instanceof NullPointerException) {
            _onFail(ApiException.CODE_NULL, ApiException.MSG_NULL);
        } else if (e instanceof HttpException) {
            _onFail(((HttpException) e).code(), ((HttpException) e).message());
        } else if (e instanceof SocketTimeoutException) {
            _onFail(ApiException.CODE_TIMEOUT, ApiException.MSG_TIMEOUT);
        } else if (e instanceof ConnectException) {
            _onFail(ApiException.CODE_UN_CONNECT, ApiException.MSG_TIMEOUT);
        } else if (e instanceof UnknownHostException) {
            _onFail(ApiException.CODE_UN_CONNECT, ApiException.MSG_TIMEOUT);
        } else if (e instanceof MalformedJsonException) {
            _onFail(ApiException.CODE_MALFORM_JSON, ApiException.MSG_MALFORM_JSON);
        } else if (e instanceof CompositeException) {
            CompositeException compositeE = (CompositeException) e;
            for (Throwable throwable : compositeE.getExceptions()) {
                if (throwable instanceof SocketTimeoutException) {
                    _onFail(ApiException.CODE_TIMEOUT, ApiException.MSG_TIMEOUT);
                } else if (throwable instanceof ConnectException) {
                    _onFail(ApiException.CODE_UN_CONNECT, ApiException.MSG_TIMEOUT);
                } else if (throwable instanceof UnknownHostException) {
                    _onFail(ApiException.CODE_UN_CONNECT, ApiException.MSG_TIMEOUT);
                } else if (throwable instanceof MalformedJsonException) {
                    _onFail(ApiException.CODE_MALFORM_JSON, ApiException.MSG_MALFORM_JSON);
                } else {
                    _onFail(ApiException.CODE_DEFAULT, e.getMessage() == null ? e.toString() : e.getMessage());
                }
            }
        } else {
            String msg = e.getMessage();
            int code;
            if (msg != null && !msg.isEmpty()) {
                if (msg.contains("#")) {
                    code = Integer.parseInt(msg.split("#")[0]);
                    _onFail(code, msg.split("#")[1]);
                } else {
                    code = ApiException.CODE_DEFAULT;
                    _onFail(code, msg);
                }
            } else {
                code = ApiException.CODE_DEFAULT;
                _onFail(code, e.toString());
            }
        }
    }

    public abstract void _onFail(int code, String message);
}
