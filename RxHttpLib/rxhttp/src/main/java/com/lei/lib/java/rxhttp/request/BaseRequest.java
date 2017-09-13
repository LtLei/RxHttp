package com.lei.lib.java.rxhttp.request;

import android.app.Application;

import com.google.gson.stream.JsonReader;
import com.lei.lib.java.rxcache.RxCache;
import com.lei.lib.java.rxcache.entity.CacheResponse;
import com.lei.lib.java.rxcache.util.LogUtil;
import com.lei.lib.java.rxcache.util.RxUtil;
import com.lei.lib.java.rxhttp.RxHttp1;
import com.lei.lib.java.rxhttp.entity.IEntity;
import com.lei.lib.java.rxhttp.entity.RxEntity;
import com.lei.lib.java.rxhttp.entity.RxResponse;
import com.lei.lib.java.rxhttp.exception.ApiException;
import com.lei.lib.java.rxhttp.interceptors.HeadInterceptor;
import com.lei.lib.java.rxhttp.method.CacheMethod;
import com.lei.lib.java.rxhttp.service.RxService;
import com.lei.lib.java.rxhttp.util.GsonUtil;
import com.lei.lib.java.rxhttp.util.Utilities;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

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

/**
 * 发起请求的基类，用于配置一些共用的属性
 *
 * @author lei
 * @since 2017年9月11日
 */

public abstract class BaseRequest {
    private LinkedHashMap<String, String> headers = new LinkedHashMap<>();
    private LinkedHashMap<String, String> params = new LinkedHashMap<>();
    private CacheMethod cacheMethod;
    private boolean useEntity;
    private boolean debug;

    private Type type;
    private boolean forceNet;

    private ObservableTransformer<ResponseBody, String> transformer;

    private OkHttpClient.Builder okBuilder;
    private Retrofit.Builder retrofitBuilder;
    private RxCache.Builder rxCacheBuilder;

    private String requestUrl;
    private long cacheTime = -1;
    private String cacheKey;
    private Class<?> clazz;

