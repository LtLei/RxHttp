package com.lei.lib.java.rxhttp;

import android.app.Application;

import com.lei.lib.java.rxcache.converter.GsonConverter;
import com.lei.lib.java.rxcache.converter.IConverter;
import com.lei.lib.java.rxcache.mode.CacheMode;
import com.lei.lib.java.rxhttp.method.CacheMethod;
import com.lei.lib.java.rxhttp.request.GetRequest;
import com.lei.lib.java.rxhttp.util.Utilities;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.ObservableTransformer;
import okhttp3.Authenticator;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.ResponseBody;
import retrofit2.CallAdapter;
import retrofit2.Converter;

/**
 * RxHttp使用类
 *
 * @author lei
 */

public class RxHttp1 {
    //about init()
    private static Application mContext;

    public static void init(Application context) {
        mContext = Utilities.checkNotNull(context, "context is null.");
    }

    //about singleton
    private static class SingletonHolder {
        private static final RxHttp1 instance = new RxHttp1();
    }

    public static RxHttp1 getInstance() {
        return SingletonHolder.instance;
    }

    private RxHttp1() {
        Utilities.checkNotNull(mContext, "context is null, you may call init() in your application first.");
    }

    //common settings
    private boolean debug = true;
    private LinkedHashMap<String, String> commonHeaders = new LinkedHashMap<>();
    private LinkedHashMap<String, String> commonParams = new LinkedHashMap<>();
    private CacheMethod commonCacheMethod = CacheMethod.ONLY_NET;
    private boolean useEntity = true;
    private ObservableTransformer<ResponseBody, String> responseBodyStringObservableTransformer;

    public RxHttp1 convertBefore(ObservableTransformer<ResponseBody, String> transformer) {
        Utilities.checkNotNull(transformer, "transformer is null.");
        this.responseBodyStringObservableTransformer = transformer;
        return this;
    }

    public ObservableTransformer<ResponseBody, String> getResponseBodyStringObservableTransformer() {
        return responseBodyStringObservableTransformer;
    }

    public boolean isDebug() {
        return debug;
    }

    public RxHttp1 setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public LinkedHashMap<String, String> getCommonHeaders() {
        return commonHeaders;
    }

    public RxHttp1 addCommonHeader(String key, String content) {
        Utilities.checkNullOrEmpty(key, "key is null or empty");
        this.commonHeaders.put(key, content);
        return this;
    }

    public RxHttp1 addCommonHeaders(LinkedHashMap<String, String> headers) {
        Utilities.checkNotNull(headers, "headers is null.");
        this.commonHeaders.putAll(headers);
        return this;
    }

    public RxHttp1 removeCommonHeader(String key) {
        Utilities.checkNullOrEmpty(key, "key is null or empty.");
        this.commonHeaders.remove(key);
        return this;
    }

    public RxHttp1 removeAllCommonHeaders() {
        this.commonHeaders.clear();
        return this;
    }


    public LinkedHashMap<String, String> getCommonParams() {
        return commonParams;
    }

    public RxHttp1 addCommonParam(String key, String content) {
        Utilities.checkNullOrEmpty(key, "key is null or empty.");
        this.commonParams.put(key, content);
        return this;
    }

    public RxHttp1 addCommonParams(LinkedHashMap<String, String> params) {
        Utilities.checkNotNull(params, "params is null");
        this.commonParams.putAll(params);
        return this;
    }

    public RxHttp1 removeCommonParam(String key) {
        Utilities.checkNullOrEmpty(key, "key is null or empty.");
        this.commonParams.remove(key);
        return this;
    }

    public RxHttp1 removeAllCommonParams() {
        this.commonParams.clear();
        return this;
    }

    public CacheMethod getCommonCacheMethod() {
        return commonCacheMethod;
    }

    public void setCommonCacheMethod(CacheMethod cacheMethod) {
        this.commonCacheMethod = cacheMethod;
    }

    public boolean isUseEntity() {
        return useEntity;
    }

    public void useEntity(boolean useEntity) {
        this.useEntity = useEntity;
    }

