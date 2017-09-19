package com.lei.lib.java.rxhttp.request;

import android.app.Application;

import com.lei.lib.java.rxhttp.entity.RxResponse;
import com.lei.lib.java.rxhttp.interceptors.DownloadProgressInterceptor;
import com.lei.lib.java.rxhttp.progress.ProgressListener;
import com.lei.lib.java.rxhttp.util.Utilities;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
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

    private File outputFile;
    private ProgressListener progressListener;

    public DownloadRequest<T> setOutputFile(File outputFile) {
        Utilities.checkNotNull(outputFile, "file is null.");
        this.outputFile = outputFile;
        return this;
    }

    public DownloadRequest<T> setProgressListener(ProgressListener progressListener) {
        Utilities.checkNotNull(progressListener, "progressListener is null.");
        this.progressListener = progressListener;
        return this;
    }

    @Deprecated
    @Override
    public Observable<RxResponse<T>> request() {
        throw new UnsupportedOperationException("this operation is not support when download file. use excute() instead.");
    }

    public Observable<Boolean> excute() {
        Utilities.checkNotNull(outputFile, "file is null.");
        return netObservable()
                .map(new Function<ResponseBody, Boolean>() {
                    @Override
                    public Boolean apply(ResponseBody responseBody) throws Exception {
                        writeFile(responseBody.byteStream(), outputFile);
                        return true;
                    }
                });
    }

    private void writeFile(InputStream in, File file) throws Exception {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        if (file != null && file.exists())
            file.delete();

        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            byte[] buffer = new byte[1024 * 128];
            int len = -1;
            while ((len = in.read(buffer)) != -1) {
                randomAccessFile.write(buffer, 0, len);
            }
        } finally {
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
        }
    }

    @Override
    protected Observable<ResponseBody> netObservable() {
        return getApiService().downloadFile(getRequestUrl());
    }

    @Override
    protected OkHttpClient.Builder getOkBuilder() {
        Utilities.checkNotNull(progressListener, "progressListener is null.");

        return super.getOkBuilder().addNetworkInterceptor(new DownloadProgressInterceptor(progressListener));
    }
}
