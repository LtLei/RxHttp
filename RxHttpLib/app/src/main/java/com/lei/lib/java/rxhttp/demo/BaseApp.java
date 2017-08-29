package com.lei.lib.java.rxhttp.demo;

import android.app.Application;

import com.lei.lib.java.rxhttp.RxHttp;

import java.util.logging.Level;

/**
 * Created by rymyz on 2017/8/23.
 */

public class BaseApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        System.setProperty("http.proxyHost", "192.168.1.115");   //个人测试网络时用的，删掉即可
//        System.setProperty("http.proxyPort", "8888");

        RxHttp.init(this);
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("RxHttp1");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);


        RxHttp.getInstance()
                .addCommonHeader("Nihao", "Woyehao")
                .addNetworkInterceptor(loggingInterceptor)
                .addCommonHeader("Name", "Lei")
//                .setBaseUrl("https://api.github.com/")
                .setBaseUrl("http://127.0.0.1:8090/")
                .useEntity(false);
    }
}
