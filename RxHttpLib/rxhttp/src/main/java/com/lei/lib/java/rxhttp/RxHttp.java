package com.lei.lib.java.rxhttp;

import android.app.Application;

import com.lei.lib.java.rxcache.RxCache;
import com.lei.lib.java.rxcache.converter.IConverter;
import com.lei.lib.java.rxcache.mode.CacheMode;
import com.lei.lib.java.rxhttp.cache.CacheProvider;
import com.lei.lib.java.rxhttp.okhttp.OkHttpProvider;
import com.lei.lib.java.rxhttp.retrofit.RetrofitProvider;
import com.lei.lib.java.rxhttp.util.Utilities;

import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Authenticator;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rymyz on 2017/8/23.
 */

public class RxHttp {
    //==============================================================
    //                           init方法
    //==============================================================
    private static Application mContext;

    public static void init(Application context) {
        mContext = Utilities.checkNotNull(context, "context is null.");
    }

    private static void assertInit() {
        Utilities.checkNotNull(mContext, "context is null, you may call init() first in you application.");
    }

    //==================================================================
    //                             单例化
    //==================================================================
    private static RxHttp instance = null;

    public static RxHttp getInstance() {
        if (instance == null) {
            synchronized (RxHttp.class) {
                if (instance == null) {
                    instance = new RxHttp();
                }
            }
        }
        return instance;
    }

    private RxHttp() {
        assertInit();
    }

    //==============================================================
    //                          全局的变量设置
    //==============================================================
    private OkHttpProvider.Builder commonOkHttpProviderBuilder;
    private OkHttpProvider.Builder primaryOkHttpProviderBuilder;

    public OkHttpProvider.Builder getCommonOkHttpProviderBuilder(){
       if (commonOkHttpProviderBuilder==null)commonOkHttpProviderBuilder= new OkHttpProvider.Builder();
       return commonOkHttpProviderBuilder;
    }
    public OkHttpProvider.Builder getPrimaryOkHttpProviderBuilder(){
        primaryOkHttpProviderBuilder = OkHttpProvider.copyOf(getCommonOkHttpProviderBuilder());
        return primaryOkHttpProviderBuilder;
    }


    private OkHttpClient.Builder commonOkHttpClientBuilder;
    private OkHttpClient.Builder primaryOkHttpClientBuilder;
    private Retrofit.Builder commonRetrofitBuilder;
    private Retrofit.Builder primaryRetrofitBuilder;
    private RxCache.Builder commomRxCacheBuilder;
    private RxCache.Builder primaryRxCacheBuilder;

    private OkHttpClient.Builder getCommonOkHttpClientBuilder() {
        if (commonRetrofitBuilder == null) new Builder().buildOkHttp();
        return commonOkHttpClientBuilder;
    }

    private OkHttpClient.Builder getPrimaryOkHttpClientBuilder() {
        primaryOkHttpClientBuilder = getCommonOkHttpClientBuilder();
        return primaryOkHttpClientBuilder;
    }

    private Retrofit.Builder getCommonRetrofitBuilder() {
        if (commonRetrofitBuilder == null) new Builder().buildRetrofit();
        return commonRetrofitBuilder;
    }

    private Retrofit.Builder getPrimaryRetrofitBuilder() {
        primaryRetrofitBuilder = getCommonRetrofitBuilder();
        return primaryRetrofitBuilder;
    }

    private RxCache.Builder getCommomRxCacheBuilder() {
        if (commomRxCacheBuilder == null) new Builder().buildCache();
        return commomRxCacheBuilder;
    }

    private RxCache.Builder getPrimaryRxCacheBuilder() {
        primaryRxCacheBuilder = getCommomRxCacheBuilder();
        return primaryRxCacheBuilder;
    }

    private OkHttpClient commonOkHttpClient;
    private OkHttpClient primaryOkHttpClient;
    private Retrofit commonRetrofit;
    private Retrofit primaryRetrofit;
    private RxCache commomRxCache;
    private RxCache primaryRxCache;

