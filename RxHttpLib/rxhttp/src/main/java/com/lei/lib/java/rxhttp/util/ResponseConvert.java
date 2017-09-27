package com.lei.lib.java.rxhttp.util;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.lei.lib.java.rxhttp.entity.IEntity;
import com.lei.lib.java.rxhttp.entity.RxResponse;
import com.lei.lib.java.rxhttp.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.List;

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
        RxResponse<T> response = new RxResponse<>(false);

        IEntity iEntity = GsonUtil.fromJson(jsonReader, clazz);
        if (iEntity == null) {
            response.setData(null);
        } else if (iEntity.isOk()) {
            if (!type.getClass().isArray() && !iEntity.getData().getClass().isArray()) {
                IEntity<T> entity = GsonUtil.fromJson(jsonReader, GsonUtil.type(clazz, new Type[]{type}));
                response.setData(entity.getData());
            } else if (type.getClass().isArray() && iEntity.getData().getClass().isArray()) {
                Type arrayType = GsonUtil.type(new TypeToken<List>() {
                }.getType(), new Type[]{type});
                IEntity<T> entity = GsonUtil.fromJson(jsonReader, GsonUtil.type(clazz, new Type[]{arrayType}));
                response.setData(entity.getData());
            } else if (type.getClass().isArray() && !iEntity.getData().getClass().isArray()) {
                response.setData((T) new JSONArray("[]"));
            } else {
                response.setData((T) new JSONObject("{}"));
            }

        } else throw new ApiException(iEntity.getCode(), iEntity.getMsg());

        return response;
    }

    public RxResponse<T> transformDataWithEntity(String in) throws Exception {
        RxResponse<T> response = new RxResponse<>(false);

        IEntity iEntity = GsonUtil.fromJson(in, clazz);
        if (iEntity == null) {
            response.setData(null);
        } else if (iEntity.isOk()) {
            if (!type.getClass().isArray() && !iEntity.getData().getClass().isArray()) {
                IEntity<T> entity = GsonUtil.fromJson(in, GsonUtil.type(clazz, new Type[]{type}));
                response.setData(entity.getData());
            } else if (type.getClass().isArray() && iEntity.getData().getClass().isArray()) {
                Type arrayType = GsonUtil.type(new TypeToken<List>() {
                }.getType(), new Type[]{type});
                IEntity<T> entity = GsonUtil.fromJson(in, GsonUtil.type(clazz, new Type[]{arrayType}));
                response.setData(entity.getData());
            } else if (type.getClass().isArray() && !iEntity.getData().getClass().isArray()) {
                response.setData((T) new JSONArray("[]"));
            } else {
                response.setData((T) new JSONObject("{}"));
            }

        } else throw new ApiException(iEntity.getCode(), iEntity.getMsg());

        return response;
    }


    public RxResponse<T> transformDataNoEntity(Reader in) throws Exception {
        JsonReader jsonReader = new JsonReader(in);
        T t = GsonUtil.fromJson(jsonReader, type);
        RxResponse<T> response = new RxResponse<>(false, t);
        return response;
    }

    public RxResponse<T> transformDataNoEntity(String in) throws Exception {
        T t = GsonUtil.fromJson(in, type);
        RxResponse<T> response = new RxResponse<>(false, t);
        return response;
    }
}
