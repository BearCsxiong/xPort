package me.csxiong.library.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import me.csxiong.library.utils.XDisplayUtil;

/**
 * @Desc : 一个采集的手势View
 * @Author : csxiong - 2019-11-13
 */
public class CaptureView extends View {

    /**
     * 刻度文字透明度
     */
    private final static int SCALE_TEXT_ALPHA = 255;
    /**
     * 刻度透明度
     */
    private final static int SCALE_ALPHA = 80;
    /**
     * 最大角度
     */
    private final static int MAX_DEGREE = 180;
    /**
     * 最小角度
     */
    private final static int MIN_DEGREE = 0;
    /**
     * 过滑阻尼
     */
    private final static int OVERSCROLL_DAMPING_ALPHA = 4;
    /**
     * 手势阻尼
     */
    private final static float GESTURE_DAMPING_ALPHA = 2f;
    /**
     * 手势触发时间
     */
    private final static long HAND_TOUCH_TIME = 80;

    //渐变色
    LinearGradient backGradient;

    //画笔
    private Paint mBackgroundPaint;

    //刻度画笔
    private Paint mScalePaint;

    //中心刻度笔
    private Paint mCenterPaint;

    //手势盘画笔
    private Paint mGesturePaint;

    //扩张的半径
    private float expandOutRadius;

    //收缩的半径
    private float shrinkOutRadius;

    //扩张的内圈半径
    private float expandInRadius;

    //收缩的内圈半径
    private float shrinkInRadius;

    //扩张的内部透明度变化
    private int expandAlpha = 255;

    //收缩的内部透明度
    private int shrinkAlpha = 50;

    //外部半径
    private float outRadius;

    //内部手势的半径
    private float inRadius;

    //内部手势的透明读
    private int alpha;

    //程度值
    private float degree;

    /**
     * View可见宽度
     */
    private int width;

    /**
     * View可见高度
     */
    private int height;

    /**
     * 中心点 目前为View的中心点
     */
    private Point center = new Point();

    /**
     * 手势起始点 主要手势使用
     */
    private Point start = new Point();

    /**
     * 手势重点 主要手势使用
     */
    private Point end = new Point();

    /**
     * FIXME 是否是按压状态
     */
    private boolean isPress;

    /**
     * 外圈动效变量
     */
    private float differOutRadius;
    private float startOutRadius;

    /**
     * 内圈动效变量
     */
    private float differInRadius;
    private float startInRadius;

    /**
     * 内圈透明度变量
     */
    private int startAlpha;
    private int differAlpha;

    /**
     * 过滑角度变量
     */
    private float startDegree;
    private float differDegree;

    /**
     * 上一次的进度
     */
    private int lastProgress;

    /**
     * 进度改变监听
     */
    private OnProgressChangeListener onProgressChangeListener;

    /**
     * 改变动画 都是从0～1变化 内部因子使用 自己控制差值
     */
    private ValueAnimator changeAnimator = ValueAnimator.ofFloat(0, 1)
            .setDuration(200);

    /**
     * 动画更新回调
     */
    private ValueAnimator.AnimatorUpdateListener updateListener = animation -> {
        float fract = animation.getAnimatedFraction();
        //当前进度 0~1
        outRadius = differOutRadius * fract + startOutRadius;
        inRadius = differInRadius * fract + startInRadius;
        alpha = (int) (fract * differAlpha + startAlpha);
        if (!isPress) {
            degree = fract * differDegree + startDegree;
        }
        invalidate();
    };