    private OkHttpClient getCommonOkHttpClient() {
        if (commonOkHttpClient == null) commonOkHttpClient = getCommonOkHttpClientBuilder().build();
        return commonOkHttpClient;
    }

    //每次都需要初始化，因为希望的是每次调用的设置都是独立的，只影响当前的请求
    private OkHttpClient getPrimaryOkHttpClient() {
        primaryOkHttpClient = getCommonOkHttpClient();
        return primaryOkHttpClient;
    }

    private Retrofit getCommonRetrofit() {
        if (commonRetrofit == null) commonRetrofit = getCommonRetrofitBuilder().build();
        return commonRetrofit;
    }

    private Retrofit getPrimaryRetrofit() {
        primaryRetrofit = getCommonRetrofit();
        return primaryRetrofit;
    }

    private RxCache getCommomRxCache() {
        if (commomRxCache == null) commomRxCache = getCommomRxCacheBuilder().build();
        return commomRxCache;
    }

    private RxCache getPrimaryRxCache() {
        primaryRxCache = getCommomRxCache();
        return primaryRxCache;
    }

    //===============================================================
    //                          Builder
    //  用到的XXXManager是为了本类的简洁，避免写太多的配置，使得代码复杂而难以理解
    //  可以看到在XXXManager中仅仅是封装了一下XXX.Builder的初始化，以及XXX的实例生成。
    //  这里配置的全部全局有效
    //
    //  配置OkHttpClient
    //      1、设置的方式均为setXXX()格式
    //      2、设置logging 暂未实现！！！
    //      3、配置commonOkHttpClient
    //  配置Retrofit
    //  配置缓存
    //  配置一些全局的设置
    //      是否打印日志：这包含了所有的Log，以及Cache的Log，默认为true。
    //      commonHeaders
    //      commonParams
    //      retryTimes
    //===============================================================
    public static class Builder {
        private OkHttpProvider.Builder okBuilder;
        private RetrofitProvider.Builder retrofitBuilder;
        private CacheProvider.Builder cacheBuilder;

        public Builder() {
            assertInit();

            okBuilder = new OkHttpProvider.Builder();
            retrofitBuilder = new RetrofitProvider.Builder();

            RxCache.init(mContext);
            cacheBuilder = new CacheProvider.Builder();
        }

        //OkHttp Settings

        /**
         * 设置链接超时时间
         *
         * @param connectTimeout
         * @return
         */
        public Builder setConnectTimeout(long connectTimeout) {
            okBuilder.setConnectTimeout(connectTimeout);
            return this;
        }

        /**
         * 设置读取超时时间
         *
         * @param readTimeout
         */
        public Builder setReadTimeout(long readTimeout) {
            okBuilder.setReadTimeout(readTimeout);
            return this;
        }

        /**
         * 设置写入超时时间
         *
         * @param writeTimeout
         */
        public Builder setWriteTimeout(long writeTimeout) {
            okBuilder.setWriteTimeout(writeTimeout);
            return this;
        }

        /**
         * 添加拦截器
         *
         * @param interceptor
         * @return
         */
        public Builder addInterceptor(Interceptor interceptor) {
            okBuilder.addInterceptor(interceptor);
            return this;
        }

        /**
         * 批量添加拦截器
         *
         * @param interceptors
         * @return
         */
        public Builder addInterceptors(List<Interceptor> interceptors) {
            okBuilder.addInterceptors(interceptors);
            return this;
        }

        /**
         * 添加网络拦截器
         *
         * @param networkInterceptor
         * @return
         */
        public Builder addNetworkInterceptor(Interceptor networkInterceptor) {
            okBuilder.addNetworkInterceptor(networkInterceptor);
            return this;
        }

        /**
         * 批量添加网络拦截器
         *
         * @param networkInterceptors
         * @return
         */
        public Builder addNetworkInterceptors(List<Interceptor> networkInterceptors) {
            okBuilder.addNetworkInterceptors(networkInterceptors);
            return this;
        }

