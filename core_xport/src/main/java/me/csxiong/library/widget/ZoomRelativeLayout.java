package me.csxiong.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

/**
 * @Desc : 一个缩放的包装控件
 * @Author : Bear - 2020/8/18
 */
public class ZoomRelativeLayout extends RelativeLayout {
    private boolean isPressed;
    private int NORAML = 0;
    private int ZOOMIN = 1;
    private int ZOOMOUT = 2;
    private int State = NORAML;
    private OnClickListener mOnClickListener;
    private ScaleAnimation zoomInAnimation;
    private ScaleAnimation zoomOutAnimation;
    private float SCROLL_THRESHOLD = 0;

    public static float SCALE_FRACTOR = 0.8f;

    public ZoomRelativeLayout(Context context) {
        this(context, null);
    }

    public ZoomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        SCROLL_THRESHOLD = ViewConfiguration.get(context).getScaledTouchSlop();
        init();
    }

    private void init() {
        zoomInAnimation = new ScaleAnimation(1f, SCALE_FRACTOR, 1f, SCALE_FRACTOR, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        zoomInAnimation.setFillAfter(true);
        zoomInAnimation.setDuration(150);
        zoomOutAnimation = new ScaleAnimation(SCALE_FRACTOR, 1f, SCALE_FRACTOR, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        zoomOutAnimation.setFillAfter(true);
        zoomOutAnimation.setDuration(150);
        zoomInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                synchronized (ZoomRelativeLayout.this) {
                    State = ZOOMOUT;
                }
                invalidate();
                startAnimation(zoomOutAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        zoomOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                synchronized (ZoomRelativeLayout.this) {
                    State = NORAML;
                }
                if (!isPressed && isOnClick) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(ZoomRelativeLayout.this);
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mOnClickListener = l;
    }

    private float mDownX;
    private float mDownY;

    private boolean isOnClick;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPressed = true;
                isOnClick = true;
                mDownX = event.getX();
                mDownY = event.getY();
                synchronized (ZoomRelativeLayout.this) {
                    State = ZOOMIN;
                }
                startAnimation(zoomInAnimation);
                break;
            case MotionEvent.ACTION_UP:
                isPressed = false;
                if (State == NORAML && isOnClick) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(ZoomRelativeLayout.this);
                    }
                }
                return false;
            case MotionEvent.ACTION_MOVE:
                if (isOnClick && (Math.abs(mDownX - event.getX()) > SCROLL_THRESHOLD || Math.abs(mDownY - event.getY()) > SCROLL_THRESHOLD)) {
                    isOnClick = false;
                }
                break;
        }
        return true;

    }
}
