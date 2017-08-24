package com.lei.lib.java.rxhttp.method;

/**
 * <p>
 * 发起HTTP请求的方法
 * GET: 发起Get请求，从服务端获取资源
 * POST: 发起Post请求，在服务端创建一个资源
 * JSON_POST:以Json形式发起Post请求，在服务端创建一个资源
 * PUT: 发起Put请求，在服务端更新资源（客户端提供完整的改变后的资源）
 * PATCH: 发起Patch请求，在服务端更新资源（客户端提供改变的属性）
 * DELETE: 发起Delete请求，从服务端删除资源
 * </p>
 */

public enum HttpMethod {
    GET,
    POST,
    JSON_POST,
    PUT,
    PATCH,
    DELETE
}