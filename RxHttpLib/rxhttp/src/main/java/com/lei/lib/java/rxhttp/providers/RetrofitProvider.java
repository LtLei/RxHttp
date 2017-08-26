package com.lei.lib.java.rxhttp.providers;

import com.lei.lib.java.rxhttp.util.Utilities;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit管理工具类
 *
 * @author lei
 */

public class RetrofitProvider {
    private Retrofit.Builder retrofitBuilder;
    private String baseStringUrl = "";
    private HttpUrl baseHttpUrl;
    private List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>();
    private List<Converter.Factory> converterFactories = new ArrayList<>();
    private OkHttpClient okHttpClient;

    public RetrofitProvider() {
        retrofitBuilder = new Retrofit.Builder();
    }

    public RetrofitProvider(Retrofit.Builder retrofitBuilder) {
        this.retrofitBuilder = Utilities.checkNotNull(retrofitBuilder, "retrofitBuilder is null");
    }

    public void setBaseUrl(String url) {
        this.baseStringUrl = Utilities.checkNullOrEmpty(url, "url is null or empty.");
    }

    public void setBaseUrl(HttpUrl httpUrl) {
        this.baseHttpUrl = Utilities.checkNotNull(httpUrl, "httpUrl is null.");
    }

    public void addCallAdapterFactory(CallAdapter.Factory factory) {
        this.callAdapterFactories.add(Utilities.checkNotNull(factory, "factory is null."));
    }

    public void addCallAdapterFactories(List<CallAdapter.Factory> factories) {
        this.callAdapterFactories.addAll(Utilities.checkNotNull(factories, "factories is null."));
    }

    public void addConverterFactory(Converter.Factory factory) {
        this.converterFactories.add(Utilities.checkNotNull(factory, "factory is null."));
    }

    public void addConverterFactories(List<Converter.Factory> factories) {
        this.converterFactories.addAll(Utilities.checkNotNull(factories, "factories is null."));
    }

    public void setClient(OkHttpClient okHttpClient) {
        this.okHttpClient = Utilities.checkNotNull(okHttpClient, "okHttpClient is null.");
    }

    public void generateBuilder() {
        //校验URL
        if (!baseStringUrl.isEmpty() && baseHttpUrl != null) {
            //二次定义了
            throw new ExceptionInInitializerError("url has been set twice.");
        } else if (!baseStringUrl.isEmpty()) {
            retrofitBuilder.baseUrl(baseStringUrl);
        } else if (baseHttpUrl != null) {
            retrofitBuilder.baseUrl(baseHttpUrl);
        } else {
            throw new ExceptionInInitializerError("BaseUrl needed, call setBaseUrl() first.");
        }

        if (!callAdapterFactories.isEmpty()) {
            for (CallAdapter.Factory f : callAdapterFactories) {
                retrofitBuilder.addCallAdapterFactory(f);
            }
        } else {
            //默认值
            RxJava2CallAdapterFactory rxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create();
            callAdapterFactories.add(rxJava2CallAdapterFactory);
            retrofitBuilder.addCallAdapterFactory(rxJava2CallAdapterFactory);
        }
        if (!converterFactories.isEmpty()) {
            for (Converter.Factory f : converterFactories) {
                retrofitBuilder.addConverterFactory(f);
            }
        } else {
            //默认值
            GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();
            converterFactories.add(gsonConverterFactory);
            retrofitBuilder.addConverterFactory(gsonConverterFactory);
        }
        if (okHttpClient != null) {
            retrofitBuilder.client(okHttpClient);
        } else {
            //默认值
            OkHttpProvider okHttpProvider = new OkHttpProvider();
            okHttpProvider.generateBuilder();

            okHttpClient = okHttpProvider.getOkBuilder().build();
            retrofitBuilder.client(okHttpClient);
        }
    }

    public Retrofit.Builder getRetrofitBuilder() {
        return retrofitBuilder;
    }

    public String getBaseStringUrl() {
        return baseStringUrl;
    }

    public HttpUrl getBaseHttpUrl() {
        return baseHttpUrl;
    }

    public List<CallAdapter.Factory> getCallAdapterFactories() {
        return callAdapterFactories;
    }

    public List<Converter.Factory> getConverterFactories() {
        return converterFactories;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
