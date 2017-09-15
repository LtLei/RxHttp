package com.lei.lib.java.rxhttp.demo;

import android.app.Application;

import com.lei.lib.java.rxhttp.RxHttp;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

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
        RxHttp.init(this);

        //http://ditu.amap.com/service/regeo?longitude=121.04925573429551&latitude=31.315590522490712
        RxHttp.getInstance()
                .setBaseUrl("http://192.168.1.115:8090/")
                .addCommonHeader("Hello", "enen")
                .addCommonHeader("nihao", "hao")
                .setEntity(BaseBean.class)
                .useEntity(true)
                .convertBefore(new ObservableTransformer<ResponseBody, String>() {
                    @Override
                    public ObservableSource<String> apply(Observable<ResponseBody> upstream) {
                        return upstream.map(new Function<ResponseBody, String>() {
                            @Override
                            public String apply(ResponseBody responseBody) throws Exception {
                                return responseBody.string();
                            }
                        });
                    }
                });
    }
}
