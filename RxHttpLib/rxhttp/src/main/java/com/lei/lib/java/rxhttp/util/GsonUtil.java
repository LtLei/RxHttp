package com.lei.lib.java.rxhttp.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.stream.JsonReader;
import com.lei.lib.java.rxcache.util.LogUtil;
import com.lei.lib.java.rxhttp.entity.IEntity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * 实现Gson解析的操作类
 *
 * @author lei
 */

public class GsonUtil {
    private static Gson gson;

    static {
        gson = new GsonBuilder().registerTypeHierarchyAdapter(Object.class, new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                Class<?> rawType = $Gson$Types.getRawType(typeOfT);
                boolean isArray;
                isArray = List.class.isAssignableFrom(rawType);
                if (!isArray && typeOfT instanceof ParameterizedType) {
                    //有base
                    Class trueType = $Gson$Types.getRawType(((ParameterizedType) typeOfT).getActualTypeArguments()[0]);
                    isArray = List.class.isAssignableFrom(trueType);
                    LogUtil.e("确认是不是列表？" + isArray + ">>>>>>>" + trueType + ",  " + trueType);

                    IEntity iEntity = (IEntity) new Gson().fromJson(json, rawType);
                    boolean isList = List.class.isAssignableFrom(iEntity.getData().getClass());
                    LogUtil.e("再次确认是不是列表？" + isList + ">>>>>>>" + iEntity.getData().getClass());

                    if (isArray && isList || !isArray && !isList) {
                        return new Gson().fromJson(json, typeOfT);
                    } else if (isArray) {
                        iEntity.setData(null);
                        return iEntity;
                    } else {
                        iEntity.setData(null);
                        return iEntity;
                    }
                } else {
                    //没有base
                    if ((isArray && json.isJsonArray()) || (!isArray && !json.isJsonArray())) {
                        return new Gson().fromJson(json, typeOfT);
                    } else {
                        if (isArray) {
                            return Collections.emptyList();
                        } else {
                            return new Gson().fromJson("{}", typeOfT);
                        }
                    }
                }
            }
        })
                .create();
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

    public static <T> T fromJson(String in, Type type) {
        return gson.fromJson(in, type);
    }

    public static <T> T fromJson(JsonReader reader, Type type) {
        return gson.fromJson(reader, type);
    }
}
