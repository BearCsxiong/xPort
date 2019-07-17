package com.base.project.ui;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

/**
 * @Desc : 高级的手势器
 * 1.处理手势的部分回调 保持搞笑的调用结构
 * @Author : csxiong - 2019/7/17
 */
public class ProGestureDetector implements View.OnTouchListener {

    /**
     * 处理多手势回调
     */
    private MultiGestureListener multiGestureListener;

    /**
     * 原生滑动手势探测器
     */
    private GestureDetector gestureDetector;

    /**
     * 原生缩放手势探测器
     */
    private ScaleGestureDetector scaleGestureDetector;

    /**
     * 上下文
     */
    private Context context;

    /**
     * 是否是多点触控
     */
    private boolean isMultiTouch;

    public boolean isMultiTouch() {
        return isMultiTouch;
    }

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            multiGestureListener.onScroll(e1, e2, distanceX, distanceY);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            multiGestureListener.onFling(velocityX, velocityY);
            return super.onFling(e1, e2, velocityX, velocityY);
        }

    };

    private ScaleGestureDetector.OnScaleGestureListener scaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return multiGestureListener.onScale(detector.getScaleFactor(), detector.getFocusX(), detector.getFocusY());
        }

    };

    public ProGestureDetector(Context context, MultiGestureListener multiGestureListener) {
        this.context = context;
        this.multiGestureListener = multiGestureListener;
        gestureDetector = new GestureDetector(context, gestureListener);
        scaleGestureDetector = new ScaleGestureDetector(context, scaleGestureListener);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        isMultiTouch = event.getPointerCount() > 1;
        boolean isDisposeGesture = gestureDetector.onTouchEvent(event);
        boolean isDisposeScaleGesture = scaleGestureDetector.onTouchEvent(event);
        return isDisposeGesture || isDisposeScaleGesture;
    }

    /**
     * 多手势回调
     * TODO support multi click
     */
    public interface MultiGestureListener {

        /**
         * 主手指选中
         *
         * @param motionEvent
         */
        void onMajorDown(MotionEvent motionEvent);

        /**
         * 主手指抬起
         *
         * @param motionEvent
         */
        void onMajorUp(MotionEvent motionEvent);

        /**
         * 次手指选中
         *
         * @param motionEvent
         */
        void onMinorDown(MotionEvent motionEvent);

        /**
         * 次手指抬起
         *
         * @param motionEvent
         */
        void onMinorUp(MotionEvent motionEvent);

        /**
         * 滑动
         *
         * @param e1
         * @param e2
         * @param dx x位移量
         * @param dy y位移量
         */
        void onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy);

        /**
         * 缩放
         *
         * @param scaleFactor 缩放因子
         * @param scaleFocusX x缩放中心
         * @param scaleFocusY y缩放中心
         * @return true代表相对上次的缩放因子, false代表相对开始的缩放因子
         */
        boolean onScale(float scaleFactor, float scaleFocusX, float scaleFocusY);

        /**
         * 惯性滑动
         *
         * @param vx x速度
         * @param vy y速度
         */
        void onFling(float vx, float vy);
    }

    public static class SimpleMultiGestureListener implements MultiGestureListener {

        @Override
        public void onMajorDown(MotionEvent motionEvent) {

        }

        @Override
        public void onMajorUp(MotionEvent motionEvent) {

        }

        @Override
        public void onMinorDown(MotionEvent motionEvent) {

        }

        @Override
        public void onMinorUp(MotionEvent motionEvent) {

        }

        @Override
        public void onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {

        }

        @Override
        public boolean onScale(float scaleFactor, float scaleFocusX, float scaleFocusY) {
            return false;
        }

        @Override
        public void onFling(float vx, float vy) {

        }
    }
}
