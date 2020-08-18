package me.csxiong.library.integration.scheduler;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import me.csxiong.library.BuildConfig;

/**
 * @Desc : 默认ThreadFactory实现线程创建的工程方法
 * @Author : csxiong create on 2019/7/16
 */
public class XThreadFactory implements ThreadFactory {

    private static String TAG = "XThread";

    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final String namePrefix;

    public XThreadFactory() {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        namePrefix = "XPort thread pool No." + poolNumber.getAndIncrement() + ", thread No.";
    }

    @Override
    public Thread newThread(@NonNull Runnable runnable) {
        String threadName = namePrefix + threadNumber.getAndIncrement();
        Logger.t(TAG).i("Thread production, name is [" + threadName + "]");
        Thread thread = new Thread(group, runnable, threadName, 0);
        //设为非后台线程
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }
        //优先级为normal
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        // 捕获多线程处理中的异常
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                if (BuildConfig.DEBUG) {
                    Logger.t(TAG).e("Runtime Exception in Thread[" + thread.getName() + "]\n" + "with\t->\t" + ex.getMessage());
                }
            }
        });
        return thread;
    }
}
