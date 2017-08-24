package com.lei.lib.java.rxhttp.cache;

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
    private CacheProvider() {
        throw new ExceptionInInitializerError("CacheProvider not support instantiation.");
    }

    public static class Builder {
        private boolean debug = true;
        private CacheMode cacheMode = CacheMode.BOTH;
        private IConverter converter = new GsonConverter();
        private int memoryCacheSizeByMB = (int) (Runtime.getRuntime().maxMemory() / 8 / 1024 / 1024);
        private int diskCacheSizeByMB = 100;
        private String diskDirName = "RxHttp";

        public Builder() {

        }

        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder setCacheMode(CacheMode cacheMode) {
            this.cacheMode = Utilities.checkNotNull(cacheMode, "cacheMode is null.");
            return this;
        }

        public Builder setConverter(IConverter converter) {
            this.converter = Utilities.checkNotNull(converter, "converter is null.");
            return this;
        }

        public Builder setMemoryCacheSizeByMB(int memoryCacheSizeByMB) {
            if (memoryCacheSizeByMB < 0) throw new IllegalArgumentException("memoryCacheSize < 0.");
            this.memoryCacheSizeByMB = memoryCacheSizeByMB;
            return this;
        }

        public Builder setDiskCacheSizeByMB(int diskCacheSizeByMB) {
            if (diskCacheSizeByMB < 0) throw new IllegalArgumentException("diskCacheSize < 0.");
            this.diskCacheSizeByMB = diskCacheSizeByMB;
            return this;
        }

        public Builder setDiskDirName(String diskDirName) {
            this.diskDirName = Utilities.checkNullOrEmpty(diskDirName, "diskDirName is null or empty.");
            return this;
        }

        public RxCache.Builder build() {
            RxCache.Builder cacheBuilder = new RxCache.Builder();
            cacheBuilder.setDebug(debug);
            cacheBuilder.setCacheMode(cacheMode);
            cacheBuilder.setConverter(converter);
            cacheBuilder.setMemoryCacheSizeByMB(memoryCacheSizeByMB);
            cacheBuilder.setDiskCacheSizeByMB(diskCacheSizeByMB);
            cacheBuilder.setDiskDirName(diskDirName);

            return cacheBuilder;
        }
    }
}