    //about okhttp
    private static final long DEFAULT_TIMEOUT = 60 * 1000;
    private long connectTimeout = DEFAULT_TIMEOUT;
    private long readTimeout = DEFAULT_TIMEOUT;
    private long writeTimeout = DEFAULT_TIMEOUT;
    private List<Interceptor> interceptors = new ArrayList<>();
    private List<Interceptor> netInterceptors = new ArrayList<>();
    private Authenticator authenticator;
    private CookieJar cookieJar;
    private HostnameVerifier hostnameVerifier;
    private SSLSocketFactory sslSocketFactory;
    private X509TrustManager x509TrustManager;

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public RxHttp1 setConnectTimeout(long connectTimeout) {
        if (connectTimeout <= 0) connectTimeout = 0;
        this.connectTimeout = connectTimeout;
        return this;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public RxHttp1 setReadTimeout(long readTimeout) {
        if (readTimeout <= 0) readTimeout = 0;
        this.readTimeout = readTimeout;
        return this;
    }

    public long getWriteTimeout() {
        return writeTimeout;
    }

    public RxHttp1 setWriteTimeout(long writeTimeout) {
        if (writeTimeout <= 0) writeTimeout = 0;
        this.writeTimeout = writeTimeout;
        return this;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public RxHttp1 addInterceptor(Interceptor interceptor) {
        Utilities.checkNotNull(interceptor, "interceptor is null.");
        this.interceptors.add(interceptor);
        return this;
    }

    public RxHttp1 addInterceptors(List<Interceptor> interceptors) {
        Utilities.checkNotNull(interceptors, "interceptors is null.");
        this.interceptors.addAll(interceptors);
        return this;
    }

    public List<Interceptor> getNetInterceptors() {
        return netInterceptors;
    }

    public RxHttp1 addNetInterceptor(Interceptor netInterceptor) {
        Utilities.checkNotNull(netInterceptor, "netInterceptor is null.");
        this.netInterceptors.add(netInterceptor);
        return this;
    }

    public RxHttp1 addNetInterceptors(List<Interceptor> netInterceptors) {
        Utilities.checkNotNull(netInterceptors, "netInterceptors is null.");
        this.netInterceptors.addAll(netInterceptors);
        return this;
    }

    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public RxHttp1 setAuthenticator(Authenticator authenticator) {
        this.authenticator = Utilities.checkNotNull(authenticator, "authenticator is null");
        return this;
    }

    public CookieJar getCookieJar() {
        return cookieJar;
    }

    public RxHttp1 setCookieJar(CookieJar cookieJar) {
        this.cookieJar = Utilities.checkNotNull(cookieJar, "cookieJar is null.");
        return this;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public RxHttp1 setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = Utilities.checkNotNull(hostnameVerifier, "hostnameVerifier is null.");
        return this;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public RxHttp1 setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = Utilities.checkNotNull(sslSocketFactory, "sslSocketFactory is null.");
        return this;
    }

    public X509TrustManager getX509TrustManager() {
        return x509TrustManager;
    }

    public RxHttp1 setX509TrustManager(X509TrustManager x509TrustManager) {
        this.x509TrustManager = Utilities.checkNotNull(x509TrustManager, "x509TrustManager is null.");
        return this;
    }

    //about retrofit
    private String baseStringUrl = "";
    private HttpUrl baseHttpUrl;
    private List<CallAdapter.Factory> callAdapterFactories = new ArrayList<>();
    private List<Converter.Factory> converterFactories = new ArrayList<>();
    private Class<?> apiService;

    public String getBaseStringUrl() {
        return baseStringUrl;
    }

    public RxHttp1 setBaseUrl(String baseUrl) {
        Utilities.checkNullOrEmpty(baseUrl, "baseUrl is null or empty.");
        this.baseStringUrl = baseUrl;
        return this;
    }

    public HttpUrl getBaseHttpUrl() {
        return baseHttpUrl;
    }

    public RxHttp1 setBaseUrl(HttpUrl baseUrl) {
        Utilities.checkNotNull(baseUrl, "baseUrl is null.");
        this.baseHttpUrl = baseUrl;
        return this;
    }

    public List<CallAdapter.Factory> getCallAdapterFactories() {
        return callAdapterFactories;
    }

    public RxHttp1 addCallAdapterFactory(CallAdapter.Factory factory) {
        Utilities.checkNotNull(factory, "factory is null.");
        this.callAdapterFactories.add(factory);
        return this;
    }

    public RxHttp1 addCallAdapterFactories(List<CallAdapter.Factory> callAdapterFactories) {
        Utilities.checkNotNull(callAdapterFactories, "callAdapterFactories is null.");
        this.callAdapterFactories.addAll(callAdapterFactories);
        return this;
    }

    public List<Converter.Factory> getConverterFactories() {
        return converterFactories;
    }

    public RxHttp1 addConverterFactoty(Converter.Factory factory) {
        Utilities.checkNotNull(factory, "factory is null.");
        this.converterFactories.add(factory);
        return this;
    }

    public RxHttp1 addConverterFactories(List<Converter.Factory> converterFactories) {
        Utilities.checkNotNull(converterFactories, "converterFactories is null.");
        this.converterFactories.addAll(converterFactories);
        return this;
    }

    public Class<?> getApiService() {
        return apiService;
    }

    public RxHttp1 setApiService(Class<?> apiService) {
        Utilities.checkNotNull(apiService, "apiService is null.");
        this.apiService = apiService;
        return this;
    }

    //about rxcache
    private CacheMode cacheMode = CacheMode.BOTH;
    private IConverter converter = new GsonConverter();
    private int memoryCacheSizeByMB = (int) (Runtime.getRuntime().maxMemory() / 8 / 1024 / 1024);
    private int diskCacheSizeByMB = 100;
    private String diskDirName = "RxHttp";

    public CacheMode getCacheMode() {
        return cacheMode;
    }

    public RxHttp1 setCacheMode(CacheMode cacheMode) {
        Utilities.checkNotNull(cacheMode, "cacheMode is null.");
        this.cacheMode = cacheMode;
        return this;
    }

    public IConverter getConverter() {
        return converter;
    }

    public RxHttp1 setConverter(IConverter converter) {
        Utilities.checkNotNull(converter, "converter is null.");
        this.converter = converter;
        return this;
    }

    public int getMemoryCacheSizeByMB() {
        return memoryCacheSizeByMB;
    }

    public RxHttp1 setMemoryCacheSizeByMB(int memoryCacheSizeByMB) {
        if (memoryCacheSizeByMB <= 0) memoryCacheSizeByMB = 0;
        this.memoryCacheSizeByMB = memoryCacheSizeByMB;
        return this;
    }

    public int getDiskCacheSizeByMB() {
        return diskCacheSizeByMB;
    }

    public RxHttp1 setDiskCacheSizeByMB(int diskCacheSizeByMB) {
        if (diskCacheSizeByMB <= 0) diskCacheSizeByMB = 0;
        this.diskCacheSizeByMB = diskCacheSizeByMB;
        return this;
    }

    public String getDiskDirName() {
        return diskDirName;
    }

    public RxHttp1 setDiskDirName(String diskDirName) {
        Utilities.checkNullOrEmpty(diskDirName, "diskDirName is null or empty.");
        this.diskDirName = diskDirName;
        return this;
    }

    public  GetRequest get() {
        return new GetRequest(mContext);
    }
}
