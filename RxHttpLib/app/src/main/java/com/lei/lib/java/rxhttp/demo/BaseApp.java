package com.lei.lib.java.rxhttp.demo;

import android.app.Application;

import com.lei.lib.java.rxhttp.RxHttp;
import com.lei.lib.java.rxhttp.method.CacheMethod;

import java.util.List;
import java.util.logging.Level;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by rymyz on 2017/8/23.
 */

public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RxHttp.init(this);
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("RxHttp");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);
        Observable<List<Repo>> dataObservable = RxHttp.getInstance()
                .addInterceptor(loggingInterceptor)
                .setBaseUrl("https://api.github.com/")
                .setCommonCacheMethod(CacheMethod.FIRST_CACHE_THEN_NET)
                .setCommonApiService(ApiService.class)
                .setCacheKey("test")
                .setCacheTime(10000)
                .<ApiService>createService().listRepos("LtLei")
                .doOnNext(new Consumer<List<Repo>>() {
                    @Override
                    public void accept(List<Repo> repos) throws Exception {

                    }
                });

    }
}
