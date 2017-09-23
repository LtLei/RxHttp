package com.lei.lib.java.rxhttp.util;

import com.google.gson.stream.JsonReader;
import com.lei.lib.java.rxhttp.entity.IEntity;
import com.lei.lib.java.rxhttp.entity.RxResponse;
import com.lei.lib.java.rxhttp.exception.ApiException;

import java.io.Reader;
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

public class ResponseConvert<T> {
    private boolean useEntity;
    private Class<?> clazz;
    private Type type;

    public ResponseConvert(Class<?> clazz, Type type, boolean useEntity) {
        Utilities.checkNotNull(clazz, "clazz is null.");
        Utilities.checkNotNull(type, "type is null.");

        this.clazz = clazz;
        this.type = type;
        this.useEntity = useEntity;
    }

    public ObservableTransformer<ResponseBody, RxResponse<T>> handleResponseBodyResult() {
        return new ObservableTransformer<ResponseBody, RxResponse<T>>() {
            @Override
            public ObservableSource<RxResponse<T>> apply(Observable<ResponseBody> upstream) {
                return upstream.map(new Function<ResponseBody, RxResponse<T>>() {
                    @Override
                    public RxResponse<T> apply(ResponseBody responseBody) throws Exception {
                        if (useEntity) {
                            return transformDataWithEntity(responseBody.charStream());
                        } else {
                            return transformDataNoEntity(responseBody.charStream());
                        }
                    }
                });
            }
        };
    }

    public ObservableTransformer<String, RxResponse<T>> handleStringResult() {
        return new ObservableTransformer<String, RxResponse<T>>() {
            @Override
            public ObservableSource<RxResponse<T>> apply(Observable<String> upstream) {
                return upstream.map(new Function<String, RxResponse<T>>() {
                    @Override
                    public RxResponse<T> apply(String s) throws Exception {
                        if (useEntity) {
                            return transformDataWithEntity(new StringReader(s));
                        } else {
                            return transformDataNoEntity(new StringReader(s));
                        }
                    }
                });
            }
        };
    }

    public RxResponse<T> transformDataWithEntity(Reader in) throws Exception {
        JsonReader jsonReader = new JsonReader(in);
        IEntity<T> iEntity = GsonUtil.fromJson(jsonReader, GsonUtil.type(clazz, type));
        RxResponse<T> response = new RxResponse<>(false);
        if (iEntity == null) {
            response.setData(null);
        } else if (iEntity.isOk()) {
            response.setData(iEntity.getData());
        } else throw new ApiException(iEntity.getCode(), iEntity.getMsg());

        return response;
    }

    public RxResponse<T> transformDataNoEntity(Reader in) throws Exception {
        JsonReader jsonReader = new JsonReader(in);
        T t = GsonUtil.fromJson(jsonReader, type);
        RxResponse<T> response = new RxResponse<>(false, t);
        return response;
    }
}