        /**
         * 设置认证
         *
         * @param authenticator
         * @return
         */
        public Builder setAuthenticator(Authenticator authenticator) {
            okBuilder.setAuthenticator(authenticator);
            return this;
        }

        /**
         * 设置CookieJar
         *
         * @param cookieJar
         */
        public Builder setCookieJar(CookieJar cookieJar) {
            okBuilder.setCookieJar(cookieJar);
            return this;
        }

        /**
         * 服务器证书验证
         *
         * @param hostnameVerifier
         */
        public Builder setHostnameVerifier(HostnameVerifier hostnameVerifier) {
            okBuilder.setHostnameVerifier(hostnameVerifier);
            return this;
        }

        /**
         * 暂未实现！！！
         *
         * @param sslSocketFactory
         * @param x509TrustManager
         * @return
         */
        public Builder setSslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager x509TrustManager) {
            okBuilder.setSslSocketFactory(sslSocketFactory, x509TrustManager);
            return this;
        }

        //retrofit settings
        public Builder setBaseUrl(String url) {
            retrofitBuilder.setBaseUrl(url);
            return this;
        }

        public Builder setBaseUrl(HttpUrl httpUrl) {
            retrofitBuilder.setBaseUrl(httpUrl);
            return this;
        }

        public Builder addCallAdapterFactory(CallAdapter.Factory factory) {
            retrofitBuilder.addCallAdapterFactory(factory);
            return this;
        }

        public Builder addCallAdapterFactories(List<CallAdapter.Factory> factories) {
            retrofitBuilder.addCallAdapterFactories(factories);
            return this;
        }

        public Builder addConverterFactory(Converter.Factory factory) {
            retrofitBuilder.addConverterFactory(factory);
            return this;
        }

        public Builder addConverterFactories(List<Converter.Factory> factories) {
            retrofitBuilder.addConverterFactories(factories);
            return this;
        }

        public Builder setClient(OkHttpClient okHttpClient) {
            retrofitBuilder.setClient(okHttpClient);
            return this;
        }

        //cache settings
        public Builder setDebug(boolean debug) {
            cacheBuilder.setDebug(debug);
            return this;
        }

        public Builder setCacheMode(CacheMode cacheMode) {
            cacheBuilder.setCacheMode(cacheMode);
            return this;
        }

        public Builder setConverter(IConverter converter) {
            cacheBuilder.setConverter(converter);
            return this;
        }

        public Builder setMemoryCacheSizeByMB(int memoryCacheSizeByMB) {
            cacheBuilder.setMemoryCacheSizeByMB(memoryCacheSizeByMB);
            return this;
        }

        public Builder setDiskCacheSizeByMB(int diskCacheSizeByMB) {
            cacheBuilder.setDiskCacheSizeByMB(diskCacheSizeByMB);
            return this;
        }

        public Builder setDiskDirName(String diskDirName) {
            cacheBuilder.setDiskDirName(diskDirName);
            return this;
        }

        private void buildCache() {
            RxHttp.getInstance().commomRxCacheBuilder = cacheBuilder.build();
        }

        private void buildOkHttp() {
            //初始值：
            // 三个Timeout,默认均为60s
            RxHttp.getInstance().commonOkHttpClientBuilder = okBuilder.build();
        }

        private void buildRetrofit() {
            //初始值：
            //默认的CallAdapterFactory、ConverterFactory以及默认的OKHttpClient
            if (retrofitBuilder.getCallAdapterFactories().isEmpty()) {
                retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
            }
            if (retrofitBuilder.getConverterFactories().isEmpty()) {
                retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
            }
            if (retrofitBuilder.getOkHttpClient() == null) {
                buildOkHttp();
                retrofitBuilder.setClient(RxHttp.getInstance().getCommonOkHttpClient());
            }
            RxHttp.getInstance().commonRetrofitBuilder = retrofitBuilder.build();
        }

        public RxHttp build() {
            buildOkHttp();
            buildRetrofit();
            buildCache();
            return RxHttp.getInstance();
        }
    }
}
