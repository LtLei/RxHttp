package com.lei.lib.java.rxhttp.util;

/**
 * 工具类
 *
 * @author lei
 */

public class Utilities {
    public static <T> T checkNotNull(T t, String message) {
        if (t == null)
            throw new NullPointerException(message);
        return t;
    }
    public static String checkNullOrEmpty(String s,String message){
        if (s==null||s.isEmpty())
            throw new NullPointerException(message);
        return s;
    }
}
