package com.lei.lib.java.rxhttp.request;

import android.app.Application;

import com.lei.lib.java.rxhttp.entity.RxResponse;
import com.lei.lib.java.rxhttp.interceptors.DownloadProgressInterceptor;
import com.lei.lib.java.rxhttp.progress.ProgressListener;
import com.lei.lib.java.rxhttp.util.Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
                .map(new Function<ResponseBody, InputStream>() {
                    @Override
                    public InputStream apply(ResponseBody responseBody) throws Exception {
                        return responseBody.byteStream();
                    }
                })
                .map(new Function<InputStream, Boolean>() {
                    @Override
                    public Boolean apply(InputStream inputStream) throws Exception {
                        return writeFile(inputStream, outputFile);
                    }
                });
    }

    private boolean writeFile(InputStream in, File file) {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        if (file != null && file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 128];
            int len = -1;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            out.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