    /**
     * 参数计算时机
     */
    private AnimatorListenerAdapter listenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationCancel(Animator animation) {
            super.onAnimationCancel(animation);
            differOutRadius = 0;
            differInRadius = 0;
            differAlpha = 0;
            differDegree = 0;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            differOutRadius = 0;
            differInRadius = 0;
            differAlpha = 0;
            differDegree = 0;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            startOutRadius = outRadius;
            startInRadius = inRadius;
            startAlpha = alpha;
            startDegree = degree;
            if (isPress) {
                differAlpha = expandAlpha - startAlpha;
                differOutRadius = expandOutRadius - outRadius;
                differInRadius = expandInRadius - inRadius;
            } else {
                differAlpha = shrinkAlpha - startAlpha;
                differOutRadius = shrinkOutRadius - outRadius;
                differInRadius = shrinkInRadius - inRadius;

                if (degree > MAX_DEGREE) {
                    differDegree = MAX_DEGREE - degree;
                } else if (degree < MIN_DEGREE) {
                    differDegree = MIN_DEGREE - degree;
                } else {
                    differDegree = 0;
                }
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

    /**
     * 初始化
     */
    private void init() {
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mScalePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mScalePaint.setStrokeWidth(XDisplayUtil.dpToPxInt(1));
        mScalePaint.setColor(0xffffffff);
        mScalePaint.setTextAlign(Paint.Align.CENTER);
        mScalePaint.setTextSize(30);

        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterPaint.setColor(0xffffffff);
        mCenterPaint.setTextAlign(Paint.Align.CENTER);
        mCenterPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mCenterPaint.setTextSize(45);

        mGesturePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mGesturePaint.setColor(0xffffffff);

        changeAnimator.addUpdateListener(updateListener);
        changeAnimator.addListener(listenerAdapter);
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
        canvas.translate(width / 2, height / 2);
        //绘制底部圆盘
        canvas.drawCircle(0, 0, outRadius, mBackgroundPaint);
        //绘制中间透明图标
        mGesturePaint.setAlpha(alpha);
        canvas.drawCircle(0, 0, inRadius, mGesturePaint);

        canvas.save();
        //绘制刻度
        for (int i = 0; i <= 100; i++) {
            canvas.save();
            canvas.rotate(degree - i * 1.8f);
            int point = i % 25;
            if (i == 0 || i == 100 || point == 0) {
                mScalePaint.setAlpha(SCALE_TEXT_ALPHA);
                canvas.drawLine(0, -outRadius + 50, 0, -outRadius + 5, mScalePaint);
            } else {
                mScalePaint.setAlpha(SCALE_ALPHA);
                canvas.drawLine(0, -outRadius + 40, 0, -outRadius + 5, mScalePaint);
            }

            if (i == 0 || i == 50 || i == 100) {
                mScalePaint.setAlpha(SCALE_ALPHA);
                canvas.drawText(i + "", 0, -outRadius + 100, mScalePaint);
            }
            canvas.restore();
        }

        //绘制标尺刻度
        canvas.drawRoundRect(-4, -outRadius + 20, 4, -outRadius - 20, 20, 20, mCenterPaint);
        //绘制文字
        float tempDegree = degree;
        if (tempDegree > MAX_DEGREE) {
            tempDegree = MAX_DEGREE;
        } else if (tempDegree < MIN_DEGREE) {
            tempDegree = MIN_DEGREE;
        }

        int progress = (int) ((tempDegree / 180) * 100);
        if (progress != lastProgress && onProgressChangeListener != null) {
            onProgressChangeListener.onProgressChange(lastProgress, progress);
        }
        lastProgress = progress;
        canvas.drawText(String.valueOf(progress), 0, -outRadius + 80, mCenterPaint);
    }

    /**
     * 设置程度值 TODO 可修改 根据需要修改
     *
     * @param ratio
     */
    public void setDegree(@FloatRange(from = 0f, to = 1f) float ratio) {
        degree = MAX_DEGREE * ratio;
        invalidate();
    }

    /**
     * 记录手势press startTime
     */
    private long time;

    /**
     * 起始手势角度
     */
    private int lastX;
    private int lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            lastX = (int) event.getX();
            lastY = (int) event.getY();
            time = System.currentTimeMillis();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //FIXME 这边目前是200毫秒开始需求点改动
            long _time = System.currentTimeMillis();
            if (!isPress && _time - time > HAND_TOUCH_TIME) {
                isPress = true;
                changeAnimator.cancel();
                changeAnimator.start();
                time = 0;
            }
            start.set(lastX, lastY);
            lastX = (int) event.getX();
            lastY = (int) event.getY();
            end.set(lastX, lastY);
            //计算角度 主要是向量计算
            float angle = angle(center, start, end);
            //手势阻尼 不希望手势过快滑动
            angle /= GESTURE_DAMPING_ALPHA;
            //过滑阻尼 不希望越界滑动
            if (degree > MAX_DEGREE && angle > 0) {
                angle /= OVERSCROLL_DAMPING_ALPHA;
            } else if (degree < MIN_DEGREE && angle < 0) {
                angle /= OVERSCROLL_DAMPING_ALPHA;
            }
            degree += angle;
            Log.e("degree", degree + "");
            invalidate();
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
            time = 0;
            isPress = false;
            changeAnimator.cancel();
            changeAnimator.start();
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        onInitSize(w, h);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        onInitSize(right - left, bottom - top);
    }

    private void onInitSize(int width, int heigt) {
        this.width = width;
        this.height = heigt;
        expandOutRadius = width / 2.5f;
        shrinkOutRadius = width / 3;

        expandInRadius = width / 4;
        shrinkInRadius = width / 4.5f;

        outRadius = shrinkOutRadius;
        inRadius = shrinkInRadius;
        alpha = shrinkAlpha;

        center.set(width / 2, height / 2);
    }

    /**
     * 计算前后角度 需要判断顺时针和逆时针问题
     *
     * @param cen
     * @param first
     * @param second
     * @return
     */
    public float angle(Point cen, Point first, Point second) {
        float dx1, dx2, dy1, dy2;

        dx1 = first.x - cen.x;
        dy1 = first.y - cen.y;
        dx2 = second.x - cen.x;
        dy2 = second.y - cen.y;

        // 计算三边的平方
        float ab2 = (second.x - first.x) * (second.x - first.x) + (second.y - first.y) * (second.y - first.y);
        float oa2 = dx1 * dx1 + dy1 * dy1;
        float ob2 = dx2 * dx2 + dy2 * dy2;

        // 根据两向量的叉乘来判断顺逆时针
        boolean isClockwise = ((first.x - cen.x) * (second.y - cen.y) - (first.y - cen.y) * (second.x - cen.x)) > 0;

        // 根据余弦定理计算旋转角的余弦值
        double cosDegree = (oa2 + ob2 - ab2) / (2 * Math.sqrt(oa2) * Math.sqrt(ob2));

        // 异常处理，因为算出来会有误差绝对值可能会超过一，所以需要处理一下
        if (cosDegree > 1) {
            cosDegree = 1;
        } else if (cosDegree < -1) {
            cosDegree = -1;
        }

        // 计算弧度
        double radian = Math.acos(cosDegree);

        // 计算旋转过的角度，顺时针为正，逆时针为负
        return (float) (isClockwise ? Math.toDegrees(radian) : -Math.toDegrees(radian));
    }

    /**
     * 设置进度改变监听
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

        void onProgressChange(int lastProgress, int progress);

    }
}
