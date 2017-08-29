package com.lei.lib.java.rxhttp.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.lei.lib.java.rxcache.util.RxUtil;
import com.lei.lib.java.rxhttp.RxHttp;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Observable;

import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RxHttp.getInstance()
                .get("index")
                .preConvert(new Function<ResponseBody, String>() {
                    @Override
                    public String apply(ResponseBody responseBody) throws Exception {
                        return responseBody.string();
                    }
                })
                .compose(com.lei.lib.java.rxhttp.util.RxUtil.<String,UserBean>data_with_entity(UserBean.class))
                .compose(RxUtil.<UserBean>io_main())
                .subscribe(new Consumer<UserBean>() {
                    @Override
                    public void accept(UserBean userBean) throws Exception {
                        Log.e("H", userBean.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("H", throwable.getMessage());
                    }
                });

        final Type type = new TypeToken<List<Repo>>() {
        }.getType();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxHttp.getInstance()
                        .useEntity(true)
                        .addPrimaryHeader("Nihao", "Enen")
                        .postJson("index")
                        .map(new Function<ResponseBody, String>() {
                            @Override
                            public String apply(ResponseBody responseBody) throws Exception {
                                return responseBody.string();
                            }
                        })
                        .compose(RxHttp.getInstance().<String,UserBean>convert(UserBean.class))
                        .compose(RxUtil.<UserBean>io_main())
                        .subscribe(new Consumer<UserBean>() {
                            @Override
                            public void accept(UserBean userBean) throws Exception {
                                Log.d("H", userBean.toString());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d("H",throwable.getMessage());
                            }
                        });
            }
        });
    }
}
