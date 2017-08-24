package com.lei.lib.java.rxhttp.demo;

import android.app.Application;

import com.lei.lib.java.rxhttp.RxHttp;

/**
 * Created by rymyz on 2017/8/23.
 */

public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RxHttp.init(this);
        new RxHttp.Builder()
                .setBaseUrl("http://www.baidu.com/")
                .build();
    }
}
