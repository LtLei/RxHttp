package com.lei.lib.java.rxhttp.request;

import android.app.Application;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * 发起一个GET请求
 *
 * @author lei
 */

public class GetRequest extends BaseRequest {
    public GetRequest(Application context) {
        super(context);
    }

    @Override
    protected Observable<ResponseBody> netObservable() {
        return getApiService().get(getRequestUrl(), getParams());
    }
}
