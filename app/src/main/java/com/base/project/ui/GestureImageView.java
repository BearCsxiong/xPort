package com.base.project.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * @Desc : 手势控件ImageView
 * @Author : csxiong - 2019/7/17
 */
@SuppressLint("AppCompatCustomView")
public class GestureImageView extends ImageView {

    private ProGestureDetector proGestureDetector;

    private ProGestureDetector.MultiGestureListener multiGestureListener = new ProGestureDetector.SimpleMultiGestureListener() {

        @Override
        public void onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
            super.onScroll(e1, e2, dx, dy);
        }

        @Override
        public boolean onScale(float scaleFactor, float scaleFocusX, float scaleFocusY) {
            return true;
        }

        @Override
        public void onFling(float vx, float vy) {
            super.onFling(vx, vy);
        }
    };

    public GestureImageView(Context context) {
        this(context, null);
    }

    public GestureImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        proGestureDetector = new ProGestureDetector(getContext(), multiGestureListener);
        setOnTouchListener(proGestureDetector);
    }

}
