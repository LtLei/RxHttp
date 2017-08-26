package com.lei.lib.java.rxhttp.providers;

import android.app.Application;

import com.lei.lib.java.rxcache.RxCache;
import com.lei.lib.java.rxcache.converter.GsonConverter;
import com.lei.lib.java.rxcache.converter.IConverter;
import com.lei.lib.java.rxcache.mode.CacheMode;
import com.lei.lib.java.rxhttp.util.Utilities;

/**
 * 缓存的管理工具类
 *
 * @author lei
 */

public class CacheProvider {
    private RxCache.Builder cacheBuilder;
    private boolean debug = true;
    private CacheMode cacheMode = CacheMode.BOTH;
    private IConverter converter = new GsonConverter();
    private int memoryCacheSizeByMB = (int) (Runtime.getRuntime().maxMemory() / 8 / 1024 / 1024);
    private int diskCacheSizeByMB = 100;
    private String diskDirName = "RxHttp";

    public CacheProvider(Application context) {
        RxCache.init(context);
        cacheBuilder = new RxCache.Builder();
    }

    public CacheProvider(Application context, RxCache.Builder cacheBuilder) {
        RxCache.init(context);
        this.cacheBuilder = Utilities.checkNotNull(cacheBuilder, "cacheBuilder is null.");
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setCacheMode(CacheMode cacheMode) {
        this.cacheMode = Utilities.checkNotNull(cacheMode, "cacheMode is null.");
    }

    public void setConverter(IConverter converter) {
        this.converter = Utilities.checkNotNull(converter, "converter is null.");
    }

    public void setMemoryCacheSizeByMB(int memoryCacheSizeByMB) {
        if (memoryCacheSizeByMB < 0) throw new IllegalArgumentException("memoryCacheSize < 0.");
        this.memoryCacheSizeByMB = memoryCacheSizeByMB;
    }

    public void setDiskCacheSizeByMB(int diskCacheSizeByMB) {
        if (diskCacheSizeByMB < 0) throw new IllegalArgumentException("diskCacheSize < 0.");
        this.diskCacheSizeByMB = diskCacheSizeByMB;
    }

    public void setDiskDirName(String diskDirName) {
        this.diskDirName = Utilities.checkNullOrEmpty(diskDirName, "diskDirName is null or empty.");
    }

    public void generateBuilder() {
        cacheBuilder.setDebug(debug);
        cacheBuilder.setCacheMode(cacheMode);
        cacheBuilder.setConverter(converter);
        cacheBuilder.setMemoryCacheSizeByMB(memoryCacheSizeByMB);
        cacheBuilder.setDiskCacheSizeByMB(diskCacheSizeByMB);
        cacheBuilder.setDiskDirName(diskDirName);
    }

    public RxCache.Builder getCacheBuilder() {
        return cacheBuilder;
    }

    public CacheMode getCacheMode() {
        return cacheMode;
    }

    public IConverter getConverter() {
        return converter;
    }

    public int getMemoryCacheSizeByMB() {
        return memoryCacheSizeByMB;
    }

    public int getDiskCacheSizeByMB() {
        return diskCacheSizeByMB;
    }

    public String getDiskDirName() {
        return diskDirName;
    }
}
