package com.lei.lib.java.rxhttp.util;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lei.lib.java.rxhttp.entity.RxEntity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Rx转换类
 *
 * @author lei
 */

public class RxUtil {
    private static Gson gson = new Gson();

    public static <S,T> ObservableTransformer<S, T> data_with_entity(final Type type) {
        return new ObservableTransformer<S, T>() {
            @Override
            public ObservableSource<T> apply(Observable<S> upstream) {
                return upstream.flatMap(new Function<S, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(S s) throws Exception {
                        JsonReader jsonReader = null;
                        if (s instanceof ResponseBody) {
                            jsonReader = new JsonReader(new InputStreamReader(((ResponseBody) s).byteStream()));
                        }else if (s instanceof String){
                            jsonReader = new JsonReader(new StringReader((String) s));
                        }else if (s instanceof InputStream){
                            jsonReader = new JsonReader(new InputStreamReader((InputStream) s));
                        }else {
                            throw new IllegalArgumentException("Unknown UpStream");
                        }

                        Type type1 = GsonUtil.type(RxEntity.class, type);
                        RxEntity<T> rxEntity = gson.fromJson(jsonReader, type1);
                        if (rxEntity.getCode()==2000)
                        return Observable.just(rxEntity.getData());
                        else throw new Exception("MSSSSSSSSSSSS");
                    }
                });
            }
        };
    }
    public static <S,T>ObservableTransformer<S,T> data_no_entity(final Type type){
        return new ObservableTransformer<S, T>() {
            @Override
            public ObservableSource<T> apply(Observable<S> upstream) {
                return upstream.flatMap(new Function<S, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(S s) throws Exception {
                        JsonReader jsonReader = null;
                        if (s instanceof ResponseBody) {
                            jsonReader = new JsonReader(new InputStreamReader(((ResponseBody) s).byteStream()));
                        }else if (s instanceof String){
                            jsonReader = new JsonReader(new StringReader((String) s));
                        }else if (s instanceof InputStream){
                            jsonReader = new JsonReader(new InputStreamReader((InputStream) s));
                        }else {
                            throw new IllegalArgumentException("Unknown UpStream");
                        }

                        T data = gson.fromJson(jsonReader, type);
                        return Observable.just(data);
                    }
                });
            }
        };
    }
}
