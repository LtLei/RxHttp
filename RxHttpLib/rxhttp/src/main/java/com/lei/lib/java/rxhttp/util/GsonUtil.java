package com.lei.lib.java.rxhttp.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;

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
    private static GsonBuilder gson;

    static {
        gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(List.class, new JsonDeserializer<List<?>>() {
                    @Override
                    public List<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        if (json.isJsonArray() && typeOfT.getClass().isArray()) {
                            return new Gson().fromJson(json, typeOfT);
                        } else {
                            return Collections.emptyList();
                        }
                    }
                })
        ;
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
        return gson.create().fromJson(reader, type);
    }
}
