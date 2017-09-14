package com.lei.lib.java.rxhttp.request;

import android.app.Application;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * 发起一个GET请求
 *
 * @author lei
 */

public final class PatchRequest<T> extends BaseRequest<T, PatchRequest<T>> {
    public PatchRequest(Application context, String path, Type type) {
        super(context, path, type);
    }

    @Override
    protected Observable<ResponseBody> netObservable() {
        return getApiService().patch(getRequestUrl(), getParams());
    }
}
