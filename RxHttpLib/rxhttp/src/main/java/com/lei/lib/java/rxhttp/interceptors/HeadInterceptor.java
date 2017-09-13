package com.lei.lib.java.rxhttp.interceptors;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeadInterceptor implements Interceptor {
    Map<String, String> headers = new HashMap<>();

    public HeadInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        Request request = null;
        Headers.Builder headersBuilder = new Headers.Builder();
        for (String key : headers.keySet()) {
            headersBuilder.add(key, headers.get(key));
        }

        request = builder.headers(headersBuilder.build()).build();
        return chain.proceed(request);
    }
}