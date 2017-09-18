package com.lei.lib.java.rxhttp.request;

import android.app.Application;

import com.lei.lib.java.rxcache.RxCache;
import com.lei.lib.java.rxcache.entity.CacheResponse;
import com.lei.lib.java.rxcache.util.LogUtil;
import com.lei.lib.java.rxcache.util.RxUtil;
import com.lei.lib.java.rxhttp.RxHttp;
import com.lei.lib.java.rxhttp.entity.RxEntity;
import com.lei.lib.java.rxhttp.entity.RxResponse;
import com.lei.lib.java.rxhttp.exception.ApiException;
import com.lei.lib.java.rxhttp.interceptors.HeadInterceptor;
import com.lei.lib.java.rxhttp.interceptors.HttpLoggingInterceptor;
import com.lei.lib.java.rxhttp.method.CacheMethod;
import com.lei.lib.java.rxhttp.service.RxService;
import com.lei.lib.java.rxhttp.util.ResponseConvert;
import com.lei.lib.java.rxhttp.util.Utilities;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 发起请求的基类，用于配置一些共用的属性
 *
 * @author lei
 * @since 2017年9月11日
 */

public abstract class BaseRequest<T, R extends BaseRequest<T, R>> {
    private LinkedHashMap<String, String> headers = new LinkedHashMap<>();
    private LinkedHashMap<String, String> params = new LinkedHashMap<>();
    private CacheMethod cacheMethod;
    private boolean useEntity;
    private Class<?> clazz;
    private boolean debug;

    private Type type;
    private boolean forceNet;
    private ResponseConvert<T> responseConvert;

    private ObservableTransformer<ResponseBody, String> transformer;

    private OkHttpClient.Builder okBuilder;
    private Retrofit.Builder retrofitBuilder;
    private RxCache.Builder rxCacheBuilder;

    private String requestUrl;
    private long cacheTime = -1;
    private String cacheKey;

