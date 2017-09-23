package com.lei.lib.java.rxhttp.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.lei.lib.java.rxcache.util.LogUtil;
import com.lei.lib.java.rxcache.util.RxUtil;
import com.lei.lib.java.rxhttp.RxHttp;
import com.lei.lib.java.rxhttp.entity.RxResponse;
import com.lei.lib.java.rxhttp.exception.RxException;
import com.lei.lib.java.rxhttp.subscriber.FailCunsumer;
import com.lei.lib.java.rxhttp.util.GsonUtil;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.List;

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
                        .<User>get("",User.class)
                        .request()
                        .compose(RxUtil.<RxResponse<User>>io_main())
                        .subscribe(new Consumer<RxResponse<User>>() {
                            @Override
                            public void accept(RxResponse<User> userRxResponse) throws Exception {
                                LogUtil.e("数据" + userRxResponse.getData().toString());
                            }
                        }, new FailCunsumer() {
                            @Override
                            public void _onFail(int code, String message) {
LogUtil.e(message);
                            }

                            @Override
                            public void _onError(RxException e) {
                                LogUtil.e(e.getMsg());
                            }
                        });*/

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

                /*final Notification.Builder notifyBuilder = new Notification.Builder(MainActivity.this)
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
                        });*/


                String jsonStr = "{" +
                        "    \"code\": 1," +
                        "    \"message\": \"1111\"," +
                        "    \"data\": {" +
                        "    }" +
                        "}";
                String arr = "{\"code\":1111,\"message\":\"1111\",\"data\":[{\"name\":\"lei\"},{\"name\":\"lei\"},{\"name\":\"lei\"}]}";
                Type type = GsonUtil.type(BaseBean.class, new TypeToken<List<User>>(){}.getType());
                BaseBean<List<User>> userBaseBean = GsonUtil.fromJson(new JsonReader(new StringReader(jsonStr)), type);
                Log.e("特使1", userBaseBean.getData().toString());

                BaseBean<List<User>> userBaseBean1 = GsonUtil.fromJson(new JsonReader(new StringReader(arr)), type);
                Log.e("特使1", userBaseBean1.getData().toString());

                Type type1 = GsonUtil.type(BaseBean.class, User.class);
                BaseBean<User> userBaseBean2= GsonUtil.fromJson(new JsonReader(new StringReader(jsonStr)), type1);
                Log.e("特使2", userBaseBean2.getData().toString());

                String arr1 = "{\"code\":1111,\"message\":\"1111\",\"data\":[]}";
                BaseBean<User> userBaseBean3= GsonUtil.fromJson(new JsonReader(new StringReader(arr1)), type1);
                Log.e("特使2", userBaseBean3.getData().toString());
            }
        });

        }


    public class User {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
