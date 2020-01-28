package me.csxiong.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import me.csxiong.library.utils.gesture.XGestureDetector;

/**
 * @Desc : 测试手势View
 * @Author : csxiong - 2020-01-28
 */
public class GestureView extends View {

    private final static String TAG = "GestureView";

    private XGestureDetector gestureDetector;

    private XGestureDetector.OnGestureListener onGestureListener = new XGestureDetector.OnGestureListener() {
        @Override
        public void onMultiTouchChange(boolean isMultiTouch, int touchCount) {
            Log.e(TAG, "onMultiTouchChange");
        }

        @Override
        public void onScale(float scaleFraction, float focusX, float focusY) {
            Log.e(TAG, "onScale");
        }

        @Override
        public void onScroll(float distanceX, float distanceY) {
            Log.e(TAG, "onScroll");
        }

        @Override
        public void onFling(float velocityX, float velocityY) {
            Log.e(TAG, "onFling");
        }

        @Override
        public void onActionDown() {
            Log.e(TAG, "onActionDown");
        }

        @Override
        public void onSingleTap(MotionEvent event) {
            Log.e(TAG, "onSingleTap");
        }

        @Override
        public void onDoubleTap(MotionEvent event) {
            Log.e(TAG, "onDoubleTap");
        }

        @Override
        public void onLongPress() {
            Log.e(TAG, "onLongPress");
        }

        @Override
        public void onActionUp() {
            Log.e(TAG, "onActionUp");
        }
    };

    private void init() {
        gestureDetector = new XGestureDetector(getContext());
        gestureDetector.setOnGestureListener(onGestureListener);
    }

    public GestureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GestureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
}
