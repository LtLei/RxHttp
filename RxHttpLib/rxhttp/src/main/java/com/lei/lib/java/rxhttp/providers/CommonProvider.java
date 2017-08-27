package com.lei.lib.java.rxhttp.providers;

import android.app.Application;

import com.lei.lib.java.rxcache.RxCache;
import com.lei.lib.java.rxhttp.method.CacheMethod;
import com.lei.lib.java.rxhttp.util.Utilities;

import java.util.LinkedHashMap;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * 通用参数设置类
 *
 * @author lei
 */

public class CommonProvider {
    //==============================================================
    //                          全局的变量设置
    //      1、设置是否打印日志：打印运行期间网络请求日志，缓存日志等。
    //      2、添加全局的Header以及Param的功能，以及删除这些Header以及Param的功能
    //      3、设置全局的OkHttpClient：包含OkHttpProvider的全部内容
    //      4、设置全局的Retrofit客户端：包含RetrofitProvider的全部内容，以及添加一个ApiService，
    //          这个API将全局生效，如果不单独设置，都会使用这个API
    //      5、全局设置缓存，包含RxCacheProvider的全部内容，以及使用缓存的方式（默认为不使用任何缓存），
    //          这个也是全局生效，如果不单独设置，都会使用这种方式进行缓存
    //      6、未定。。。
    //==============================================================
    private Application context;
    private RxCache rxCache;
    private Retrofit retrofit;
    private OkHttpClient okHttpClient;
    private boolean debug = true;
    private LinkedHashMap<String, String> headers = new LinkedHashMap<>();
    private LinkedHashMap<String, String> params = new LinkedHashMap<>();
    private OkHttpProvider okHttpProvider;
    private RetrofitProvider retrofitProvider;
    private CacheProvider cacheProvider;
    private Class<?> apiService;
    private CacheMethod cacheMethod = CacheMethod.ONLY_NET;

    public CommonProvider(Application context) {
        this.context = context;
        cacheProvider = new CacheProvider(context);
        okHttpProvider = new OkHttpProvider();
        retrofitProvider = new RetrofitProvider();
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void addHeader(String key, String value) {
        Utilities.checkNullOrEmpty(key, "key is null or empty.");
        Utilities.checkNotNull(value, "value is null");
        this.headers.put(key, value);
    }

    public void addHeaders(LinkedHashMap<String, String> headers) {
        Utilities.checkNotNull(headers, "headers is null.");
        if (!headers.isEmpty())
            this.headers.putAll(headers);
    }

    public String removeHeader(String key) {
        Utilities.checkNullOrEmpty(key, "key is null or empty.");
        return this.headers.remove(key);
    }

    public void clearHeaders() {
        this.headers.clear();
    }

    public void addParam(String key, String value) {
        Utilities.checkNullOrEmpty(key, "key is null or empty.");
        Utilities.checkNotNull(value, "value is null");
        this.params.put(key, value);
    }

    public void addParams(LinkedHashMap<String, String> params) {
        Utilities.checkNotNull(params, "params is null.");
        if (!params.isEmpty())
            this.params.putAll(params);
    }

    public String removeParam(String key) {
        Utilities.checkNullOrEmpty(key, "key is null or empty.");
        return this.params.remove(key);
    }

    public void clearParams() {
        this.params.clear();
    }

    public void setOkHttpProvider(OkHttpProvider okHttpProvider) {
        Utilities.checkNotNull(okHttpProvider, "okHttpProvider is null.");
        this.okHttpProvider = okHttpProvider;
    }

    public void setRetrofitProvider(RetrofitProvider retrofitProvider) {
        Utilities.checkNotNull(retrofitProvider, "retrofitProvider is null.");
        this.retrofitProvider = retrofitProvider;
    }

    public void setCacheProvider(CacheProvider cacheProvider) {
        Utilities.checkNotNull(cacheProvider, "cacheProvider is null.");
        this.cacheProvider = cacheProvider;
    }

    public void setApiService(Class<?> apiService) {
        Utilities.checkNotNull(apiService, "apiService is null.");
        this.apiService = apiService;
    }

    public void setCacheMethod(CacheMethod cacheMethod) {
        this.cacheMethod = cacheMethod;
    }

    public void generate() {
        //需要设置一下retrofit rxCache okHttpClient
        generateOkHttpClient();
        generateCache();
        generateRetorfit();
    }

    public void generateRetorfit() {
        if (retrofitProvider == null) {
            retrofitProvider = new RetrofitProvider();
        }
        retrofitProvider.setClient(okHttpClient);

        retrofitProvider.generateBuilder();

        retrofit = retrofitProvider.getRetrofitBuilder().build();
    }

    public void generateCache() {
        if (cacheProvider == null) {
            cacheProvider = new CacheProvider(context);
        }
        cacheProvider.generateBuilder();

        rxCache = cacheProvider.getCacheBuilder().build();
    }

    public void generateOkHttpClient() {
        if (okHttpProvider == null) {
            okHttpProvider = new OkHttpProvider();
        }
        okHttpProvider.generateBuilder();

        okHttpClient = okHttpProvider.getOkBuilder().build();
    }

    public RxCache getRxCache() {
        return rxCache;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public boolean isDebug() {
        return debug;
    }

    public LinkedHashMap<String, String> getHeaders() {
        return headers;
    }

    public LinkedHashMap<String, String> getParams() {
        return params;
    }

    public OkHttpProvider getOkHttpProvider() {
        return okHttpProvider;
    }

    public RetrofitProvider getRetrofitProvider() {
        return retrofitProvider;
    }

    public CacheProvider getCacheProvider() {
        return cacheProvider;
    }

    public Class<?> getApiService() {
        return apiService;
    }

    public CacheMethod getCacheMethod() {
        return cacheMethod;
    }
}
