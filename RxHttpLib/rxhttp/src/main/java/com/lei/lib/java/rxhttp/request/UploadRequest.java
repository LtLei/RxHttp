package com.lei.lib.java.rxhttp.request;

import android.app.Application;

import com.lei.lib.java.rxhttp.entity.RxResponse;
import com.lei.lib.java.rxhttp.progress.ProgressListener;
import com.lei.lib.java.rxhttp.progress.upload.ProgressRequestBody;
import com.lei.lib.java.rxhttp.util.Utilities;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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

    private Map<String, Object> objectParts = new HashMap<>();
    private List<File> fileParts = new ArrayList<>();
    private boolean formData;
    private ProgressListener progressListener;

    public UploadRequest formData(boolean formData) {
        this.formData = formData;
        return this;
    }

    public UploadRequest addFile(File file) {
        Utilities.checkNotNull(file, "file is null.");
        fileParts.add(file);
        return this;
    }

    public UploadRequest addFiles(List<File> files) {
        Utilities.checkNotNull(files, "files is null.");
        fileParts.addAll(files);
        return this;
    }

    public UploadRequest<T> addObjectPart(String key, Object value) {
        Utilities.checkNullOrEmpty(key, "key is null or empty.");
        this.objectParts.put(key, value);
        return this;
    }

    public UploadRequest<T> addObjectParts(HashMap<String, Object> objectParts) {
        Utilities.checkNotNull(objectParts, "objectParts is null.");
        this.objectParts.putAll(objectParts);
        return this;
    }

    public UploadRequest<T> setProgressListener(ProgressListener progressListener) {
        Utilities.checkNotNull(progressListener, "progressListener is null.");
        this.progressListener = progressListener;
        return this;
    }

    @Override
    public Observable<RxResponse<T>> request() {
        return requestOnlyNet();
    }

    @Override
    protected Observable<ResponseBody> netObservable() {
        Utilities.checkNotNull(progressListener, "progressListener is null.");

        boolean fileMode = objectParts.size() == 0;
        MediaType mediaType = MediaType.parse(formData ? "multipart/form-data; charset=utf-8" : "application/octet-stream");

        if (fileMode) {
            List<MultipartBody.Part> files = new ArrayList<>();
            for (int i = 0; i < fileParts.size(); i++) {
                RequestBody requestFile = RequestBody.create(mediaType, fileParts.get(i));
                ProgressRequestBody progressBody = new ProgressRequestBody(requestFile, progressListener);
                String name = fileParts.size() == 1 ? "file" : "file" + i;
                MultipartBody.Part filePart = MultipartBody.Part.createFormData(name, fileParts.get(i).getName(), progressBody);
                files.add(filePart);
            }
            return getApiService().uploadFiles(getRequestUrl(), getParams(), files);
        } else {
            Map<String, RequestBody> bodies = new HashMap<>();
            for (Map.Entry<String, Object> entry : objectParts.entrySet()) {
                MediaType type = MediaType.parse(entry.getValue() instanceof JSONObject ? "application/json; charset=utf-8" : "text/plain");
                RequestBody body = RequestBody.create(type, entry.getValue().toString());
                bodies.put(entry.getKey(), body);
            }
            for (int i = 0; i < fileParts.size(); i++) {
                RequestBody requestFile = RequestBody.create(mediaType, fileParts.get(i));
                ProgressRequestBody progressBody = new ProgressRequestBody(requestFile, progressListener);
                String name = fileParts.size() == 1 ? "file" : "file" + i;
                bodies.put(name, progressBody);
            }
            return getApiService().uploadFiles(getRequestUrl(), getParams(), bodies);
        }
    }
}
