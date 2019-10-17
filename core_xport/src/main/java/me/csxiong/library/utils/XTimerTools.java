package me.csxiong.library.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;

/**
 * @Desc : 时间工具
 * @Author : csxiong - 2019-09-26
 */
public class XTimerTools {

    public static XTimer create(Runnable runnable, long stopInFutureMillis) {
        return new XTimer(stopInFutureMillis, false, runnable);
    }

    public static XTimer infinite(Runnable runnable, long intervalMillis) {
        return new XTimer(intervalMillis, true, runnable);
    }

    /**
     * @Desc : 计时器 替代CountDownTimer
     * @Author : csxiong - 2019-09-03
     */
    public static class XTimer {

        private ValueAnimator timer = ValueAnimator.ofInt(0);

        private boolean isInfinite;

        private Runnable mRunnable;

        public XTimer(long millisInFuture, boolean isInfinite, Runnable runnable) {
            this.isInfinite = isInfinite;
            this.mRunnable = runnable;
            timer.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationRepeat(Animator animation) {
                    super.onAnimationRepeat(animation);
                    if (XTimer.this.isInfinite) {
                        if (mRunnable != null) {
                            mRunnable.run();
                        }
                    }
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    if (mRunnable != null) {
                        mRunnable.run();
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (!isInfinite && mRunnable != null) {
                        mRunnable.run();
                    }
                }
            });
            if (this.isInfinite) {
                timer.setRepeatCount(ValueAnimator.INFINITE);
                timer.setRepeatMode(ValueAnimator.RESTART);
            } else {
                timer.setRepeatCount(1);
            }
            timer.setDuration(millisInFuture);
        }

        public void cancel() {
            if (timer != null) {
                timer.cancel();
            }
        }

        public void start() {
            startDelay(0);
        }

        public void startDelay(long delayMillis) {
            if (timer != null) {
                timer.setStartDelay(delayMillis);
                timer.start();
            }
        }

        public boolean isRunning() {
            if (timer != null) {
                return timer.isRunning();
            }
            return false;
        }
    }

}