    public BaseRequest(Application context) {
//        Type genType = getClass().getGenericSuperclass();
//        type = ((ParameterizedType) genType).getActualTypeArguments()[0];

        RxHttp1 rxHttp = RxHttp1.getInstance();

        headers.putAll(rxHttp.getCommonHeaders());
        params.putAll(rxHttp.getCommonParams());
        cacheMethod = rxHttp.getCommonCacheMethod();
        useEntity = rxHttp.isUseEntity();
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

    public BaseRequest setEntity(Class<?> entityClass) {
        Utilities.checkNotNull(entityClass, "class of entity is null.");
        this.clazz = entityClass;
        return this;
    }

    public BaseRequest setType(Type type) {
        this.type = type;
        return this;
    }

    public BaseRequest addHeader(String key, String content) {
        Utilities.checkNullOrEmpty(key, "key is null or empty.");
        this.headers.put(key, content);
        return this;
    }

    public BaseRequest addHeaders(LinkedHashMap<String, String> headers) {
        Utilities.checkNotNull(headers, "headers is null.");
        this.headers.putAll(headers);
        return this;
    }

    public BaseRequest removeHeader(String key) {
        Utilities.checkNullOrEmpty(key, "key is null or empty.");
        this.headers.remove(key);
        return this;
    }

    public BaseRequest removeAllHeaders() {
        this.headers.clear();
        return this;
    }

    public BaseRequest addParam(String key, String content) {
        Utilities.checkNullOrEmpty(key, "key is null or empty.");
        this.params.put(key, content);
        return this;
    }

    public BaseRequest addParams(LinkedHashMap<String, String> params) {
        Utilities.checkNotNull(params, "params is null.");
        this.params.putAll(params);
        return this;
    }

    public BaseRequest removeParam(String key) {
        Utilities.checkNullOrEmpty(key, "key is null or empty.");
        this.params.remove(key);
        return this;
    }

    public BaseRequest removeAllParams() {
        this.params.clear();
        return this;
    }

    public BaseRequest cacheMethod(CacheMethod cacheMethod) {
        Utilities.checkNotNull(cacheMethod, "cacheMethod is null.");
        this.cacheMethod = cacheMethod;
        return this;
    }

    public BaseRequest setCacheKey(String cacheKey) {
        Utilities.checkNullOrEmpty(cacheKey, "cacheKey is null or empty.");
        this.cacheKey = cacheKey;
        return this;
    }

    public BaseRequest setCacheTime(long cacheTime) {
        if (cacheTime < -1) cacheTime = -1;
        this.cacheTime = cacheTime;
        return this;
    }

    public BaseRequest setRequestUrl(String requestUrl) {
        if (requestUrl == null) requestUrl = "";
        this.requestUrl = requestUrl;
        return this;
    }

    protected OkHttpClient getOkHttpClient() {
        return okBuilder
                .addInterceptor(new HeadInterceptor(headers)).build();
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

    public String getCacheKey() {
        return cacheKey;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public <T> Observable<RxResponse<T>> request() {
        switch (cacheMethod) {
            case ONLY_NET:
                return BaseRequest.this.<T>requestOnlyNet();
            case ONLY_CACHE:
                return BaseRequest.this.<T>requestOnlyCache();
            case FIRST_NET_THEN_CACHE:
                return BaseRequest.this.<T>requestFirstNet();
            case FIRST_CACHE_THEN_NET:
                return BaseRequest.this.<T>requestFirstCache();
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

    protected <T> Observable<RxResponse<T>> requestOnlyNet() {
        if (transformer != null) {
            return netObservable().compose(transformer)
                    .compose(this.<T>handleStringResult(type, useEntity));
        } else
            return netObservable().compose(this.<T>handleResponseBodyResult(type, useEntity));
    }

    protected <T> Observable<RxResponse<T>> requestOnlyCache() {
        return getRxCache().<T>get(cacheKey, forceNet, type).map(new Function<CacheResponse<T>, RxResponse<T>>() {
            @Override
            public RxResponse<T> apply(CacheResponse<T> tCacheResponse) throws Exception {
                RxResponse<T> response = new RxResponse<>(true, tCacheResponse.getData());
                return response;
            }
        });
    }

    protected <T> Observable<RxResponse<T>> requestFirstNet() {
        if (transformer != null) {
            return netObservable()
                    .compose(transformer)
                    .compose(this.<T>handleStringResult(type, useEntity))
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
                    .compose(this.<T>handleResponseBodyResult(type, useEntity))
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

    protected <T> Observable<RxResponse<T>> requestFirstCache() {
        return getRxCache().<T>get(cacheKey, forceNet, type).flatMap(this.<T>flatFuncFirstCache());
    }

    protected <T> Function<CacheResponse<T>, ObservableSource<RxResponse<T>>> flatFuncFirstCache() {
        return new Function<CacheResponse<T>, ObservableSource<RxResponse<T>>>() {
            @Override
            public ObservableSource<RxResponse<T>> apply(CacheResponse<T> tCacheResponse) throws Exception {
                T t = tCacheResponse.getData();
                if (t == null) {
                    if (transformer != null) {
                        return netObservable().compose(transformer)
                                .compose(BaseRequest.this.<T>handleStringResult(type, useEntity)).doOnNext(new Consumer<RxResponse<T>>() {
                                    @Override
                                    public void accept(RxResponse<T> tRxResponse) throws Exception {
                                        if (tRxResponse.getData() != null)
                                            saveLocal(tRxResponse.getData());
                                    }
                                });
                    } else
                        return netObservable().compose(BaseRequest.this.<T>handleResponseBodyResult(type, useEntity)).doOnNext(new Consumer<RxResponse<T>>() {
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

    protected <T> void saveLocal(T t) {
        getRxCache().put(cacheKey, t, cacheTime)
                .compose(RxUtil.<Boolean>io_main())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        LogUtil.i(aBoolean ? "save cache success" : "save cache failed.");
                    }
                });
    }

    protected <T> ObservableTransformer<ResponseBody, RxResponse<T>> handleResponseBodyResult(final Type type, final boolean useEntity) {
        return new ObservableTransformer<ResponseBody, RxResponse<T>>() {
            @Override
            public ObservableSource<RxResponse<T>> apply(Observable<ResponseBody> upstream) {
                return upstream.map(new Function<ResponseBody, RxResponse<T>>() {
                    @Override
                    public RxResponse<T> apply(ResponseBody responseBody) throws Exception {
                        if (useEntity) {
                            return BaseRequest.this.<T>transformDataWithEntity(type, responseBody.charStream());
                        } else {
                            return BaseRequest.this.<T>transformDataNoEntity(type, responseBody.charStream());
                        }
                    }
                });
            }
        };
    }

    protected <T> ObservableTransformer<String, RxResponse<T>> handleStringResult(final Type type, final boolean useEntity) {
        return new ObservableTransformer<String, RxResponse<T>>() {
            @Override
            public ObservableSource<RxResponse<T>> apply(Observable<String> upstream) {
                return upstream.map(new Function<String, RxResponse<T>>() {
                    @Override
                    public RxResponse<T> apply(String s) throws Exception {
                        if (useEntity) {
                            return BaseRequest.this.<T>transformDataWithEntity(type, new StringReader(s));
                        } else {
                            return BaseRequest.this.<T>transformDataNoEntity(type, new StringReader(s));
                        }
                    }
                });
            }
        };
    }

    protected <T> RxResponse<T> transformDataWithEntity(Type type, Reader in) throws Exception {
        JsonReader jsonReader = new JsonReader(in);
        if (clazz == null) clazz = RxEntity.class;

//        if (type == null) {
//            ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
//            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
//            for (Type actualTypeArgument : actualTypeArguments) {
//                System.out.println(actualTypeArgument);
//            }
//            type = actualTypeArguments[0];
//        }
        //type = type1;//((ParameterizedType)genType).getActualTypeArguments()[0];

        Type geType = GsonUtil.type(clazz, type);
        IEntity<T> iEntity = GsonUtil.fromJson(jsonReader, geType);
        RxResponse<T> response = new RxResponse<>(false);
        if (iEntity == null)

        {
            response.setData(null);
        } else if (iEntity.isOk())

        {
            response.setData(iEntity.getData());
        } else throw new

                    ApiException(iEntity.getCode(), iEntity.

                    getMsg());

        return response;
    }

    protected <T> RxResponse<T> transformDataNoEntity(Type type, Reader in) throws Exception {
        JsonReader jsonReader = new JsonReader(in);
        T t = GsonUtil.fromJson(jsonReader, type);
        RxResponse<T> response = new RxResponse<>(false, t);
        return response;
    }
}