package com.lei.lib.java.rxhttp.retrofit;

import com.lei.lib.java.rxhttp.util.Utilities;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Retrofit管理工具类
 *
 * @author lei
 */

public class RetrofitProvider {
    private RetrofitProvider() {
        throw new ExceptionInInitializerError("RetrofitProvider not support instantiation.");
    }

    public static class Builder {
        private String baseStringUrl = "";
        private HttpUrl baseHttpUrl;
        private List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>();
        private List<Converter.Factory> converterFactories = new ArrayList<>();
        private OkHttpClient okHttpClient;

        public Builder() {
        }

        public Builder setBaseUrl(String url) {
            this.baseStringUrl = Utilities.checkNullOrEmpty(url, "url is null or empty.");
            return this;
        }

        public Builder setBaseUrl(HttpUrl httpUrl) {
            this.baseHttpUrl = Utilities.checkNotNull(httpUrl, "httpUrl is null.");
            return this;
        }

        public Builder addCallAdapterFactory(CallAdapter.Factory factory) {
            this.callAdapterFactories.add(Utilities.checkNotNull(factory, "factory is null."));
            return this;
        }

        public Builder addCallAdapterFactories(List<CallAdapter.Factory> factories) {
            this.callAdapterFactories.addAll(Utilities.checkNotNull(factories, "factories is null."));
            return this;
        }

        public Builder addConverterFactory(Converter.Factory factory) {
            this.converterFactories.add(Utilities.checkNotNull(factory, "factory is null."));
            return this;
        }

        public Builder addConverterFactories(List<Converter.Factory> factories) {
            this.converterFactories.addAll(Utilities.checkNotNull(factories, "factories is null."));
            return this;
        }

        public Builder setClient(OkHttpClient okHttpClient) {
            this.okHttpClient = Utilities.checkNotNull(okHttpClient, "okHttpClient is null.");
            return this;
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

        public HttpUrl getBaseHttpUrl() {
            return baseHttpUrl;
        }

        public String getBaseStringUrl() {
            return baseStringUrl;
        }

        public Retrofit.Builder build() {
            Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
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
            }
            if (!converterFactories.isEmpty()) {
                for (Converter.Factory f : converterFactories) {
                    retrofitBuilder.addConverterFactory(f);
                }
            }
            if (okHttpClient != null) {
                retrofitBuilder.client(okHttpClient);
            }

            return retrofitBuilder;
        }
    }
}
