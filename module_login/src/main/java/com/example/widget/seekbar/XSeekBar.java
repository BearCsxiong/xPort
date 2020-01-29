package com.example.widget.seekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.login.R;

import me.csxiong.library.utils.XAnimator;
import me.csxiong.library.utils.XDisplayUtil;

/**
 * @Desc : 一个内容可编辑的Seekbar 比较简易
 * 添加自动滚动和中心可移动模式
 * @Author : csxiong - 2020-01-14
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
     * 进度百分比 0~1之间的浮点数
     */
    private float progressPercent = 0.5f;
    /**
     * 进度浮点 为了UI渲染在progress范围小时 UI显得卡顿
     */
    private float progress = 0f;
    /**
     * 整型进度 回调进度
     */
    private int intProgress = 0;
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
     * 动画
     */
    private XAnimator animator = XAnimator.ofFloat(0, 1)
            .duration(300)
            .setAnimationListener(new XAnimator.XAnimationListener() {
                @Override
                public void onAnimationUpdate(float fraction, float value) {
                    setProgress((int) (startProgress + fraction * diffProgress), false);
                }

                @Override
                public void onAnimationStart(XAnimator animation) {
                    startProgress = progress;
                    diffProgress = forwardProgress - progress;
                }

                @Override
                public void onAnimationEnd(XAnimator animation) {
                    if (onProgressChangeListener != null) {
                        onProgressChangeListener.onProgressChange(intProgress, getLimitLeft() + barWidth * progressPercent, false);
                    }
                }

                @Override
                public void onAnimationCancel(XAnimator animation) {
                    if (onProgressChangeListener != null) {
                        onProgressChangeListener.onProgressChange(intProgress, getLimitLeft() + barWidth * progressPercent, false);
                    }
                }
            });
    /**
     * 动画其实进度
     */
    private float startProgress;
    /**
     * 动画差异进度
     */
    private float diffProgress;
    /**
     * 期望进度
     */
    private float forwardProgress;

    public XSeekBar(Context context) {
        this(context, null);
    }

    public XSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (context != null && attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.XSeekBar);
            isEnableStroke = ta.getBoolean(R.styleable.XSeekBar_isEnableStroke, true);
            isEnableCenterPoint = ta.getBoolean(R.styleable.XSeekBar_isEnableCenterPoint, true);
            backgroundColor = ta.getColor(R.styleable.XSeekBar_xBackgroundColor, 0x80F2F2F2);
            strokeColor = ta.getColor(R.styleable.XSeekBar_xStrokeColor, 0x33000000);
            progressColor = ta.getColor(R.styleable.XSeekBar_xProgressColor, 0xffffffff);
            mThumbRadius = ta.getDimensionPixelSize(R.styleable.XSeekBar_xThumbRadius, XDisplayUtil.dpToPxInt(9.5f));
            mCenterPointWidth = ta.getDimensionPixelSize(R.styleable.XSeekBar_xCenterPointWidth, XDisplayUtil.dpToPxInt(3));
            mCenterPointHeight = ta.getDimensionPixelSize(R.styleable.XSeekBar_xCenterPointHeight, XDisplayUtil.dpToPxInt(7));
            mSeekBarHeight = ta.getDimensionPixelSize(R.styleable.XSeekBar_xSeekbarHeight, XDisplayUtil.dpToPxInt(3));
            centerPointPercent = ta.getFloat(R.styleable.XSeekBar_xCenterPointPercent, .5f);
            maxProgress = ta.getInteger(R.styleable.XSeekBar_xMaxProgress, 100);
            minProgress = ta.getInteger(R.styleable.XSeekBar_xMinProgress, -100);
            progress = ta.getInteger(R.styleable.XSeekBar_xProgress, 0);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        initAttrs(context, attrs);
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(backgroundColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setColor(strokeColor);
        mStrokePaint.setStrokeWidth(XDisplayUtil.dpToPxInt(.5f));
        mStrokePaint.setStyle(Paint.Style.STROKE);

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setColor(progressColor);
        mProgressPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        canvas.drawRoundRect(mBackgroundRectf, 50, 50, mBackgroundPaint);
        //绘制进度
        canvas.drawRoundRect(mProgressRectf, 50, 50, mProgressPaint);
        //绘制描边
        if (isEnableStroke) {
            canvas.drawRoundRect(mBackgroundRectf, 50, 50, mStrokePaint);
        }
        //绘制中心点
        if (isEnableCenterPoint) {
            canvas.drawRoundRect(mCenterPointRectf, 50, 50, mProgressPaint);
            if (isEnableStroke) {
                //绘制中心描边
                canvas.drawRoundRect(mCenterPointRectf, 50, 50, mStrokePaint);
            }
        }
        //绘制手指拖动的thumb
        canvas.drawCircle(getLimitLeft() + barWidth * progressPercent, height / 2f, mThumbRadius, mProgressPaint);
        if (isEnableStroke) {
            //描边
            canvas.drawCircle(getLimitLeft() + barWidth * progressPercent, height / 2f, mThumbRadius, mStrokePaint);
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
            mCenterPointRectf = new RectF(getLimitLeft() + barWidth * centerPointPercent - mCenterPointWidth / 2f, height / 2f - mCenterPointHeight / 2f, getLimitLeft() + barWidth * centerPointPercent + mCenterPointWidth / 2f, height / 2f + mCenterPointHeight / 2f);
        } else {
            //设置中心的Rectf
            mCenterPointRectf.set(getLimitLeft() + barWidth * centerPointPercent - mCenterPointWidth / 2f, height / 2f - mCenterPointHeight / 2f, getLimitLeft() + barWidth * centerPointPercent + mCenterPointWidth / 2f, height / 2f + mCenterPointHeight / 2f);
        }
        invalidate();
    }

    /**
     * 设置进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        setProgress(progress, false);
    }

    /**
     * 设置进度
     *
     * @param progress
     * @param withAnimation
     */
    public void setProgress(int progress, boolean withAnimation) {
        boolean isChange = progress != intProgress;
        setProgress(progress, false, withAnimation);
        //手动设置也是需要回调的
        //若执行动画 会在动画执行之后 回调结果
        if (isChange && !withAnimation && onProgressChangeListener != null) {
            onProgressChangeListener.onProgressChange(intProgress, getLimitLeft() + barWidth * progressPercent, false);
        }
    }

    /**
     * 设置进度
     *
     * @param progress      期望进度
     * @param isReset       是否重置设置
     * @param withAnimation 是否执行动画
     */
    private void setProgress(float progress, boolean isReset, boolean withAnimation) {
        if (isReset || (progress >= minProgress && progress <= maxProgress)) {
            if (withAnimation) {
                forwardProgress = progress;
                animator.cancel();
                animator.start();
            } else {
                this.progress = progress;
                this.intProgress = (int) progress;
                progressPercent = progress / (float) (maxProgress - minProgress) + centerPointPercent;
                float left = progressPercent > centerPointPercent ? getLimitLeft() + centerPointPercent * barWidth : barWidth * progressPercent + getLimitLeft();
                float right = progressPercent > centerPointPercent ? barWidth * progressPercent + getLimitLeft() : getLimitLeft() + centerPointPercent * barWidth;
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
        float newProgress = (progressPercent - centerPointPercent) * (maxProgress - minProgress);
        int intNewProgress = (int) newProgress;
        if (action == MotionEvent.ACTION_DOWN) {
            isTouch = true;
            setProgress(newProgress, true, false);
            if (onProgressChangeListener != null) {
                onProgressChangeListener.onStartTracking(intProgress, getLimitLeft() + barWidth * progressPercent);
            }
            return true;
        } else if (isTouch && action == MotionEvent.ACTION_MOVE) {
            setProgress(newProgress, false, false);
            if (intNewProgress != intProgress) {
                if (onProgressChangeListener != null) {
                    onProgressChangeListener.onProgressChange(intProgress, getLimitLeft() + barWidth * progressPercent, false);
                }
            }
            if (onProgressChangeListener != null) {
                onProgressChangeListener.onPositionChange(intProgress, getLimitLeft() + barWidth * progressPercent);
            }
        } else if (isTouch && action == MotionEvent.ACTION_UP) {
            setProgress(newProgress, true, false);
            isTouch = false;
            if (onProgressChangeListener != null) {
                onProgressChangeListener.onStopTracking(intProgress, getLimitLeft() + barWidth * progressPercent, true);
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 获取接触百分比
     * 以左向右作为基准
     * 均使用百分比触控计算所有值  因为这样有利控制原点和其他参数的关系
     *
     * @param event
     * @return
     */
    private float getTouchPercent(MotionEvent event) {
        float x = event.getX() - getLimitLeft();
        return x / barWidth;
    }

    /**
     * 适配ViewPadding
     *
     * @return
     */
    private int getLimitLeft() {
        return getPaddingLeft() + mThumbRadius;
    }

    /**
     * 适配ViewPadding
     *
     * @return
     */
    private int getLimitRight() {
        return getPaddingRight() + mThumbRadius;
    }

    /**
     * 初始化size
     */
    private void initSize(int width, int height) {
        this.width = width;
        this.height = height;
        barWidth = width - getLimitLeft() - getLimitRight();

        //初始化基础图形结构
        mBackgroundRectf = new RectF(getLimitLeft(), height / 2f - mSeekBarHeight / 2f, width - getLimitRight(), height / 2f + mSeekBarHeight / 2f);
        //进度基础Rectf
        setProgress(progress, true, false);
        setCenterPointPercent(centerPointPercent);
    }

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
        void onStartTracking(int progress, float leftDx);

        /**
         * 进度改变
         *
         * @param progress
         * @param fromUser
         */
        void onProgressChange(int progress, float leftDx, boolean fromUser);

        /**
         * 位置改变监听
         *
         * @param leftDx
         */
        void onPositionChange(int progress, float leftDx);

        /**
         * 停止拖动
         *
         * @param progress
         */
        void onStopTracking(int progress, float leftDx, boolean fromUser);
    }

}
