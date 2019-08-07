package me.csxiong.library.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import me.csxiong.library.base.APP;
import me.csxiong.library.di.component.DaggerSystemComponent;
import me.csxiong.library.di.module.ClientModule;

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
     * {@link ClientModule#provideExecutor()}
     */
    @Inject
    public ExecutorService executorService;

    /**
     * 单例实现
     *
     * @return
     */
    public static ThreadExecutor get() {
        synchronized (ThreadExecutor.class) {
            if (executor == null) {
                executor = new ThreadExecutor();
            }
        }
        return executor;
    }

    /**
     * 默认构造 提供简单的注入获取XPort的线程池
     * {@link ClientModule#provideExecutor()}
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
    public void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    /**
     * 简单的切换线程,切换到主线程
     *
     * @param runnable
     * @param delay    延迟
     */
    public void runOnUiThread(Runnable runnable, long delay) {
        handler.postDelayed(runnable, delay);
    }

    /**
     * 简单的切换线程,切换到后台运作
     *
     * @param runnable
     */
    public void runOnBackgroundThread(Runnable runnable) {
        executorService.execute(runnable);
    }

}
