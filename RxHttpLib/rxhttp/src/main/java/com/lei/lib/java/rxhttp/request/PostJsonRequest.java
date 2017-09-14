package com.lei.lib.java.rxhttp.request;

import android.app.Application;
import android.text.TextUtils;

import com.lei.lib.java.rxhttp.util.Utilities;

import org.json.JSONObject;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 发起一个GET请求
 *
 * @author lei
 */

public final class PostJsonRequest<T> extends BaseRequest<T, PostJsonRequest<T>> {
    public PostJsonRequest(Application context, String path, Type type) {
        super(context, path, type);
    }

    private String strRequest;

    public PostJsonRequest jsonString(String jsonStr) {
        Utilities.checkNotNull(jsonStr, "jsonStr is null.");
        this.strRequest = jsonStr;
        return this;
    }

    public PostJsonRequest jsonObject(JSONObject jsonObject) {
        Utilities.checkNotNull(jsonObject, "jsonObject is null.");
        this.strRequest = jsonObject.toString();
        return this;
    }

    @Override
    protected Observable<ResponseBody> netObservable() {
        if (TextUtils.isEmpty(this.strRequest)) {
            this.strRequest = new JSONObject(getParams()).toString();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), this.strRequest);
        return getApiService().postJson(getRequestUrl(), body);
    }
}
