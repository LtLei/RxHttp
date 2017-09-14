package com.lei.lib.java.rxhttp.request;

import android.app.Application;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 发起一个GET请求
 *
 * @author lei
 */

public final class UploadRequest<T> extends BaseRequest<T, UploadRequest<T>> {
    public UploadRequest(Application context, String path, Type type) {
        super(context, path, type);
    }

    private Map<String, Object> bodyParams = new LinkedHashMap<>();
    private List<File> files = new LinkedList<>();

    public UploadRequest addFile(File file) {
        this.files.add(file);
        return this;
    }

    @Override
    protected Observable<ResponseBody> netObservable() {
        RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream; charset=utf-8"), files);
        return getApiService().uploadFiles(getRequestUrl(), getParams(), body);
    }
}
