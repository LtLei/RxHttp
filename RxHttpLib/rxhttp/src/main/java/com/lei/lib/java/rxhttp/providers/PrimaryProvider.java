package com.lei.lib.java.rxhttp.providers;

import com.lei.lib.java.rxhttp.method.CacheMethod;
import com.lei.lib.java.rxhttp.util.Utilities;

import java.util.LinkedHashMap;

/**
 * 仅本次生效的参数设置类
 *
 * @author lei
 */

public class PrimaryProvider {
    //==============================================================
    //                          单独生效的变量设置
    //      1、设置缓存的key以及缓存的Time
    //      2、添加仅本次生效的Header和Param的功能，以及删除这些Header和Param的功能
    //      3、更换一个临时的ApiService的功能，若不设置，则使用全局的，设置后，本次将使用设置的
    //      4、单独设置缓存的方式，若设置，仅本次生效，否则使用全局的设置。
    //      5、未定。。。
    //==============================================================
    private String cacheKey;
    private long cacheTime;
    private LinkedHashMap<String, String> headers = new LinkedHashMap<>();
    private LinkedHashMap<String, String> params = new LinkedHashMap<>();
    private Class<?> apiService;
    private CacheMethod cacheMethod;

    public PrimaryProvider() {
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = Utilities.checkNullOrEmpty(cacheKey, "cacheKey is null or empty.");
    }

    public void setCacheTime(long cacheTime) {
        if (cacheTime <= -1) cacheTime = -1;
        this.cacheTime = cacheTime;
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

    public void setApiService(Class<?> apiService) {
        Utilities.checkNotNull(apiService, "apiService is null.");
        this.apiService = apiService;
    }

    public void setCacheMethod(CacheMethod cacheMethod) {
        this.cacheMethod = cacheMethod;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public LinkedHashMap<String, String> getHeaders() {
        return headers;
    }

    public LinkedHashMap<String, String> getParams() {
        return params;
    }

    public Class<?> getApiService() {
        return apiService;
    }

    public CacheMethod getCacheMethod() {
        return cacheMethod;
    }
}
