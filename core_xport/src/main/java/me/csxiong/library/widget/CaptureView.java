package me.csxiong.library.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import me.csxiong.library.utils.XDisplayUtil;

/**
 * @Desc : 一个采集的手势View
 * @Author : csxiong - 2019-11-13
 */
public class CaptureView extends View {

    LinearGradient backGradient;

    //画笔
    private Paint mBackgroundPaint;

    //刻度画笔
    private Paint mScalePaint;

    //中心刻度笔
    private Paint mCenterPaint;

    //背景Path
    private Path backgroundPath = new Path();

    //扩张的半径
    private float expandOutRadius;

    //收缩的半径
    private float shrinkOutRadius;

    //扩张的内圈半径
    private float expandInRadius;

    //收缩的内圈半径
    private float shrinkInRadius;

    private float outRadius;

    private float inRadius;

    private boolean isPress;

    private float differRadius;
    private float startRadius;

    //手势切换动画
    private ValueAnimator pressAnimator = ValueAnimator.ofFloat(0, 1)
            .setDuration(200);

    //动画更新监听
    private ValueAnimator.AnimatorUpdateListener updateListener = animation -> {
        float fract = animation.getAnimatedFraction();
        //当前进度 0~1
        outRadius = differRadius * fract + startRadius;
        invalidate();
    };

    //动画时机监听
    private AnimatorListenerAdapter listenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationCancel(Animator animation) {
            super.onAnimationCancel(animation);
            startRadius = 0;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            startRadius = 0;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            startRadius = outRadius;
            if (isPress) {
                differRadius = expandOutRadius - outRadius;
            } else {
                differRadius = shrinkOutRadius - outRadius;
            }
        }
    };

    public CaptureView(Context context) {
        this(context, null);
    }

    public CaptureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CaptureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScalePaint.setStrokeWidth(XDisplayUtil.dpToPxInt(1));
        mScalePaint.setColor(0xffffffff);
        mScalePaint.setTextAlign(Paint.Align.CENTER);
        mScalePaint.setTextSize(35);

        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterPaint.setColor(0xffffffff);
        mCenterPaint.setTextAlign(Paint.Align.CENTER);
        mCenterPaint.setTextSize(35);

        pressAnimator.addUpdateListener(updateListener);
        pressAnimator.addListener(listenerAdapter);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (backGradient == null) {
            backGradient = new LinearGradient(width, height, 0, 0, new int[]{0xFFFE537F, 0xFFFF48B1},
                    null,
                    Shader.TileMode.CLAMP);
            mBackgroundPaint.setShader(backGradient);
        }
        //绘制底部圆盘
        backgroundPath.reset();
        backgroundPath.addCircle(width / 2, height / 2, outRadius, Path.Direction.CCW);
        backgroundPath.addCircle(width / 2, height / 2, inRadius, Path.Direction.CW);
        canvas.drawPath(backgroundPath, mBackgroundPaint);

        //绘制刻度
        canvas.translate(width / 2, height / 2);
        for (int i = 0; i <= 100; i++) {
            canvas.save();
            canvas.rotate(90 - i * 1.8f);
            int point = i % 25;
            if (i == 0 || i == 100 || point == 0) {
                mScalePaint.setAlpha(255);
                canvas.drawLine(0, -outRadius + 50, 0, -outRadius + 5, mScalePaint);
            } else {
                mScalePaint.setAlpha(100);
                canvas.drawLine(0, -outRadius + 40, 0, -outRadius + 5, mScalePaint);
            }

            if (i == 0 || i == 50 || i == 100) {
                canvas.drawText(i + "", 0, -outRadius + 100, mScalePaint);
            }
            canvas.restore();
        }

        //绘制标尺刻度
        canvas.drawRoundRect(-4, -outRadius + 20, 4, -outRadius - 20, 20, 20, mCenterPaint);
        canvas.drawText("Index", 0, -outRadius + 50, mCenterPaint);

        //绘制中间透明图标
        canvas.drawCircle(0, 0, XDisplayUtil.dpToPx(50), mScalePaint);
    }


    private long time;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            time = System.currentTimeMillis();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            long _time = System.currentTimeMillis();
            if (!isPress && _time - time > 200) {
                isPress = true;
                pressAnimator.cancel();
                pressAnimator.start();
                time = 0;
            }
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
            time = 0;
            isPress = false;
            pressAnimator.cancel();
            pressAnimator.start();
        }
        return super.onTouchEvent(event);
    }

    private int width;

    private int height;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = right - left;
        height = bottom - top;
        expandOutRadius = width / 2.5f;
        shrinkOutRadius = width / 3;
        outRadius = shrinkOutRadius;
        inRadius = 0;
    }
}
