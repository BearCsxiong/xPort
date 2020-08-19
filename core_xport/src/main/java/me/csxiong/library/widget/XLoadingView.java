package me.csxiong.library.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

import me.csxiong.library.R;
import me.csxiong.library.utils.XAnimator;
import me.csxiong.library.utils.XDisplayUtil;


/**
 * @Desc : 可定制的loading框
 * @Author : csxiong create on 2019/7/17
 */
public class XLoadingView extends View {

    private int mSize = XDisplayUtil.dpToPxInt(32);
    /**
     * 画笔颜色
     */
    private int mPaintColor = Color.WHITE;
    /**
     * 当前线条数
     */
    private int curLineCount = 0;

    private XAnimator xAnimator = XAnimator.ofFloat(0, LINE_COUNT - 1)
            .restartMode()
            .repeatCount(ValueAnimator.INFINITE)
            .duration(500)
            .interpolator(new LinearInterpolator())
            .setAnimationListener(new XAnimator.XAnimationListener() {
                @Override
                public void onAnimationUpdate(float fraction, float value) {
                    Log.e("csx", "onAnimationUpdate");
                    curLineCount = (int) value;
                    invalidate();
                }

                @Override
                public void onAnimationStart(XAnimator animation) {

                }

                @Override
                public void onAnimationEnd(XAnimator animation) {

                }

                @Override
                public void onAnimationCancel(XAnimator animation) {

                }
            });

    private Paint mPaint;
    private static final int LINE_COUNT = 12;
    private static final int DEGREE_PER_LINE = 360 / LINE_COUNT;

    public XLoadingView(Context context) {
        this(context, null);
    }

    public XLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    public XLoadingView(Context context, int size, int color) {
        super(context);
        mSize = size;
        mPaintColor = color;
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(mPaintColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setColor(int color) {
        mPaintColor = color;
        mPaint.setColor(color);
        invalidate();
    }

    public void setSize(int size) {
        mSize = size;
        requestLayout();
    }

    public void start() {
        if (!xAnimator.isRunning()) {
            xAnimator.start();
        }
    }

    public void stop() {
        xAnimator.cancel();
    }

    private void drawLoading(Canvas canvas, int rotateDegrees) {
        int width = mSize / 12, height = mSize / 6;
        mPaint.setStrokeWidth(width);

        canvas.rotate(rotateDegrees, mSize / 2, mSize / 2);
        canvas.translate(mSize / 2, mSize / 2);

        for (int i = 0; i < LINE_COUNT; i++) {
            canvas.rotate(DEGREE_PER_LINE);
            mPaint.setAlpha((int) (255f * (i + 1) / LINE_COUNT));
            canvas.translate(0, -mSize / 2 + width / 2);
            canvas.drawLine(0, 0, 0, height, mPaint);
            canvas.translate(0, mSize / 2 - width / 2);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mSize, mSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        drawLoading(canvas, curLineCount * DEGREE_PER_LINE);
        canvas.restoreToCount(saveCount);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            start();
        } else {
            stop();
        }
    }

}
