package com.lei.lib.java.rxhttp.providers;

import com.lei.lib.java.rxhttp.util.Utilities;

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

/**
 * OkHttp的管理工具类，为配置OkHttp而存在
 *
 * @author lei
 */

public class OkHttpProvider {
    private OkHttpClient.Builder okBuilder;

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

    public OkHttpProvider() {
        okBuilder = new OkHttpClient.Builder();
    }

    public OkHttpProvider(OkHttpClient.Builder okBuilder) {
        this.okBuilder = Utilities.checkNotNull(okBuilder, "okBuilder is null.");
    }

    /**
     * 设置链接超时时间
     *
     * @param connectTimeout
     * @return
     */
    public void setConnectTimeout(long connectTimeout) {
        if (connectTimeout <= 0) throw new IllegalArgumentException("connectTimeout < 0.");
        this.connectTimeout = connectTimeout;
    }

    /**
     * 设置读取超时时间
     *
     * @param readTimeout
     */
    public void setReadTimeout(long readTimeout) {
        if (readTimeout <= 0) throw new IllegalArgumentException("readTimeout < 0.");
        this.readTimeout = readTimeout;
    }

    /**
     * 设置写入超时时间
     *
     * @param writeTimeout
     */
    public void setWriteTimeout(long writeTimeout) {
        if (writeTimeout <= 0) throw new IllegalArgumentException("writeTimeout < 0.");
        this.writeTimeout = writeTimeout;
    }

    /**
     * 添加拦截器
     *
     * @param interceptor
     * @return
     */
    public void addInterceptor(Interceptor interceptor) {
        this.interceptors.add(Utilities.checkNotNull(interceptor, "interceptor is null."));
    }

    /**
     * 批量添加拦截器
     *
     * @param interceptors
     * @return
     */
    public void addInterceptors(List<Interceptor> interceptors) {
        this.interceptors.addAll(Utilities.checkNotNull(interceptors, "interceptors is null."));
    }

    /**
     * 添加网络拦截器
     *
     * @param networkInterceptor
     * @return
     */
    public void addNetworkInterceptor(Interceptor networkInterceptor) {
        this.netInterceptors.add(Utilities.checkNotNull(networkInterceptor, "networkInterceptor is null."));
    }

    /**
     * 批量添加网络拦截器
     *
     * @param networkInterceptors
     * @return
     */
    public void addNetworkInterceptors(List<Interceptor> networkInterceptors) {
        this.netInterceptors.addAll(Utilities.checkNotNull(networkInterceptors, "networkInterceptors is null."));
    }

    /**
     * 设置认证
     *
     * @param authenticator
     * @return
     */
    public void setAuthenticator(Authenticator authenticator) {
        this.authenticator = Utilities.checkNotNull(authenticator, "authenticator is null");
    }

    /**
     * 设置CookieJar
     *
     * @param cookieJar
     */
    public void setCookieJar(CookieJar cookieJar) {
        this.cookieJar = Utilities.checkNotNull(cookieJar, "cookieJar is null.");
    }

    /**
     * 服务器证书验证
     *
     * @param hostnameVerifier
     */
    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = Utilities.checkNotNull(hostnameVerifier, "hostnameVerifier is null.");
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager x509TrustManager) {
        this.sslSocketFactory = Utilities.checkNotNull(sslSocketFactory, "sslSocketFactory is null.");
        this.x509TrustManager = x509TrustManager;
    }

    public void generateBuilder() {
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

        okBuilder.sslSocketFactory(sslSocketFactory, x509TrustManager);
    }


    public OkHttpClient.Builder getOkBuilder() {
        return this.okBuilder;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public long getReadTimeout() {
        return readTimeout;
    }

    public long getWriteTimeout() {
        return writeTimeout;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public List<Interceptor> getNetInterceptors() {
        return netInterceptors;
    }

    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public CookieJar getCookieJar() {
        return cookieJar;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public X509TrustManager getX509TrustManager() {
        return x509TrustManager;
    }
}
