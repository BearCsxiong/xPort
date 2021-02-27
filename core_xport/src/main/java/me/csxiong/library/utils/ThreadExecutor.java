package me.csxiong.library.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import me.csxiong.library.integration.scheduler.XThreadFactory;

/**
 * @Desc : 简单的线程切换工具
 * @Author : csxiong create on 2019/7/16
 * <p>
 * 因为常规线程需要切换场景需要
 */
public class ThreadExecutor {

    /**
     * 静态handler,UIThread的Looper,提供简单的线程切换服务
     */
    private static Handler handler;

    /**
     * 闲置线程
     */
    private volatile static XIdleTimingHandler idleHandler;

    /**
     * 快销任务 快速处理 快速释放
     */
    private static ExecutorService fastExecutorService;

    /**
     * 慢销任务 需要足够长的等待和处理
     */
    private static ExecutorService slowExecutorService;


    /**
     * 获取XPort提供的线程池
     */
    /**
     * 单例实现
     *
     * @return
     */
    static {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(Math.max(5, DeviceUtils.getCPUCount()), Math.max(10, DeviceUtils.getCPUCount()), 10L,
                TimeUnit.SECONDS,
                new PriorityBlockingQueue<>(),
                new XThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy());
        executor.allowCoreThreadTimeOut(true);
        fastExecutorService = executor;

        slowExecutorService = new ThreadPoolExecutor(1, Integer.MAX_VALUE, 5L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new XThreadFactory(), new ThreadPoolExecutor.DiscardOldestPolicy());
        handler = new Handler(Looper.getMainLooper());
    }

    private ThreadExecutor() {

    }

    /**
     * 简单的切换线程,切换到主线程
     *
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        runOnUiThread(runnable, 0);
    }

    /**
     * 简单的切换线程,切换到主线程
     *
     * @param runnable
     * @param delay    延迟
     */
    public static void runOnUiThread(Runnable runnable, long delay) {
        if (delay <= 0) {
            if (isUIThread()) {
                runnable.run();
            } else {
                handler.post(runnable);
            }
        } else {
            handler.postDelayed(runnable, delay);
        }
    }

    /**
     * 简单的切换线程,切换到后台运作
     *
     * @param runnable
     */
    public static void runOnBackgroundThread(Runnable runnable) {
        fastExecutorService.execute(runnable);
    }

    /**
     * 同步执行在子线程
     *
     * @param runnable
     */
    public static void runOnBackgroundThreadSync(Runnable runnable) {
        if (isUIThread()) {
            fastExecutorService.execute(runnable);
        } else {
            runnable.run();
        }
    }

    /**
     * 执行在慢销线程
     *
     * @param runnable
     */
    public static void runOnSlowBackgroundThread(Runnable runnable) {
        slowExecutorService.execute(runnable);
    }

    /**
     * 同步执行在子线程
     *
     * @param runnable
     */
    public static void runOnSlowBackgroundThreadSync(Runnable runnable) {
        if (isUIThread()) {
            slowExecutorService.execute(runnable);
        } else {
            runnable.run();
        }
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

    /**
     * 执行在UI线程闲置阶段
     *
     * @param runnable
     */
    public static void runOnIdleTiming(Runnable runnable) {
        if (runnable != null) {
            runOnUiThread(() -> {
                if (idleHandler == null) {
                    idleHandler = new XIdleTimingHandler();
                    Looper.myQueue().addIdleHandler(idleHandler);
                }
                idleHandler.getIdleQueue().add(runnable);
            });
        }
    }

}
