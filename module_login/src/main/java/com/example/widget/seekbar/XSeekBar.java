package com.example.widget.seekbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import me.csxiong.library.utils.XDisplayUtil;

/**
 * @Desc : Seekbar
 * @Author : csxiong - 2020-01-13
 */
public class XSeekBar extends View {
    /**
     * 是否允许描边
     */
    private boolean isEnableStroke = true;
    /**
     * 是否需要中心点
     */
    private boolean isEnableCenterPoint = true;
    /**
     * 背景颜色
     */
    private int backgroundColor = 0x80F2F2F2;
    /**
     * 描边颜色
     */
    private int strokeColor = 0x33000000;
    /**
     * 进度颜色
     */
    private int progressColor = 0xffffffff;
    /**
     * 图钉半径
     */
    private int mThumbRadius = XDisplayUtil.dpToPxInt(9.5f);
    /**
     * 中心点宽度
     */
    private int mCenterPointWidth = XDisplayUtil.dpToPxInt(3);
    /**
     * 中心点point高度
     */
    private int mCenterPointHeight = XDisplayUtil.dpToPxInt(7);
    /**
     * bar高度
     */
    private int mSeekBarHeight = XDisplayUtil.dpToPxInt(3);
    /**
     * 进度百分比
     */
    private float progressPercent = 0.5f;
    /**
     * 进度
     */
    private int progress = 0;
    /**
     * 中心点位于滑杆位置
     */
    private float centerPointPercent = 0.5f;
    /**
     * 最小进度
     */
    private int minProgress = -100;
    /**
     * 最大进度
     */
    private int maxProgress = 100;
    /**
     * 宽度
     */
    private int width;
    /**
     * 高度
     */
    private int height;
    /**
     * bar宽度
     */
    private int barWidth;
    /**
     * 背景矩形
     */
    private RectF mBackgroundRectf;
    /**
     * 进度矩形
     */
    private RectF mProgressRectf;
    /**
     * 中心点矩形
     */
    private RectF mCenterPointRectf;
    /**
     * 普通颜色笔
     */
    private Paint mProgressPaint;
    /**
     * 背景笔
     */
    private Paint mBackgroundPaint;
    /**
     * 描边笔
     */
    private Paint mStrokePaint;
    /**
     * 进度回调
     */
    private ValueAnimator animator = ValueAnimator.ofFloat(0, 1)
            .setDuration(350);
    /**
     * 是否开始
     */
    private boolean isStart;

    private int startProgress;

    private int diffProgress;

    private int forwardProgress;

