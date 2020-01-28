package me.csxiong.library.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;

/**
 * @Desc : 动画基础监听的封装
 * 使调用更加线性和好用
 * @Author : csxiong - 2020-01-26
 */
public class XAnimation implements Animator.AnimatorListener, Animator.AnimatorPauseListener, ValueAnimator.AnimatorUpdateListener {

    /**
     * 动画监听主体
     */
    private ValueAnimator valueAnimator;

    /**
     * 动画监听
     */
    private XAnimationListener animationListener;

    /**
     * BugFix 修复部分机型对于onAnimationUpdate回调的问题
     */
    private boolean isStart = false;

    private XAnimation(float... floats) {
        valueAnimator = ValueAnimator.ofFloat(floats);
        valueAnimator.addUpdateListener(this);
        valueAnimator.addListener(this);
        valueAnimator.addPauseListener(this);
    }

    public XAnimation duration(long duration) {
        valueAnimator.setDuration(duration);
        return this;
    }

    public XAnimation restartMode() {
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        return this;
    }

    public XAnimation reverseMode() {
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        return this;
    }

    public XAnimation repeatCount(int count) {
        valueAnimator.setRepeatCount(count);
        return this;
    }

    public XAnimation setAnimationListener(XAnimationListener animationListener) {
        this.animationListener = animationListener;
        return this;
    }

    public void startDelay(long duration) {
        valueAnimator.setStartDelay(duration);
        valueAnimator.start();
    }

    public void start() {
        valueAnimator.setStartDelay(0);
        valueAnimator.start();
    }

    public void cancel() {
        valueAnimator.cancel();
    }

    public static XAnimation ofFloat(float... floats) {
        return new XAnimation(floats);
    }

    @Override
    public void onAnimationStart(Animator animation) {
        isStart = true;
        if (animationListener != null) {
            animationListener.onAnimationStart(this);
        }
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        isStart = false;
        if (animationListener != null) {
            animationListener.onAnimationEnd(this);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        isStart = false;
        if (animationListener != null) {
            animationListener.onAnimationCancel(this);
        }
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationPause(Animator animation) {

    }

    @Override
    public void onAnimationResume(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (isStart && animationListener != null) {
            animationListener.onAnimationUpdate(animation.getAnimatedFraction(), (Float) animation.getAnimatedValue());
        }
    }

    /**
     * @Desc : 动画进度监听
     * @Author : csxiong - 2020-01-26
     */
    public interface XAnimationListener {

        /**
         * 动画进度监听
         *
         * @param fraction
         * @param value
         */
        void onAnimationUpdate(float fraction, float value);

        /**
         * 动画开始监听
         *
         * @param animation
         */
        void onAnimationStart(XAnimation animation);

        /**
         * 动画结束监听
         *
         * @param animation
         */
        void onAnimationEnd(XAnimation animation);

        /**
         * 动画取消监听
         *
         * @param animation
         */
        void onAnimationCancel(XAnimation animation);
    }
}
