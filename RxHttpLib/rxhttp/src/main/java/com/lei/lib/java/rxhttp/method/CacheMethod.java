package com.lei.lib.java.rxhttp.method;

/**
 * Created by lei on 2017/8/26.
 */

public enum CacheMethod {
    ONLY_NET,   //仅使用网络，不使用缓存，这也是默认的缓存方式。
    ONLY_CACHE, //仅使用缓存，不建议使用
    FIRST_CACHE_THEN_NET,   //先从缓存获取，缓存没有再从网络获取
    FIRST_NET_THEN_CACHE,   //先从网络获取，网络没有再使用缓存
    CACHE_AND_NET,  //缓存和网络同时使用，哪个先接收到就使用哪一个
}
