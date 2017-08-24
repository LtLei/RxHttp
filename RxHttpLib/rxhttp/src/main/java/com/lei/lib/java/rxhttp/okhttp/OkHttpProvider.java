package com.lei.lib.java.rxhttp.okhttp;

import com.lei.lib.java.rxcache.util.LogUtil;
import com.lei.lib.java.rxhttp.util.Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Authenticator;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * OkHttp的管理工具类，为配置OkHttp而存在
 *
 * @author lei
 */

public class OkHttpProvider {

    private OkHttpProvider() {
        throw new ExceptionInInitializerError("OkHttpProvider not support instantiation.");
    }

    public static class Builder {
        private static final long DEFAULT_TIMEOUT = 60 * 1000;
        private long connectTimeout = DEFAULT_TIMEOUT;
        private long readTimeout = DEFAULT_TIMEOUT;
        private long writeTimeout = DEFAULT_TIMEOUT;
        private List<Interceptor> interceptors = new ArrayList<>();
        private List<Interceptor> netInterceptors = new ArrayList<>();
        private Authenticator authenticator;
        private CookieJar cookieJar;
        private HostnameVerifier hostnameVerifier;
        private SSLSocketFactory sslSocketFactory;
        private X509TrustManager x509TrustManager;

        public Builder() {
        }

        /**
         * 设置链接超时时间
         *
         * @param connectTimeout
         * @return
         */
        public Builder setConnectTimeout(long connectTimeout) {
            if (connectTimeout <= 0) throw new IllegalArgumentException("connectTimeout < 0.");
            this.connectTimeout = connectTimeout;
            return this;
        }

        /**
         * 设置读取超时时间
         *
         * @param readTimeout
         */
        public Builder setReadTimeout(long readTimeout) {
            if (readTimeout <= 0) throw new IllegalArgumentException("readTimeout < 0.");
            this.readTimeout = readTimeout;
            return this;
        }

        /**
         * 设置写入超时时间
         *
         * @param writeTimeout
         */
        public Builder setWriteTimeout(long writeTimeout) {
            if (writeTimeout <= 0) throw new IllegalArgumentException("writeTimeout < 0.");
            this.writeTimeout = writeTimeout;
            return this;
        }

        /**
         * 添加拦截器
         *
         * @param interceptor
         * @return
         */
        public Builder addInterceptor(Interceptor interceptor) {
            this.interceptors.add(Utilities.checkNotNull(interceptor, "interceptor is null."));
            return this;
        }

        /**
         * 批量添加拦截器
         *
         * @param interceptors
         * @return
         */
        public Builder addInterceptors(List<Interceptor> interceptors) {
            this.interceptors.addAll(Utilities.checkNotNull(interceptors, "interceptors is null."));
            return this;
        }

        /**
         * 添加网络拦截器
         *
         * @param networkInterceptor
         * @return
         */
        public Builder addNetworkInterceptor(Interceptor networkInterceptor) {
            this.netInterceptors.add(Utilities.checkNotNull(networkInterceptor, "networkInterceptor is null."));
            return this;
        }

        /**
         * 批量添加网络拦截器
         *
         * @param networkInterceptors
         * @return
         */
        public Builder addNetworkInterceptors(List<Interceptor> networkInterceptors) {
            this.netInterceptors.addAll(Utilities.checkNotNull(networkInterceptors, "networkInterceptors is null."));
            return this;
        }

        /**
         * 设置认证
         *
         * @param authenticator
         * @return
         */
        public Builder setAuthenticator(Authenticator authenticator) {
            this.authenticator = Utilities.checkNotNull(authenticator, "authenticator is null");
            return this;
        }

        /**
         * 设置CookieJar
         *
         * @param cookieJar
         */
        public Builder setCookieJar(CookieJar cookieJar) {
            this.cookieJar = Utilities.checkNotNull(cookieJar, "cookieJar is null.");
            return this;
        }

        /**
         * 服务器证书验证
         *
         * @param hostnameVerifier
         */
        public Builder setHostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.hostnameVerifier = Utilities.checkNotNull(hostnameVerifier, "hostnameVerifier is null.");
            return this;
        }

        public Builder setSslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager x509TrustManager) {
            this.sslSocketFactory = Utilities.checkNotNull(sslSocketFactory, "sslSocketFactory is null.");
            this.x509TrustManager = x509TrustManager;
            return this;
        }

        public OkHttpClient.Builder build() {
            OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();

            okBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
            okBuilder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
            okBuilder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
            if (!interceptors.isEmpty()) {
                for (Interceptor i : interceptors) {
                    okBuilder.addInterceptor(i);
                }
            }
            if (!netInterceptors.isEmpty()) {
                for (Interceptor i : netInterceptors) {
                    okBuilder.addNetworkInterceptor(i);
                }
            }
            if (authenticator != null) {
                okBuilder.authenticator(authenticator);
            }
            if (cookieJar != null) {
                okBuilder.cookieJar(cookieJar);
            }
            if (hostnameVerifier != null) {
                okBuilder.hostnameVerifier(hostnameVerifier);
            }

            //暂未实现
//            okBuilder.sslSocketFactory(sslSocketFactory, x509TrustManager);

            return okBuilder;
        }
    }

    public static OkHttpProvider.Builder copyOf(final OkHttpProvider.Builder builder) {
        Builder builder1 = new Builder();
        //int 直接复制
        builder1.connectTimeout=builder.connectTimeout;
        builder1.readTimeout = builder.readTimeout;
        builder1.writeTimeout = builder.writeTimeout;

        //对象得这样复制
        builder1.interceptors.addAll(builder.interceptors);
        builder1.netInterceptors.addAll(builder.netInterceptors);

        //以下几个暂时不管了，用到时候封装完了再做处理
        Authenticator authenticator1 = new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                return builder.authenticator.authenticate(route,response);
            }
        };//builder.authenticator;
        builder1.authenticator = authenticator1;

        CookieJar cookieJar1 = builder.cookieJar;
        builder1.cookieJar = cookieJar1;

        HostnameVerifier hostnameVerifier1 = builder.hostnameVerifier;
        builder1.hostnameVerifier = hostnameVerifier1;

        SSLSocketFactory sslSocketFactory1 = builder.sslSocketFactory;
        builder1.sslSocketFactory = sslSocketFactory1;

        X509TrustManager x509TrustManager1 = builder.x509TrustManager;
        builder1.x509TrustManager = x509TrustManager1;

        return builder1;
    }

    public static void compare(Builder builder1, Builder builder) {
        LogUtil.e("1" + (builder1.connectTimeout == builder.connectTimeout));
        LogUtil.e("2" + (builder1.readTimeout == builder.readTimeout));
        LogUtil.e("3" + (builder1.writeTimeout == builder.writeTimeout));
        LogUtil.e("4" + (builder1.interceptors == builder.interceptors));
        LogUtil.e("5" + (builder1.netInterceptors == builder.netInterceptors));
        LogUtil.e("6" + (builder1.authenticator == builder.authenticator));
        LogUtil.e("7" + (builder1.cookieJar == builder.cookieJar));
        LogUtil.e("8" + (builder1.hostnameVerifier == builder.hostnameVerifier));
        LogUtil.e("9" + (builder1.sslSocketFactory == builder.sslSocketFactory));
        LogUtil.e("10" + (builder1.x509TrustManager == builder.x509TrustManager));
    }
}
