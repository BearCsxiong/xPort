package me.csxiong.library.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import me.csxiong.library.base.APP;
import me.csxiong.library.di.component.DaggerSystemComponent;

/**
 * @Desc : 简单的线程切换工具
 * @Author : csxiong create on 2019/7/16
 */
public class ThreadExecutor {

    private static ThreadExecutor executor;

    /**
     * 静态handler,UIThread的Looper,提供简单的线程切换服务
     */
    public Handler handler;

    /**
     * 获取XPort提供的线程池
     */
    @Inject
    public ExecutorService executorService;

    /**
     * 单例实现
     *
     * @return
     */
    public static ThreadExecutor get() {
        if (executor == null) {
            synchronized (ThreadExecutor.class) {
                if (executor == null) {
                    executor = new ThreadExecutor();
                }
            }
        }
        return executor;
    }

    /**
     * 默认构造 提供简单的注入获取XPort的线程池
     */
    private ThreadExecutor() {
        DaggerSystemComponent.builder()
                .appComponent(APP.get().getAppComponent())
                .build()
                .inject(this);
        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * 简单的切换线程,切换到主线程
     *
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        get().handler.post(runnable);
    }

    /**
     * 简单的切换线程,切换到主线程
     *
     * @param runnable
     * @param delay    延迟
     */
    public static void runOnUiThread(Runnable runnable, long delay) {
        get().handler.postDelayed(runnable, delay);
    }

    /**
     * 简单的切换线程,切换到后台运作
     *
     * @param runnable
     */
    public static void runOnBackgroundThread(Runnable runnable) {
        get().executorService.execute(runnable);
    }

    /**
     * 是否是主线程
     *
     * @return
     */
    public static boolean isUIThread() {
        if (Looper.myLooper() != null && Looper.myLooper() == Looper.getMainLooper()) {
            return true;
        }
        return false;
    }

}
