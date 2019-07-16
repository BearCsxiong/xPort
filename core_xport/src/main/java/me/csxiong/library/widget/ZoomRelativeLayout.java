package me.csxiong.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

/**
 * @Desc : 动感的relativeLayout
 * @Author : csxiong create on 2019/7/17
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

    public ZoomRelativeLayout(Context context) {
        this(context, null);
    }

    public ZoomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        SCROLL_THRESHOLD = ViewConfiguration.get(context).getScaledTouchSlop();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init() {
        zoomInAnimation = new ScaleAnimation(1f, 0.8f, 1f, 0.8f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        zoomInAnimation.setFillAfter(true);
        zoomInAnimation.setDuration(150);
        zoomOutAnimation = new ScaleAnimation(0.8f, 1f, 0.8f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
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
                    if (mOnClickListener != null)
                        mOnClickListener.onClick(ZoomRelativeLayout.this);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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
                startAnimation(zoomInAnimation);//执行动画
                break;
            case MotionEvent.ACTION_UP:
                isPressed = false;
                if (State == NORAML && isOnClick) {
                    if (mOnClickListener != null)
                        mOnClickListener.onClick(ZoomRelativeLayout.this);
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
