package me.csxiong.library.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * @Desc : 时间工具
 * @Author : csxiong - 2019-09-26
 */
public class XTimerTools {

    public static XTimer create(Runnable runnable, long stopInFutureMillis) {
        return new XTimer(stopInFutureMillis, false, runnable);
    }

    public static XTimer infinite(Runnable runnable, long space) {
        return new XTimer(space, true, runnable);
    }

    /**
     * @Desc : 计时器 替代CountDownTimer
     * @Author : csxiong - 2019-09-03
     */
    public static class XTimer {

        private final Handler uiHandler = new Handler(Looper.getMainLooper());

        private boolean isInfinite;

        private long space;

        private Runnable targetTask;

        private final Runnable executeRunnable = ()->{
            if (targetTask != null) {
                targetTask.run();
            }
            if (isInfinite) {
                startDelay(space);
            }
        };

        public XTimer(long space, boolean isInfinite, Runnable targetTask) {
            this.space = space;
            this.isInfinite = isInfinite;
            this.targetTask = targetTask;
        }

        public void cancel() {
            uiHandler.removeCallbacksAndMessages(executeRunnable);
        }

        public void start() {
            startDelay(0);
        }

        public void startDelay(long delayMillis) {
            if (targetTask != null) {
                uiHandler.postDelayed(executeRunnable, delayMillis);
            }
        }
    }

}
