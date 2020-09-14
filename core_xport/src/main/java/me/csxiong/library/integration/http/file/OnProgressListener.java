package me.csxiong.library.integration.http.file;

import androidx.annotation.WorkerThread;

/**
 * @Desc : 进度监听
 * @Author : Bear - 2020/4/3
 */
public interface OnProgressListener {

    @WorkerThread
    default void onStart() {
    }

    /**
     * 进度回调
     * @param currentWrite 当前写长度
     * @param contentLength 总长度
     */
    @WorkerThread
    void onProgress(long currentWrite, long contentLength);

}
