package com.lei.lib.java.rxhttp.demo;

import android.app.Application;
import android.os.Build;

import com.lei.lib.java.rxhttp.RxHttp;

import java.util.UUID;

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
                .setBaseUrl("http://test.app2.mayi121.com/")
                .addCommonHeader("Request-Type", "mayi-api-request")
                .addCommonHeader("Device-Number", getUniquePsuedoID())
//                .addCommonHeader("Hello", "enen")
//                .addCommonHeader("nihao", "hao")
//                .addCommonHeader("nihao", "hao")
                .setEntity(BaseBean.class)
                .useEntity(true)
        .convertBefore(new ResponseConverter());
//                .convertBefore(new ObservableTransformer<ResponseBody, String>() {
//                    @Override
//                    public ObservableSource<String> apply(Observable<ResponseBody> upstream) {
//                        return upstream.map(new Function<ResponseBody, String>() {
//                            @Override
//                            public String apply(ResponseBody responseBody) throws Exception {
//                                return responseBody.string();
//                            }
//                        });
//                    }
//                });
    }
    //获得独一无二的Psuedo ID
    private static String getUniquePsuedoID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

                Build.USER.length() % 10; //13 位

        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
}
