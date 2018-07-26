package com.pa.glide.pahglidemodule.progress;

/**
 * 定义回调接口
 */
public interface OnProgressListener {
    void onProgress(boolean isComplete, int percentage, long bytesRead, long totalBytes);
}
