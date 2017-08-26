package com.lei.lib.java.rxhttp.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lei.lib.java.rxcache.util.LogUtil;
import com.lei.lib.java.rxhttp.RxHttp;
import com.lei.lib.java.rxhttp.providers.OkHttpProvider;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int a = 4;
        int b = a;
        b = 3;
        LogUtil.e("a = " + a + ", b = " + b);
        List<String> as = new ArrayList<>();
        as.add("hello1");
        as.add("hello2");

        List<String> bs = as;
        bs.add("Heoo;");

        LogUtil.e("as = " + as + ", bs = " + bs);

        OkHttpProvider.Builder builder1 = RxHttp.getInstance()
                .getCommonOkHttpProviderBuilder();
        OkHttpProvider.Builder builder2 = RxHttp.getInstance()
                .getPrimaryOkHttpProviderBuilder();

        OkHttpProvider.Builder builder3= new OkHttpProvider.Builder();
        try {
            builder3 = (OkHttpProvider.Builder) BeanUtils.cloneBean(builder1);
            OkHttpProvider.compare(builder1,builder3);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

}
