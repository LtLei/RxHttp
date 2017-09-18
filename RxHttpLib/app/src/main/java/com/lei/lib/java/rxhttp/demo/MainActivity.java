package com.lei.lib.java.rxhttp.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lei.lib.java.rxcache.util.LogUtil;
import com.lei.lib.java.rxcache.util.RxUtil;
import com.lei.lib.java.rxhttp.RxHttp;
import com.lei.lib.java.rxhttp.entity.RxResponse;
import com.lei.lib.java.rxhttp.method.CacheMethod;
import com.lei.lib.java.rxhttp.subscriber.FailCunsumer;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxHttp.getInstance()
                        .<UserBean>delete("index", UserBean.class)
                        .addHeader("Hedada", "hengheng")
                        .addParam("test", "1")
                        .cacheKey("test1")
                        .cacheTime(1000 * 5)
                        .cacheMethod(CacheMethod.FIRST_CACHE_THEN_NET)
                        .request()
                        .compose(RxUtil.<RxResponse<UserBean>>io_main())
                        .doOnDispose(new Action() {
                            @Override
                            public void run() throws Exception {
                                Log.e("测试", "结束了");
                            }
                        })
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                Log.e("测试", "开始请求了");
                            }
                        })
                        .subscribe(new Consumer<RxResponse<UserBean>>() {
                            @Override
                            public void accept(RxResponse<UserBean> userBeanRxResponse) throws Exception {
                                Log.e("测试", (userBeanRxResponse.isLocal() ? "数据从缓存获得" : "数据从网络获得")
                                        + ": "
                                        + (userBeanRxResponse.getData() == null ? "这是一个空的结果哦" : userBeanRxResponse.getData().toString()));
                            }
                        }, new FailCunsumer() {
                            @Override
                            public void _onFail(int code, String message) {
                                LogUtil.e(message);
                            }
                        });
               /* RxHttp.getInstance()
                        .download("http://192.168.1.115:8090/test.pdf", BaseBean.class)
                        .setProgressListener(new UIProgressListener() {
                            @Override
                            public void onUIProgress(long currentBytes, long contentLength, boolean done) {
                                Log.e("测试", "进度是：" + ((int) (currentBytes * 100 / contentLength)) + "是否完成？" + done);
                            }
                        })
                        .setOutputFile(new File(Environment.getExternalStoragePublicDirectory
                                (Environment.DIRECTORY_DOWNLOADS), "file2.pdf"))
                        .excute()
                        .compose(RxUtil.<InputStream>io_main())
                        .subscribe(new Consumer<InputStream>() {
                            @Override
                            public void accept(InputStream inputStream) throws Exception {

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("测试", "下载失败了，" + throwable.getMessage());
                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {
                                Log.e("测试", "下载完成了");
                            }
                        });*/

            }
        });
    }

}
