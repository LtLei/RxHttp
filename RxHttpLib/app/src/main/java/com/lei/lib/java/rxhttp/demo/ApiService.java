package com.lei.lib.java.rxhttp.demo;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by lei on 2017/8/26.
 */

public interface ApiService {
    @GET("users/{user}/repos")
    Observable<List<Repo>> listRepos(@Path("user") String user);
    @GET("test.php/")
    Observable<BaseBean<UserBean>> getTest();
}
