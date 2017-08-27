package com.lei.lib.java.rxhttp.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.lei.lib.java.rxcache.util.LogUtil;
import com.lei.lib.java.rxcache.util.RxUtil;

import java.util.List;
import java.util.logging.Level;

import io.reactivex.functions.Consumer;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("RxHttp");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);
        OkHttpClient.Builder ok = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(ok.build());
//        builder.build().create(ApiService.class)
//                .listRepos("LtLei")
//                .compose(RxUtil.<List<Repo>>io_main())
//                .subscribe(new Consumer<List<Repo>>() {
//                    @Override
//                    public void accept(List<Repo> repos) throws Exception {
//                        Log.e("H",repos.toString());
//                    }
//                });
//                .compose(RxUtil.<BaseBean<UserBean>>io_main())
//                .subscribe(new Consumer<BaseBean<UserBean>>() {
//                    @Override
//                    public void accept(BaseBean<UserBean> userBeanBaseBean) throws Exception {
//                        LogUtil.e(userBeanBaseBean.getData().toString());
//                    }
//                });
    }

}
