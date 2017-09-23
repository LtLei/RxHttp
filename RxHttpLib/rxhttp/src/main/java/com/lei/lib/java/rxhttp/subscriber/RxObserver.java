package com.lei.lib.java.rxhttp.subscriber;

import com.lei.lib.java.rxhttp.exception.ApiException;
import com.lei.lib.java.rxhttp.exception.RxException;
import com.lei.lib.java.rxhttp.util.ThrowableHandler;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
