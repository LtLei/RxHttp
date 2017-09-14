package com.lei.lib.java.rxhttp.service;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * <p>
 * 这是一个通用的Service，封装了各种HTTP请求以及文件上传、下载的API设计
 * </p>
 * <p>
 * 由于是通用的，所以无法设置自定义的javabeans，因此统一返回ResponseBody;
 * 如果在初始化或调用时未设置其他的Service，将使用此Service进行API请求;
 * </p>
 */

public interface RxService {
    /**
     * 发起一个Get请求，从服务端获取资源
     *
     * @param path 除BaseURL外的相对路径
     * @param map  请求的参数
     * @return 返回请求结果，以ResponseBody形式
     */
    @GET("{path}")
    Observable<ResponseBody> get(
            @Path(value = "path", encoded = true) String path,
            @QueryMap() Map<String, String> map
    );

    /**
     * 发起一个Post请求，在服务端创建一个资源
     *
     * @param path 除BaseURL外的相对路径
     * @param map  提交的参数
     * @return 返回请求结果，以ResponseBody形式
     */
    @FormUrlEncoded
    @POST("{path}")
    Observable<ResponseBody> post(
            @Path(value = "path", encoded = true) String path,
            @FieldMap() Map<String, String> map
    );

    /**
     * 以Json形式发起Post请求，在服务端创建一个资源
     *
     * @param path 除BaseURL外的相对路径
     * @param body 传递的Json参数
     * @return 返回请求结果，以ResponseBody形式
     */
    @POST("{path}")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<ResponseBody> postJson(
            @Path(value = "path", encoded = true) String path,
            @Body RequestBody body
    );

    /**
     * 发起Put请求，在服务端更新资源（客户端提供完整的改变后的资源）
     *
     * @param path 除BaseURL外的相对路径
     * @param map  提交的参数
     * @return
     */
    @PUT("{path}")
    Observable<ResponseBody> put(@Path(value = "path", encoded = true) String path, @QueryMap Map<String, String> map);

    /**
     * 发起Patch请求，在服务端更新资源（客户端提供改变的属性）
     *
     * @param path 除BaseURL外的相对路径
     * @param map  提交的参数
     * @return
     */
    @PATCH("{path}")
    Observable<ResponseBody> patch(@Path(value = "path", encoded = true) String path, @QueryMap Map<String, String> map);

    /**
     * 发起Delete请求，从服务端删除资源
     *
     * @param path 除BaseURL外的相对路径
     * @param map  提交的参数
     * @return
     */
    @DELETE("{path}")
    Observable<ResponseBody> delete(@Path(value = "path", encoded = true) String path, @QueryMap Map<String, String> map);

    @Multipart
    @POST
    Observable<ResponseBody> uploadFiles(
            @Url String url,
            @PartMap() Map<String, String> description,
            @Part List<MultipartBody.Part> bodies);

    @Multipart
    @POST
    Observable<ResponseBody> uploadFiles(
            @Url String url,
            @PartMap() Map<String, String> description,
            @PartMap() Map<String, RequestBody> bodies);
}
