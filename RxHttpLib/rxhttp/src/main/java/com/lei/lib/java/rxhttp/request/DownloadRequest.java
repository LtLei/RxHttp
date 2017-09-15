package com.lei.lib.java.rxhttp.request;

import android.app.Application;

import com.lei.lib.java.rxhttp.entity.RxResponse;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * 发起一个GET请求
 *
 * @author lei
 */

public final class DownloadRequest<T> extends BaseRequest<T, DownloadRequest<T>> {
    public DownloadRequest(Application context, String path, Type type) {
        super(context, path, type);
    }

    @Deprecated
    @Override
    public Observable<RxResponse<T>> request() {
        throw new UnsupportedOperationException("this operation is not support when download file. use excute() instead.");
    }

    public Observable<ResponseBody> excute() {
        return netObservable();
    }

    @Override
    protected Observable<ResponseBody> netObservable() {
        return getApiService().downloadFile(getRequestUrl());
    }
}
