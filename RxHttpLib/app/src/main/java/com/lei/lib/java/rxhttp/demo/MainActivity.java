package com.lei.lib.java.rxhttp.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.lei.lib.java.rxcache.util.LogUtil;
import com.lei.lib.java.rxhttp.util.GsonUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

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

                String t1 = "{\"name\":\"lei\"}";
                String t2 = "{}";
                String t3 = "[{\"name\":\"lei\"},{\"name\":\"lei\"}]";
                String t4 = "[]";

                User user = GsonUtil.fromJson(t1, User.class);
                LogUtil.e("Test ==== user true " + user.toString());
                user = GsonUtil.fromJson(t4, User.class);
                LogUtil.e("Test ==== user false " + user.toString());

                List<User> userList = GsonUtil.fromJson(t3, new TypeToken<List<User>>() {
                }.getType());
                LogUtil.e("Test ==== list true " + userList.toString());
                userList = GsonUtil.fromJson(t2, new TypeToken<List<User>>() {
                }.getType());
                LogUtil.e("Test ==== list false " + userList.toString());

                String t5 = "{\"code\":100,\"message\":\"1111\",\"data\":{\"name\":\"lei\"}}";
                String t8 = "{\"code\":100,\"message\":\"1111\",\"data\":{}}";
                String t7 = "{\"code\":100,\"message\":\"1111\",\"data\":[{\"name\":\"lei\"},{\"name\":\"lei\"}]}";
                String t6 = "{\"code\":100,\"message\":\"1111\",\"data\":[]}";

                Type type = GsonUtil.type(BaseBean.class, new Type[]{User.class});
                BaseBean<User> userBaseBean = GsonUtil.fromJson(t5, type);
                LogUtil.e("Test ==== base user true " + userBaseBean.toString());
                userBaseBean = GsonUtil.fromJson(t6, type);
                LogUtil.e("Test ==== base user false " + userBaseBean.toString());

                Type type1 = GsonUtil.type(BaseBean.class, new TypeToken<List<User>>() {
                }.getType());
                BaseBean<List<User>> listBaseBean = GsonUtil.fromJson(t7, type1);
                LogUtil.e("Test ==== base list user true " + listBaseBean.toString());
                listBaseBean = GsonUtil.fromJson(t8, type1);
                LogUtil.e("Test ==== base list user false " + listBaseBean.toString());

                int[] a = {11, 22, 3, 4, 55};
                int[] b = {22, 11, 33, 4, 54};
                int[] c = {12, 11, 33, 4, 55};

                int[][] data = {a, b, c};
                int[] flag = new int[a.length];
                for (int i = 0; i < a.length; i++) {
                    int tmp = data[0][i];
                    for (int j = 1; j < data.length; j++) {
                        if (tmp < data[j][i]) {
                            LogUtil.e(tmp + ":::" + data[j][i]);
                            flag[i] = j;
                            tmp = data[j][i];
                        }
                    }
                }
                for (int i = 0; i < flag.length; i++) {
                    LogUtil.e("顺序" + flag[i]);
                }

            }
        });

    }

    public static ParameterizedType type(final Type raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }

    private Gson gson(final boolean needObject) {
        return new GsonBuilder()
                .registerTypeHierarchyAdapter(Object.class, new JsonDeserializer<Object>() {
                    @Override
                    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        LogUtil.e("哈哈" + typeOfT);
                        Type type = new TypeToken<List<User>>() {
                        }.getType();
                        LogUtil.e("哈哈" + typeOfT.getClass().isInstance(type));
                        if (needObject) {
                            if (json.isJsonObject()) {
                                LogUtil.e("1111");
                                return new Gson().fromJson(json, typeOfT);
                            } else {
                                LogUtil.e("2222");
                                return new Gson().fromJson("{}", typeOfT);
                            }
                        } else {
                            if (json.isJsonArray()) {
                                LogUtil.e("3333");
                                return new Gson().fromJson(json, typeOfT);
                            } else {
                                LogUtil.e("4444");
                                return new Gson().fromJson("[]", typeOfT);
                            }
                        }
                    }
                }).create();
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
