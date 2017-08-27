package com.lei.lib.java.rxhttp;

import android.app.Application;

import com.lei.lib.java.rxcache.converter.IConverter;
import com.lei.lib.java.rxcache.mode.CacheMode;
import com.lei.lib.java.rxhttp.method.CacheMethod;
import com.lei.lib.java.rxhttp.providers.CommonProvider;
import com.lei.lib.java.rxhttp.providers.PrimaryProvider;
import com.lei.lib.java.rxhttp.util.Utilities;

import java.util.LinkedHashMap;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import okhttp3.Authenticator;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

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
        commonProvider = new CommonProvider(mContext);
        primaryProvider = new PrimaryProvider();
    }

    private CommonProvider commonProvider;
    private PrimaryProvider primaryProvider;

    //=============================================================
    //                  所有的全局设置
    //===========================================================
    public RxHttp debug(boolean debug) {
        commonProvider.setDebug(debug);
        return this;
    }

    public RxHttp setCommonCacheMethod(CacheMethod cacheMethod) {
        commonProvider.setCacheMethod(cacheMethod);
        return this;
    }

    public RxHttp setCommonApiService(Class<?> apiService) {
        commonProvider.setApiService(apiService);
        return this;
    }

    public RxHttp addCommonHeader(String key, String value) {
        commonProvider.addHeader(key, value);
        return this;
    }

    public RxHttp addCommonHeaders(LinkedHashMap<String, String> headers) {
        commonProvider.addHeaders(headers);
        return this;
    }

    public RxHttp removeCommonHeader(String key) {
        commonProvider.removeHeader(key);
        return this;
    }

    public RxHttp clearCommonHeaders() {
        commonProvider.clearHeaders();
        return this;
    }

    public RxHttp addCommonParam(String key, String value) {
        commonProvider.addParam(key, value);
        return this;
    }

    public RxHttp addCommonParams(LinkedHashMap<String, String> params) {
        commonProvider.addParams(params);
        return this;
    }

    public RxHttp removeCommonParam(String key) {
        commonProvider.removeParam(key);
        return this;
    }

    public RxHttp clearCommonParams() {
        commonProvider.clearParams();
        return this;
    }

    //okhttp settings

    /**
     * 设置链接超时时间
     *
     * @param connectTimeout
     * @return
     */
    public RxHttp setConnectTimeout(long connectTimeout) {
        commonProvider.getOkHttpProvider().setConnectTimeout(connectTimeout);
        return this;
    }

    /**
     * 设置读取超时时间
     *
     * @param readTimeout
     */
    public RxHttp setReadTimeout(long readTimeout) {
        commonProvider.getOkHttpProvider().setReadTimeout(readTimeout);
        return this;
    }

    /**
     * 设置写入超时时间
     *
     * @param writeTimeout
     */
    public RxHttp setWriteTimeout(long writeTimeout) {
        commonProvider.getOkHttpProvider().setWriteTimeout(writeTimeout);
        return this;
    }

    /**
     * 添加拦截器
     *
     * @param interceptor
     * @return
     */
    public RxHttp addInterceptor(Interceptor interceptor) {
        commonProvider.getOkHttpProvider().addInterceptor(interceptor);
        return this;
    }

    /**
     * 批量添加拦截器
     *
     * @param interceptors
     * @return
     */
    public RxHttp addInterceptors(List<Interceptor> interceptors) {
        commonProvider.getOkHttpProvider().addInterceptors(interceptors);
        return this;
    }

    /**
     * 添加网络拦截器
     *
     * @param networkInterceptor
     * @return
     */
    public RxHttp addNetworkInterceptor(Interceptor networkInterceptor) {
        commonProvider.getOkHttpProvider().addNetworkInterceptor(networkInterceptor);
        return this;
    }

    /**
     * 批量添加网络拦截器
     *
     * @param networkInterceptors
     * @return
     */
    public RxHttp addNetworkInterceptors(List<Interceptor> networkInterceptors) {
        commonProvider.getOkHttpProvider().addNetworkInterceptors(networkInterceptors);
        return this;
    }

    /**
     * 设置认证
     *
     * @param authenticator
     * @return
     */
    public RxHttp setAuthenticator(Authenticator authenticator) {
        commonProvider.getOkHttpProvider().setAuthenticator(authenticator);
        return this;
    }

    /**
     * 设置CookieJar
     *
     * @param cookieJar
     */
    public RxHttp setCookieJar(CookieJar cookieJar) {
        commonProvider.getOkHttpProvider().setCookieJar(cookieJar);
        return this;
    }

    /**
     * 服务器证书验证
     *
     * @param hostnameVerifier
     */
    public RxHttp setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        commonProvider.getOkHttpProvider().setHostnameVerifier(hostnameVerifier);
        return this;
    }

    public RxHttp setSslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager x509TrustManager) {
        commonProvider.getOkHttpProvider().setSslSocketFactory(sslSocketFactory, x509TrustManager);
        return this;
    }

    //retrofit settings

    /**
     * 设置baseUrl，请以/结尾，不要以/开头
     *
     * @param url
     */
    public RxHttp setBaseUrl(String url) {
        commonProvider.getRetrofitProvider().setBaseUrl(url);
        return this;
    }

    public RxHttp setBaseUrl(HttpUrl httpUrl) {
        commonProvider.getRetrofitProvider().setBaseUrl(httpUrl);
        return this;
    }

    public RxHttp addCallAdapterFactory(CallAdapter.Factory factory) {
        commonProvider.getRetrofitProvider().addCallAdapterFactory(factory);
        return this;
    }

    public RxHttp addCallAdapterFactories(List<CallAdapter.Factory> factories) {
        commonProvider.getRetrofitProvider().addCallAdapterFactories(factories);
        return this;
    }

    public RxHttp addConverterFactory(Converter.Factory factory) {
        commonProvider.getRetrofitProvider().addConverterFactory(factory);
        return this;
    }

    public RxHttp addConverterFactories(List<Converter.Factory> factories) {
        commonProvider.getRetrofitProvider().addConverterFactories(factories);
        return this;
    }

    public RxHttp setClient(OkHttpClient okHttpClient) {
        commonProvider.getRetrofitProvider().setClient(okHttpClient);
        return this;
    }

    //cache settings

    public RxHttp setCacheMode(CacheMode cacheMode) {
        commonProvider.getCacheProvider().setCacheMode(cacheMode);
        return this;
    }

    public RxHttp setConverter(IConverter converter) {
        commonProvider.getCacheProvider().setConverter(converter);
        return this;
    }

    public RxHttp setMemoryCacheSizeByMB(int memoryCacheSizeByMB) {
        commonProvider.getCacheProvider().setMemoryCacheSizeByMB(memoryCacheSizeByMB);
        return this;
    }

    public RxHttp setDiskCacheSizeByMB(int diskCacheSizeByMB) {
        commonProvider.getCacheProvider().setDiskCacheSizeByMB(diskCacheSizeByMB);
        return this;
    }

    public RxHttp setDiskDirName(String diskDirName) {
        commonProvider.getCacheProvider().setDiskDirName(diskDirName);
        return this;
    }

    //=============================================================
    //        所有的局部设置，以下设置仅当次请求生效
    //===========================================================
    public RxHttp setCacheKey(String cacheKey) {
        primaryProvider.setCacheKey(cacheKey);
        return this;
    }

    public RxHttp setCacheTime(long cacheTime) {
        primaryProvider.setCacheTime(cacheTime);
        return this;
    }

    public RxHttp addPrimaryHeader(String key, String value) {
        primaryProvider.addHeader(key, value);
        return this;
    }

    public RxHttp addPrimaryHeaders(LinkedHashMap<String, String> headers) {
        primaryProvider.addHeaders(headers);
        return this;
    }

    public RxHttp removePrimaryHeader(String key) {
        primaryProvider.removeHeader(key);
        return this;
    }

    public RxHttp clearPrimaryHeaders() {
        primaryProvider.clearHeaders();
        return this;
    }

    public RxHttp addPrimaryParam(String key, String value) {
        primaryProvider.addParam(key, value);
        return this;
    }

    public RxHttp addPrimaryParams(LinkedHashMap<String, String> params) {
        primaryProvider.addParams(params);
        return this;
    }

    public RxHttp removePrimaryParam(String key) {
        primaryProvider.removeParam(key);
        return this;
    }

    public RxHttp clearPrimaryParams() {
        primaryProvider.clearParams();
        return this;
    }

    public RxHttp setPrimaryApiService(Class<?> apiService) {
        primaryProvider.setApiService(apiService);
        return this;
    }

    public RxHttp setPrimaryCacheMethod(CacheMethod cacheMethod) {
        primaryProvider.setCacheMethod(cacheMethod);
        return this;
    }

    public Retrofit getRetrofit() {
        commonProvider.generate();
        return commonProvider.getRetrofit();
    }

    public <S> S createService() {
        Utilities.checkNotNull(commonProvider.getApiService(), "ApiService is null.");
        return (S) getRetrofit().create(commonProvider.getApiService());
    }

    public <T> void handleResult(Observable<T> dataObservable){

    }
}
