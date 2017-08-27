package com.lei.lib.java.rxhttp.demo;

/**
 * Created by lei on 2017/8/27.
 */

public class UserBean {
    private String user_name;
    private String user_pass;

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_pass() {
        return user_pass;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "user_name='" + user_name + '\'' +
                ", user_pass='" + user_pass + '\'' +
                '}';
    }
}
