package me.csxiong.library.utils.gesture;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

/**
 * @Desc : 手势检测包装类型 主要是包装手势的一些简易操作
 * @Author : csxiong - 2020-01-28
 */
public class XGestureDetector {

    /**
     * 原生手势检测 -> 支持单指操作
     */
    private android.view.GestureDetector gestureDetector;

    /**
     * 原生scale检测 -> 支持多指检测
     */
    private ScaleGestureDetector scaleGestureDetector;

    /**
     * 是否是多点触控
     */
    private boolean isMultiTouch;

    /**
     * 手势检测回调
     */
    private OnGestureListener onGestureListener;

    private GestureDetector.OnGestureListener viewGestureListener = new GestureDetector.OnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!isMultiTouch && onGestureListener != null) {
                onGestureListener.onScroll(distanceX, distanceY);
            }
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (!isMultiTouch && onGestureListener != null) {
                onGestureListener.onLongPress();
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (onGestureListener != null) {
                onGestureListener.onFling(velocityX, velocityY);
            }
            return false;
        }
    };

    private GestureDetector.OnDoubleTapListener viewDoubleTapListener = new GestureDetector.OnDoubleTapListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (onGestureListener != null) {
                onGestureListener.onSingleTap(e);
            }
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (onGestureListener != null) {
                onGestureListener.onDoubleTap(e);
            }
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            //包含所有doubletap中的所有方法的回调可以掌控细节
            return false;
        }
    };

    private ScaleGestureDetector.OnScaleGestureListener onScaleGestureListener = new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (onGestureListener != null) {
                onGestureListener.onScale(detector.getScaleFactor(), detector.getFocusX(), detector.getFocusY());
            }
            //返回ture为触发模式 ture为fraction每次相对之前的值的计算
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            if (onGestureListener != null) {
                onGestureListener.onScaleStart();
            }
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            if (onGestureListener != null) {
                onGestureListener.onScaleEnd();
            }
        }
    };

    public XGestureDetector(Context context) {
        gestureDetector = new GestureDetector(context, viewGestureListener);
        gestureDetector.setOnDoubleTapListener(viewDoubleTapListener);
        scaleGestureDetector = new ScaleGestureDetector(context, onScaleGestureListener);
        scaleGestureDetector.setQuickScaleEnabled(true);
    }

    /**
     * Touch事件拦截
     *
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        boolean isMultiTouch = event.getPointerCount() > 1;
        if (this.isMultiTouch != isMultiTouch) {
            if (onGestureListener != null) {
                onGestureListener.onMultiTouchChange(isMultiTouch, event.getPointerCount());
            }
        }
        this.isMultiTouch = isMultiTouch;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (onGestureListener != null) {
                onGestureListener.onActionDown();
            }
        }
        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (onGestureListener != null) {
                onGestureListener.onActionUp();
            }
        }
        return true;
    }

    /**
     * 是否是多点触控
     *
     * @return
     */
    public boolean isMultiTouch() {
        return isMultiTouch;
    }

    public void setOnGestureListener(OnGestureListener onGestureListener) {
        this.onGestureListener = onGestureListener;
    }

    /**
     * 手势监听回调
     */
    public interface OnGestureListener {

        /**
         * 在多指和单指事件改变触发
         *
         * @param isMultiTouch 是否是多指操作
         * @param touchCount   手指数
         */
        void onMultiTouchChange(boolean isMultiTouch, int touchCount);

        /**
         * 缩放开始
         */
        void onScaleStart();

        /**
         * 缩放结束
         */
        void onScaleEnd();

        /**
         * 缩放事件 多指操作
         *
         * @param scaleFraction 缩放因子，此为累加因子
         * @param focusX        缩放中心X
         * @param focusY        缩放中心Y
         */
        void onScale(float scaleFraction, float focusX, float focusY);

        /**
         * 滑动触发 单指操作
         *
         * @param distanceX X轴位移
         * @param distanceY Y轴位移
         */
        void onScroll(float distanceX, float distanceY);

        /**
         * OverScroll触发 单指操作
         *
         * @param velocityX X轴速度
         * @param velocityY Y轴速度
         */
        void onFling(float velocityX, float velocityY);

        /**
         * 每次单击down事件 区别于onSingleTap
         */
        void onActionDown();

        /**
         * 检测到单击事件，单击与双击互斥
         */
        void onSingleTap(MotionEvent event);

        /**
         * 检测到双击事件
         */
        void onDoubleTap(MotionEvent event);

        /**
         * 长按触发
         */
        void onLongPress();

        /**
         * 每次主手指抬起动作
         */
        void onActionUp();
    }
}
