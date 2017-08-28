package com.lei.lib.java.rxhttp.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.lei.lib.java.rxcache.util.LogUtil;
import com.lei.lib.java.rxcache.util.RxUtil;
import com.lei.lib.java.rxhttp.RxHttp;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Type type = new TypeToken<List<Repo>>() {
        }.getType();
        RxHttp.getInstance()
//                .get("users/LtLei/repos")
                .postJson("appConfig")
                .map(new Function<ResponseBody, String>() {
                    @Override
                    public String apply(ResponseBody responseBody) throws Exception {
                        String str = responseBody.string();
                        LogUtil.e(str);
                        return str;
                    }
                })
                .map(new Function<String, List<Repo>>() {
                    @Override
                    public List<Repo> apply(String s) throws Exception {
                        JsonReader jsonReader = new JsonReader(new StringReader(s));
                        Gson gson = new Gson();
                        List<Repo> list = gson.fromJson(jsonReader, type);
                        return list;
                    }
                })
                .compose(RxUtil.<List<Repo>>io_main())
                .subscribe(new Consumer<List<Repo>>() {
                    @Override
                    public void accept(List<Repo> repos) throws Exception {
                        Log.d("REPO: ", repos.get(0).toString());
                    }
                });
    }

}
