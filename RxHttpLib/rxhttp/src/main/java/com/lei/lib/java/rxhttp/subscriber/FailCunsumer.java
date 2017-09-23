package com.lei.lib.java.rxhttp.subscriber;

import com.lei.lib.java.rxhttp.exception.ApiException;
import com.lei.lib.java.rxhttp.exception.RxException;
import com.lei.lib.java.rxhttp.util.ThrowableHandler;

import io.reactivex.functions.Consumer;

/**
 * Created by lei on 17-7-21.
 */

public abstract class FailCunsumer implements Consumer<Throwable> {
    @Override
    public void accept(Throwable e) throws Exception {
        Throwable throwable = ThrowableHandler.handleErr(e);
        if (throwable instanceof RxException) {
            _onError((RxException) throwable);
        } else if (throwable instanceof ApiException) {
            _onFail(((ApiException) throwable).getCode(), throwable.getMessage());
        } else {
            _onError(new RxException(RxException.CODE_DEFAULT, throwable.getMessage() == null ? throwable.toString() : throwable.getMessage()));
        }
    }

    public abstract void _onFail(int code, String message);

    public abstract void _onError(RxException e);
}