    private ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (isStart) {
                float fraction = animation.getAnimatedFraction();
                setProgress((int) (startProgress + fraction * diffProgress), false);
            }
        }
    };

    private ValueAnimator.AnimatorListener animatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationCancel(Animator animation) {
            super.onAnimationCancel(animation);
            isStart = false;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            isStart = false;
            if (onProgressChangeListener != null) {
                onProgressChangeListener.onProgressChange(progress, false);
            }
        }

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            isStart = true;
            startProgress = progress;
            diffProgress = forwardProgress - progress;
        }
    };

    public XSeekBar(Context context) {
        super(context);
        init();
    }

    public XSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public XSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(backgroundColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setColor(strokeColor);
//        mStrokePaint.setStrokeWidth(XDisplayUtil.dpToPxInt(1));
        mStrokePaint.setStyle(Paint.Style.STROKE);

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setColor(progressColor);
        mProgressPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        animator.addListener(animatorListener);
        animator.addUpdateListener(updateListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        canvas.drawRoundRect(mBackgroundRectf, 50, 50, mBackgroundPaint);
        //绘制进度
        canvas.drawRoundRect(mProgressRectf, 50, 50, mProgressPaint);
        if (isEnableStroke) {
            //绘制描边
            canvas.drawRoundRect(mBackgroundRectf, 50, 50, mStrokePaint);
        }
        //绘制中心点
        canvas.drawRoundRect(mCenterPointRectf, 50, 50, mProgressPaint);
        if (isEnableStroke) {
            //绘制中心描边
            canvas.drawRoundRect(mCenterPointRectf, 50, 50, mStrokePaint);
        }
        //绘制手指拖动的thumb
        canvas.drawCircle(mThumbRadius + barWidth * progressPercent, height / 2f, mThumbRadius, mProgressPaint);
        if (isEnableStroke) {
            //描边
            canvas.drawCircle(mThumbRadius + barWidth * progressPercent, height / 2f, mThumbRadius, mStrokePaint);
        }

    }

    /**
     * 设置中心比例
     *
     * @param centerPointPercent
     */
    public void setCenterPointPercent(@FloatRange(from = 0.0f, to = 1.0f) float centerPointPercent) {
        this.centerPointPercent = centerPointPercent;
        if (mCenterPointRectf == null) {
            //中心点Rectf
            mCenterPointRectf = new RectF(mThumbRadius + (width - mThumbRadius * 2) * centerPointPercent - mCenterPointWidth / 2f, height / 2f - mCenterPointHeight / 2f, mThumbRadius + (width - mThumbRadius * 2) * centerPointPercent + mCenterPointWidth / 2f, height / 2f + mCenterPointHeight / 2f);
        } else {
            //设置中心的Rectf
            mCenterPointRectf.set(mThumbRadius + (width - mThumbRadius * 2) * centerPointPercent - mCenterPointWidth / 2f, height / 2f - mCenterPointHeight / 2f, mThumbRadius + (width - mThumbRadius * 2) * centerPointPercent + mCenterPointWidth / 2f, height / 2f + mCenterPointHeight / 2f);
        }
        invalidate();
    }

    public void setProgress(int progress) {
        setProgress(progress, false);
    }

    public void setProgress(int progress, boolean withAnimation) {
        setProgress(progress, false, withAnimation);
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    private void setProgress(int progress, boolean isReset, boolean withAnimation) {
        if (isReset || (progress != this.progress && progress >= minProgress && progress <= maxProgress)) {
            if (withAnimation) {
                forwardProgress = progress;
                animator.cancel();
                animator.start();
            } else {
                this.progress = progress;
                progressPercent = progress / (float) (maxProgress - minProgress) + centerPointPercent;
                float left = progressPercent > centerPointPercent ? mThumbRadius + centerPointPercent * (width - mThumbRadius * 2) : (width - mThumbRadius * 2) * progressPercent + mThumbRadius;
                float right = progressPercent > centerPointPercent ? (width - mThumbRadius * 2) * progressPercent + mThumbRadius : mThumbRadius + centerPointPercent * (width - mThumbRadius * 2);
                if (mProgressRectf == null) {
                    mProgressRectf = new RectF(left, height / 2f - mSeekBarHeight / 2f, right, height / 2f + mSeekBarHeight / 2f);
                } else {
                    mProgressRectf.set(left, height / 2f - mSeekBarHeight / 2f, right, height / 2f + mSeekBarHeight / 2f);
                }
                invalidate();
            }
        }
    }

    /**
     * 设置背景颜色
     *
     * @param backgroundColor
     */
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        this.backgroundColor = backgroundColor;
        if (mBackgroundPaint != null) {
            mBackgroundPaint.setColor(backgroundColor);
        }
    }

    /**
     * 设置进度颜色
     *
     * @param progressColor
     */
    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        if (mProgressPaint != null) {
            mProgressPaint.setColor(progressColor);
        }
    }

    /**
     * 设置描边颜色
     *
     * @param strokeColor
     */
    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        if (mStrokePaint != null) {
            mStrokePaint.setColor(strokeColor);
        }
    }

    /**
     * 是否绘制中心点
     *
     * @param enableCenterPoint
     */
    public void setEnableCenterPoint(boolean enableCenterPoint) {
        isEnableCenterPoint = enableCenterPoint;
        invalidate();
    }

    /**
     * 设置是否描边
     *
     * @param enableStroke
     */
    public void setEnableStroke(boolean enableStroke) {
        isEnableStroke = enableStroke;
        invalidate();
    }

    /**
     * 设置最小进度
     *
     * @param minProgress
     */
    public void setMinProgress(int minProgress) {
        this.minProgress = minProgress;
        invalidate();
    }

    /**
     * 设置最大进度
     *
     * @param maxProgress
     */
    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initSize(right - left, bottom - top);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initSize(w, h);
    }

    /**
     * 是否触控
     */
    private boolean isTouch;
    /**
     * 进度改变监听
     */
    private OnProgressChangeListener onProgressChangeListener;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        progressPercent = getTouchPercent(event);
        if (progressPercent < 0) {
            progressPercent = 0;
        } else if (progressPercent > 1) {
            progressPercent = 1;
        }
        //计算新进度
        int newProgress = (int) ((progressPercent - centerPointPercent) * (maxProgress - minProgress));
        if (action == MotionEvent.ACTION_DOWN) {
            isTouch = true;
            setProgress(newProgress, true, false);
            if (onProgressChangeListener != null) {
                onProgressChangeListener.onStartTracking(progress);
            }
            return true;
        } else if (isTouch && action == MotionEvent.ACTION_MOVE) {
            if (newProgress != progress) {
                setProgress(newProgress, false, false);
                if (onProgressChangeListener != null) {
                    onProgressChangeListener.onProgressChange(progress, false);
                }
            }
        } else if (isTouch && action == MotionEvent.ACTION_UP) {
            setProgress(newProgress, true, false);
            isTouch = false;
            if (onProgressChangeListener != null) {
                onProgressChangeListener.onStopTracking(progress, true);
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 获取接触百分比
     *
     * @param event
     * @return
     */
    private float getTouchPercent(MotionEvent event) {
        float x = event.getX() - mThumbRadius;
        return x / barWidth;
    }

    /**
     * 获取手势落点距离进度中心半径
     *
     * @param event
     * @return
     */
    private double getTouch2ProgressRadius(MotionEvent event) {
        float progressX = (int) (barWidth * progressPercent);
        float progressY = height / 2f;

        float x = event.getX();
        float y = event.getY();
        return Math.sqrt(Math.pow(x - progressX, 2) + Math.pow(y - progressY, 2));
    }

    /**
     * 初始化size
     */
    private void initSize(int width, int height) {
        this.width = width;
        this.height = height;
        barWidth = width - mThumbRadius * 2;

        //初始化基础图形结构
        mBackgroundRectf = new RectF(mThumbRadius, height / 2f - mSeekBarHeight / 2f, width - mThumbRadius, height / 2f + mSeekBarHeight / 2f);
        //进度基础Rectf
        setProgress(progress, true, false);
        setCenterPointPercent(centerPointPercent);
    }

    /**
     * 设置数据变更监听
     *
     * @param onProgressChangeListener
     */
    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }

    /**
     * 进度改变监听
     */
    public interface OnProgressChangeListener {

        /**
         * 开始拖动
         *
         * @param progress
         */
        void onStartTracking(int progress);

        /**
         * 进度改变
         *
         * @param progress
         * @param fromUser
         */
        void onProgressChange(int progress, boolean fromUser);

        /**
         * 停止拖动
         *
         * @param progress
         */
        void onStopTracking(int progress, boolean fromUser);
    }
}
