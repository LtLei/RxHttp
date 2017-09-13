package com.lei.lib.java.rxhttp.util;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 实现Gson解析的操作类
 *
 * @author lei
 */

public class GsonUtil {
    private static Gson gson;

    static {
        gson = new Gson();
    }

    public static ParameterizedType type(final Class raw, final Type... args) {
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

    public static <T> T fromJson(JsonReader reader, Type type) {
        return gson.fromJson(reader, type);
    }
}
