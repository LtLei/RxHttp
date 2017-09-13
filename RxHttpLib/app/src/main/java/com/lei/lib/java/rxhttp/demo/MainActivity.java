package com.lei.lib.java.rxhttp.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.lei.lib.java.rxcache.util.RxUtil;
import com.lei.lib.java.rxhttp.RxHttp1;
import com.lei.lib.java.rxhttp.entity.RxResponse;
import com.lei.lib.java.rxhttp.method.CacheMethod;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
                long begin1 = System.nanoTime();
                long f1 = 0;
                for (int i = 0; i < 1000000; i++) {
                    long begin = System.nanoTime();
                    f1 = getNthNumber1(i);
                    long end = System.nanoTime();
                    System.out.println("第" + i + "个斐波那契数是" + f1 + ", 耗时" + TimeUnit.NANOSECONDS.toMillis(end - begin) + "毫秒");

                }
                long end1 = System.nanoTime();
                System.out.println(f1 + ", 耗时" + TimeUnit.NANOSECONDS.toMillis(end1 - begin1) + "毫秒");
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxHttp1.getInstance()
                        .get()
                        .setRequestUrl("index")
                        .setCacheKey("test")
                        .setCacheTime(1000 * 60 * 60)
                        .cacheMethod(CacheMethod.ONLY_NET)
                        .setEntity(BaseBean.class)
                        .setType(new TypeToken<UserBean>() {
                        }.getType())
                        .<UserBean>request()
                        .compose(RxUtil.<RxResponse<UserBean>>io_main())
                        .subscribe(new Consumer<RxResponse<UserBean>>() {
                            @Override
                            public void accept(RxResponse<UserBean> userBeanRxResponse) throws Exception {
                                Log.e("测试", (userBeanRxResponse.isLocal() ? "数据从缓存获得" : "数据从网络获得")
                                        + ": "
                                        + (userBeanRxResponse.getData()==null?"这是一个空的结果哦":userBeanRxResponse.getData().toString()));
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("测试", throwable.getLocalizedMessage());
                                throwable.printStackTrace();
                            }
                        });

            }
        });


    }

    public static long calcWithoutRecursion(long n) {
        if (n < 0)
            return 0;
        if (n == 0 || n == 1) {
            return 1;
        }
        long fib = 0;
        long fibOne = 1;
        long fibTwo = 1;
        for (long i = 1; i < n; i++) {
            fib = (fibOne + fibTwo) % 1000000007;
            fibTwo = fibOne % 1000000007;
            fibOne = fib % 1000000007;
        }
        return fib % 1000000007;
    }


    long[][] f = new long[][]{{0, 1}, {1, 1}};

    public long getNthNumber1(int n) {
        long[][] fib_result = new long[2][2];
        int result[] = {1, 1};
        if (n < 2)
            return result[n];
        pow(n, fib_result);
        return fib_result[0][1];
    }

    private void pow(int n, long[][] Result) {
        long AResult[][] = new long[2][2];
        long zResult1[][] = new long[2][2];
        long zResult2[][] = new long[2][2];

        AResult[0][0] = 1;
        AResult[0][1] = 1;
        AResult[1][0] = 1;
        AResult[1][1] = 0;
        if (n == 1) {
            Result[0][0] = 1;
            Result[0][1] = 1;
            Result[1][0] = 1;
            Result[1][1] = 0;
        } else if (n % 2 == 0) {
            pow(n / 2, zResult1);
            MUL(zResult1, zResult1, Result);
        } else {
            pow((n - 1) / 2, zResult1);
            MUL(zResult1, zResult1, zResult2);
            MUL(zResult2, AResult, Result);
        }
    }

    /************************************************************************/
/* 两个 矩阵相乘  、结果矩阵保存在 MatrixResult[2][2]中       */

    /************************************************************************/
    void MUL(long MatrixA[][], long MatrixB[][], long MatrixResult[][])//矩阵相乘
    {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                MatrixResult[i][j] = 0;
                for (int k = 0; k < 2; k++) {
                    MatrixResult[i][j] = MatrixResult[i][j] + MatrixA[i][k] * MatrixB[k][j];
                }
            }
        }
    }

    private long[][] fun(long[][] f, long[][] m) {
        long[][] temp = new long[2][2];
        temp[0][0] = (f[0][0] * m[0][0] + f[0][1] * m[1][0]);
        Log.e("测试00 ", "" + temp[0][0]);
        temp[0][1] = (f[0][0] * m[0][1] + f[0][1] * m[1][1]);
        Log.e("测试01 ", "" + temp[0][1]);
        temp[1][0] = (f[1][0] * m[0][0] + f[1][1] * m[1][0]);
        Log.e("测试10 ", "" + temp[1][0]);
        temp[1][1] = (f[1][0] * m[0][1] + f[1][1] * m[1][1]);
        Log.e("测试11 ", "" + temp[1][1]);
        return temp;
    }
}
