package com.lei.lib.java.rxhttp.demo;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lei.lib.java.rxcache.util.RxUtil;
import com.lei.lib.java.rxhttp.RxHttp;
import com.lei.lib.java.rxhttp.progress.ProgressListener;

import java.io.File;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {
    int preBytes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*RxHttp.getInstance()
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
                        });*/

                final Notification.Builder notifyBuilder = new Notification.Builder(MainActivity.this)
                        .setContentTitle("开始下载")
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setAutoCancel(false);
                final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notifyBuilder.getNotification());


                RxHttp.getInstance()
                        .download("http://p1.exmmw.cn/p1/dl/huocaier.apk", BaseBean.class)
                        .setProgressListener(new ProgressListener() {
                            @Override
                            public void onProgress(long currentBytes, long contentLength, boolean done) {
                                int progress = (int) (currentBytes * 100 / contentLength);
                                if (preBytes < progress) {
                                    Log.e("测试", "进度是：" + progress + "是否完成？" + done);
                                    notifyBuilder.setProgress(100, progress, false);
                                    Notification notification =notifyBuilder.getNotification();
                                    notification.flags=Notification.FLAG_NO_CLEAR|Notification.FLAG_FOREGROUND_SERVICE;
                                    notificationManager.notify(0, notification);
                                }
                                preBytes = progress;
                            }
                        })
                        .setOutputFile(new File(Environment.getExternalStoragePublicDirectory
                                (Environment.DIRECTORY_DOWNLOADS), "heihei.apk"))
                        .excute()
                        .compose(RxUtil.<Boolean>io_main())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean b) throws Exception {

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
                        });
            }
        });
    }

}
