package com.lei.lib.java.rxhttp.progress;

public interface ProgressListener {
    void onProgress(long currentBytes, long contentLength, boolean done);
}