    public BaseRequest(Application context, String path, Type type) {
        this.requestUrl = path;
        this.type = type;

        RxHttp rxHttp = RxHttp.getInstance();

        headers.putAll(rxHttp.getCommonHeaders());
        params.putAll(rxHttp.getCommonParams());
        cacheMethod = rxHttp.getCommonCacheMethod();
        useEntity = rxHttp.isUseEntity();
        clazz = rxHttp.getEntityClass() == null ? RxEntity.class : rxHttp.getEntityClass();
        debug = rxHttp.isDebug();

        transformer = rxHttp.getResponseBodyStringObservableTransformer();

        okBuilder = new OkHttpClient.Builder();
        okBuilder.connectTimeout(rxHttp.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(rxHttp.getReadTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(rxHttp.getWriteTimeout(), TimeUnit.MILLISECONDS);
        if (rxHttp.getCookieJar() != null)
            okBuilder.cookieJar(rxHttp.getCookieJar());

        for (int i = 0; i < rxHttp.getInterceptors().size(); i++) {
            okBuilder.addInterceptor(rxHttp.getInterceptors().get(i));
        }

        if (rxHttp.getNetInterceptors().size() == 0) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("RxHttpLog");
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
            loggingInterceptor.setColorLevel(Level.INFO);
            rxHttp.addNetInterceptor(loggingInterceptor);
        }
        for (int i = 0; i < rxHttp.getNetInterceptors().size(); i++) {
            okBuilder.addNetworkInterceptor(rxHttp.getNetInterceptors().get(i));
        }

        if (rxHttp.getHostnameVerifier() != null)
            okBuilder.hostnameVerifier(rxHttp.getHostnameVerifier());

        if (rxHttp.getSslSocketFactory() != null && rxHttp.getX509TrustManager() != null)
            okBuilder.sslSocketFactory(rxHttp.getSslSocketFactory(), rxHttp.getX509TrustManager());

        if (rxHttp.getAuthenticator() != null)
            okBuilder.authenticator(rxHttp.getAuthenticator());


        retrofitBuilder = new Retrofit.Builder();
        if (rxHttp.getBaseStringUrl().length() == 0 && rxHttp.getBaseHttpUrl() == null)
            throw new NullPointerException("baseUrl needed, call setBaseUrl() first.");
        else if (rxHttp.getBaseHttpUrl() != null && rxHttp.getBaseStringUrl().length() > 0)
            throw new ExceptionInInitializerError("both stringUrl and HttpUrl setted.");
        else if (rxHttp.getBaseStringUrl().length() > 0)
            retrofitBuilder.baseUrl(rxHttp.getBaseStringUrl());
        else retrofitBuilder.baseUrl(rxHttp.getBaseHttpUrl());

        if (rxHttp.getCallAdapterFactories().size() == 0) {
            rxHttp.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        }
        for (int i = 0; i < rxHttp.getCallAdapterFactories().size(); i++) {
            retrofitBuilder.addCallAdapterFactory(rxHttp.getCallAdapterFactories().get(i));
        }

        for (int i = 0; i < rxHttp.getConverterFactories().size(); i++) {
            retrofitBuilder.addConverterFactory(rxHttp.getConverterFactories().get(i));
        }


        RxCache.init(context);
        rxCacheBuilder = new RxCache.Builder();
        rxCacheBuilder.setDebug(rxHttp.isDebug())
                .setConverter(rxHttp.getConverter())
                .setMemoryCacheSizeByMB(rxHttp.getMemoryCacheSizeByMB())
                .setDiskDirName(rxHttp.getDiskDirName())
                .setDiskCacheSizeByMB(rxHttp.getDiskCacheSizeByMB())
                .setCacheMode(rxHttp.getCacheMode());
    }

    public R addHeader(String key, String content) {
        Utilities.checkNullOrEmpty(key, "key is null or empty.");
        this.headers.put(key, content);
        return (R) this;
    }

    public R addHeaders(LinkedHashMap<String, String> headers) {
        Utilities.checkNotNull(headers, "headers is null.");
        this.headers.putAll(headers);
        return (R) this;
    }

    public R removeHeader(String key) {
        Utilities.checkNullOrEmpty(key, "key is null or empty.");
        this.headers.remove(key);
        return (R) this;
    }

    public R removeAllHeaders() {
        this.headers.clear();
        return (R) this;
    }

    public R addParam(String key, String content) {
        Utilities.checkNullOrEmpty(key, "key is null or empty.");
        this.params.put(key, content);
        return (R) this;
    }

    public R addParams(LinkedHashMap<String, String> params) {
        Utilities.checkNotNull(params, "params is null.");
        this.params.putAll(params);
        return (R) this;
    }

    public R removeParam(String key) {
        Utilities.checkNullOrEmpty(key, "key is null or empty.");
        this.params.remove(key);
        return (R) this;
    }

    public R removeAllParams() {
        this.params.clear();
        return (R) this;
    }

    public R cacheMethod(CacheMethod cacheMethod) {
        Utilities.checkNotNull(cacheMethod, "cacheMethod is null.");
        this.cacheMethod = cacheMethod;
        return (R) this;
    }

    public R cacheKey(String cacheKey) {
        Utilities.checkNullOrEmpty(cacheKey, "cacheKey is null or empty.");
        this.cacheKey = cacheKey;
        return (R) this;
    }

    public R cacheTime(long cacheTime) {
        if (cacheTime < -1) cacheTime = -1;
        this.cacheTime = cacheTime;
        return (R) this;
    }

    protected OkHttpClient.Builder getOkBuilder() {
        return okBuilder;
    }

    protected OkHttpClient getOkHttpClient() {
        if (!headers.containsKey(HeadInterceptor.USER_AGENT)) {
            headers.put(HeadInterceptor.USER_AGENT, Utilities.getUserAgent());
        }
        return getOkBuilder().addInterceptor(new HeadInterceptor(headers)).build();
    }

    protected Retrofit getRetrofit() {
        return retrofitBuilder.client(getOkHttpClient()).build();
    }

    protected RxService getApiService() {
        return getRetrofit().create(RxService.class);
    }

    protected RxCache getRxCache() {
        return rxCacheBuilder.build();
    }

    protected String getRequestUrl() {
        return requestUrl;
    }

    protected LinkedHashMap<String, String> getParams() {
        return params;
    }

    protected String getCacheKey() {
        return cacheKey;
    }

    protected long getCacheTime() {
        return cacheTime;
    }

    protected CacheMethod getCacheMethod() {
        return cacheMethod;
    }

    private ResponseConvert<T> getResponseConvert() {
        if (responseConvert == null)
            responseConvert = new ResponseConvert<>(clazz, type, useEntity);
        return responseConvert;
    }

    public Observable<RxResponse<T>> request() {
        switch (getCacheMethod()) {
            case ONLY_NET:
                return requestOnlyNet();
            case ONLY_CACHE:
                return requestOnlyCache();
            case FIRST_NET_THEN_CACHE:
                return requestFirstNet();
            case FIRST_CACHE_THEN_NET:
                return requestFirstCache();
            case CACHE_AND_NET:
                return Observable.create(new ObservableOnSubscribe<Observable<RxResponse<T>>>() {
                    @Override
                    public void subscribe(ObservableEmitter<Observable<RxResponse<T>>> e) throws Exception {
                        e.onNext(BaseRequest.this.<T>requestOnlyCache());
                        e.onNext(BaseRequest.this.<T>requestOnlyNet().doOnNext(new Consumer<RxResponse<T>>() {
                            @Override
                            public void accept(RxResponse<T> tRxResponse) throws Exception {
                                if (tRxResponse.getData() != null) saveLocal(tRxResponse.getData());
                            }
                        }));
                        e.onComplete();
                    }
                }).flatMap(new Function<Observable<RxResponse<T>>, ObservableSource<RxResponse<T>>>() {
                    @Override
                    public ObservableSource<RxResponse<T>> apply(Observable<RxResponse<T>> rxResponseObservable) throws Exception {
                        return rxResponseObservable;
                    }
                });
        }
        return null;
    }

    protected abstract Observable<ResponseBody> netObservable();

    protected Observable<RxResponse<T>> requestOnlyNet() {
        if (transformer != null) {
            return netObservable().compose(transformer)
                    .compose(getResponseConvert().handleStringResult());
        } else
            return netObservable().compose(getResponseConvert().handleResponseBodyResult());
    }

    protected Observable<RxResponse<T>> requestOnlyCache() {
        return getRxCache().<T>get(cacheKey, forceNet, type).map(new Function<CacheResponse<T>, RxResponse<T>>() {
            @Override
            public RxResponse<T> apply(CacheResponse<T> tCacheResponse) throws Exception {
                RxResponse<T> response = new RxResponse<>(true, tCacheResponse.getData());
                return response;
            }
        });
    }

    protected Observable<RxResponse<T>> requestFirstNet() {
        if (transformer != null) {
            return netObservable()
                    .compose(transformer)
                    .compose(getResponseConvert().handleStringResult())
                    .doOnNext(new Consumer<RxResponse<T>>() {
                        @Override
                        public void accept(RxResponse<T> tRxResponse) throws Exception {
                            if (tRxResponse.getData() != null) {
                                saveLocal(tRxResponse.getData());
                            }
                        }
                    })
                    .onExceptionResumeNext(getRxCache().<T>get(cacheKey, forceNet, type)
                            .map(new Function<CacheResponse<T>, RxResponse<T>>() {
                                @Override
                                public RxResponse<T> apply(CacheResponse<T> tCacheResponse) throws Exception {
                                    return new RxResponse<>(true, tCacheResponse.getData());
                                }
                            }));
        } else
            return netObservable()
                    .compose(getResponseConvert().handleResponseBodyResult())
                    .doOnNext(new Consumer<RxResponse<T>>() {
                        @Override
                        public void accept(RxResponse<T> tRxResponse) throws Exception {
                            if (tRxResponse.getData() != null) {
                                saveLocal(tRxResponse.getData());
                            }
                        }
                    })
                    .onErrorResumeNext(new Function<Throwable, ObservableSource<? extends RxResponse<T>>>() {
                        @Override
                        public ObservableSource<? extends RxResponse<T>> apply(Throwable throwable) throws Exception {
                            if (!(throwable instanceof ApiException)) {
                                return getRxCache().<T>get(cacheKey, forceNet, type)
                                        .map(new Function<CacheResponse<T>, RxResponse<T>>() {
                                            @Override
                                            public RxResponse<T> apply(CacheResponse<T> tCacheResponse) throws Exception {
                                                return new RxResponse<>(true, tCacheResponse.getData());
                                            }
                                        });
                            }
                            return Observable.error(throwable);
                        }
                    });
    }

    protected Observable<RxResponse<T>> requestFirstCache() {
        return getRxCache().<T>get(cacheKey, forceNet, type).flatMap(this.<T>flatFuncFirstCache());
    }

    protected Function<CacheResponse<T>, ObservableSource<RxResponse<T>>> flatFuncFirstCache() {
        return new Function<CacheResponse<T>, ObservableSource<RxResponse<T>>>() {
            @Override
            public ObservableSource<RxResponse<T>> apply(CacheResponse<T> tCacheResponse) throws Exception {
                T t = tCacheResponse.getData();
                if (t == null) {
                    if (transformer != null) {
                        return netObservable().compose(transformer)
                                .compose(getResponseConvert().handleStringResult()).doOnNext(new Consumer<RxResponse<T>>() {
                                    @Override
                                    public void accept(RxResponse<T> tRxResponse) throws Exception {
                                        if (tRxResponse.getData() != null)
                                            saveLocal(tRxResponse.getData());
                                    }
                                });
                    } else
                        return netObservable().compose(getResponseConvert().handleResponseBodyResult()).doOnNext(new Consumer<RxResponse<T>>() {
                            @Override
                            public void accept(RxResponse<T> tRxResponse) throws Exception {
                                if (tRxResponse.getData() != null) saveLocal(tRxResponse.getData());
                            }
                        });
                } else {
                    RxResponse<T> response = new RxResponse<>(true, t);
                    return Observable.just(response);
                }
            }
        };
    }

    protected void saveLocal(T t) {
        getRxCache().put(cacheKey, t, cacheTime)
                .compose(RxUtil.<Boolean>io_main())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        LogUtil.i(aBoolean ? "save cache success" : "save cache failed.");
                    }
                });
    }
}