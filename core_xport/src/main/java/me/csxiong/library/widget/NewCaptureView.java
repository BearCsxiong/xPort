package me.csxiong.library.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import me.csxiong.library.utils.XDisplayUtil;


/**
 * @Desc : 一个采集的手势View
 * @Author : csxiong - 2019-11-13
 */
public class NewCaptureView extends View {

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
    private final static int OVERSCROLL_DAMPING_ALPHA = 5;
    /**
     * 手势阻尼
     */
    private final static float GESTURE_DAMPING_ALPHA = 1.5f;
    /**
     * 手势触发时间
     */
    private final static long HAND_TOUCH_TIME = 120;

    //渐变色
    private LinearGradient backGradient;

    //画笔
    private Paint mBackgroundPaint;

    //刻度画笔
    private Paint mScalePaint;

    //中心刻度笔
    private Paint mCenterPaint;

    //手势盘画笔
    private Paint mGesturePaint;

    //面部画笔
    private Paint mFacePaint;

    //选中弧度画笔
    private Paint mArcPaint;

    //指示器画笔
    private Paint mIndicatorPaint;

    //常规scale的长度
    private int scaleLength;

    //扩张scale的长度
    private int expandScaleLength;

    //弧度宽度
    private float sweepWidth;

    //扩张的半径
    private float expandOutRadius;

    //收缩的半径
    private float shrinkOutRadius;

    //扩张的内圈半径
    private float expandInRadius;

    //正常的内圈半径
    private float normalInRadius;

    //收缩的内圈半径
    private float shrinkInRadius;

    //扩张的内部透明度变化
    private int expandAlpha = 255;

    //收缩的内部透明度
    private int shrinkAlpha = 26;

    //外部半径
    private float outRadius;

    //内部手势的半径
    private float inRadius;

    //面部指示器的半径
    private float faceRadius;

    //内部手势的透明读
    private int alpha;

    //面部透明度
    private int faceAlpha;

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
     * 绘制圆弧的矩阵
     */
    private RectF arcRectf = new RectF();

    /**
     * 顶部指示器的矩阵
     */
    private RectF roundRectf = new RectF();

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
     * 指示器透明度动画改造
     */
    private int startFaceAlpha;
    private int differFaceAlpha;

    /**
     * 拍摄时 单击的动画变量
     */
    private float captureStartInRadius;
    private float captureDifferInRadius;

    /**
     * 拍摄时 单击的透明度变化
     */
    private int captureStartAlpha;
    private int captureDifferAlpha;

    /**
     * 指示器颜色变化
     */
    private int indicatorStartColor;

    /**
     * 上一次的进度 用于比较进度
     */
    private int progress;

    /**
     * 当前指示器的颜色值
     */
    private int indicatorColor = 0xFFFFFFFF;

    /**
     * 颜色渐变计算
     */
    private ArgbEvaluator argbEvaluator;

    /**
     * 相机单击动画
     */
    private ValueAnimator captureAnimator = ValueAnimator.ofFloat(0f, 1f, 0f)
            .setDuration(300);


    /**
     * 改变动画 都是从0～1变化 内部因子使用 自己控制差值
     */
    private ValueAnimator changeAnimator = ValueAnimator.ofFloat(0f, 1f)
            .setDuration(200);

    /**
     * 相机单击更新
     */
    private ValueAnimator.AnimatorUpdateListener captureUpdateListener = animation -> {
        float value = (float) animation.getAnimatedValue();
        inRadius = captureStartInRadius + value * captureDifferInRadius;
        alpha = (int) (captureStartAlpha + value * captureDifferAlpha);
        invalidate();
    };

    /**
     * 相机单击监听
     */
    private AnimatorListenerAdapter captureListener = new AnimatorListenerAdapter() {

        @Override
        public void onAnimationCancel(Animator animation) {
            super.onAnimationCancel(animation);
            inRadius = normalInRadius;
            alpha = shrinkAlpha;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            inRadius = normalInRadius;
            alpha = shrinkAlpha;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            captureStartInRadius = inRadius;
            captureStartAlpha = alpha;

            captureDifferInRadius = shrinkInRadius - inRadius;
            captureDifferAlpha = 255 - alpha;
        }
    };

    /**
     * 动画更新回调 当前进度 0~1
     */
    private ValueAnimator.AnimatorUpdateListener updateListener = animation -> {
        //动画因子
        float fract = animation.getAnimatedFraction();
        outRadius = differOutRadius * fract + startOutRadius;
        inRadius = differInRadius * fract + startInRadius;
        alpha = (int) (fract * differAlpha + startAlpha);
        if (!isPress) {
            degree = fract * differDegree + startDegree;
        }
        faceAlpha = (int) (fract * differFaceAlpha + startFaceAlpha);
        indicatorColor = getIndicatorColor(fract, indicatorStartColor, isPress);
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
            differFaceAlpha = 0;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            differOutRadius = 0;
            differInRadius = 0;
            differAlpha = 0;
            differDegree = 0;
            differFaceAlpha = 0;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            startOutRadius = outRadius;
            startInRadius = inRadius;
            startAlpha = alpha;
            startDegree = degree;
            startFaceAlpha = faceAlpha;
            indicatorStartColor = indicatorColor;
            if (isPress) {
                differAlpha = expandAlpha - startAlpha;
                differOutRadius = expandOutRadius - outRadius;
                differInRadius = expandInRadius - inRadius;
                differFaceAlpha = 255 - faceAlpha;
            } else {
                differAlpha = shrinkAlpha - startAlpha;
                differOutRadius = shrinkOutRadius - outRadius;
                differInRadius = normalInRadius - inRadius;
                differFaceAlpha = 0 - faceAlpha;

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

    public NewCaptureView(Context context) {
        this(context, null);
    }

    public NewCaptureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NewCaptureView(Context context, AttributeSet attrs, int defStyleAttr) {
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

        mFacePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFacePaint.setColor(0xffffffff);
        mFacePaint.setAlpha(0);

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(45);
        mArcPaint.setColor(0xffffffff);
        mArcPaint.setAlpha(80);

        mIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndicatorPaint.setStyle(Paint.Style.FILL);
        mIndicatorPaint.setColor(0xFFFD5A5C);

        changeAnimator.addUpdateListener(updateListener);
        changeAnimator.addListener(listenerAdapter);

        captureAnimator.setInterpolator(new LinearInterpolator());
        captureAnimator.addUpdateListener(captureUpdateListener);
        captureAnimator.addListener(captureListener);

        argbEvaluator = new ArgbEvaluator();

    }

    /**
     * 获取渐变颜色
     *
     * @param fract
     * @param isPress
     * @return
     */
    private int getIndicatorColor(float fract, int startColor, boolean isPress) {
        return (int) argbEvaluator.evaluate(fract, startColor, isPress ? 0xFFFE537F : 0xFFFFFFFF);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //1.位移至中心
        canvas.translate(width / 2f, height / 2f);
        //2.绘制底部圆盘 这里需要旋转的原因的渐变色需要随角度变化
        canvas.drawCircle(0, 0, outRadius, mBackgroundPaint);
        //2.绘制当前进度圆弧 透明弧度
        float arcDegree = degree;
        if (arcDegree > 180) {
            arcDegree = 180;
        } else if (arcDegree < 0) {
            arcDegree = 0;
        }
        arcRectf.set(-outRadius + sweepWidth / 2f, -outRadius + sweepWidth / 2f, outRadius - sweepWidth / 2f, outRadius - sweepWidth / 2f);
        canvas.drawArc(arcRectf, 180, arcDegree, false, mArcPaint);
        //3.绘制中间透明图标
        mGesturePaint.setAlpha(alpha);
        canvas.drawCircle(0, 0, inRadius, mGesturePaint);
        //4.绘制刻度
        for (int i = 0; i <= 100; i++) {
            canvas.save();
            float scale = i * 1.8f;
            //动态计算横扫过的面积 填充刻度
            boolean isNeedFill = degree > scale;
            canvas.rotate(-90 + scale);
            int point = i % 25;
            //25分割
            if (point == 0) {
                mScalePaint.setAlpha(SCALE_TEXT_ALPHA);
                canvas.drawLine(0, -outRadius + expandScaleLength, 0, -outRadius + 5, mScalePaint);
            } else {
                if (isNeedFill) {
                    mScalePaint.setAlpha(SCALE_TEXT_ALPHA);
                } else {
                    mScalePaint.setAlpha(SCALE_ALPHA);
                }
                canvas.drawLine(0, -outRadius + scaleLength, 0, -outRadius + 5, mScalePaint);
            }

            if (i == 0 || i == 50 || i == 100) {
                int alpha = getAlpha(progress, i);
                mScalePaint.setAlpha(alpha);
                canvas.drawText(i + "", 0, -outRadius + expandScaleLength + 50, mScalePaint);
            }
            canvas.restore();
        }


        canvas.save();
        canvas.rotate(-90 + degree);
        //5.绘制标尺刻度 存在切换动画
        roundRectf.set(-4, -outRadius + expandScaleLength, 4, -outRadius - 10);
        canvas.drawRoundRect(roundRectf, 20, 20, mCenterPaint);
        //6.绘制指示刻度
        canvas.drawText((isPress ? "+" : "") + progress, 0, -outRadius + 130, mCenterPaint);
        //7.绘制内部指示点 存在颜色渐变
        mIndicatorPaint.setColor(indicatorColor);
        canvas.drawCircle(0, -inRadius + 30, 8, mIndicatorPaint);
        canvas.restore();


//        //7.绘制面部指示器 存在切换动画
//        mFacePaint.setAlpha(faceAlpha);
//        canvas.drawCircle(0, -outRadius + 30, 50, mFacePaint);
//        //中心点（0,-outRadius + 40）
//        if (faceDrawable != null) {
//            faceRadius = 30 - progress / 100f * 10;
//            faceDrawable.setAlpha(faceAlpha);
//            faceDrawable.setBounds((int) -faceRadius, (int) (-outRadius + 30 - faceRadius), (int) faceRadius, (int) (-outRadius + 30 + faceRadius));
//            faceDrawable.draw(canvas);
//        }
    }

    /**
     * 获取透明度
     *
     * @param progress
     * @return
     */
    public int getAlpha(int progress, int index) {
        if (index == 0) {
            if (progress < 5) {
                return 0;
            } else if (progress <= 10) {
                return (int) ((progress - 5) / 5f * SCALE_ALPHA);
            } else {
                return SCALE_ALPHA;
            }
        }
        if (index == 50) {
            if (progress > 40 && progress < 45) {
                return (int) ((45 - progress) / 5f * SCALE_ALPHA);
            } else if (progress >= 45 && progress <= 55) {
                return 0;
            } else if (progress > 55 && progress < 60) {
                return (int) ((progress - 55) / 5f * SCALE_ALPHA);
            } else {
                return SCALE_ALPHA;
            }
        }
        if (index == 100) {
            if (progress >= 90 && progress < 95) {
                return (int) ((95 - progress) / 5f * SCALE_ALPHA);
            } else if (progress >= 95) {
                return 0;
            } else {
                return SCALE_ALPHA;
            }
        }
        return SCALE_ALPHA;
    }

    /**
     * 设置程度值
     *
     * @param progress
     */
    public void setProgress(@IntRange(from = 0, to = 100) int progress) {
        degree = progress / 100f * MAX_DEGREE;
        invalidate();
    }

    /**
     * 获取当前进度
     *
     * @return
     */
    public int getProgress() {
        return progress;
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

    /**
     * 需要检测是否在内圈点击
     */
    private boolean isInCircle;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            lastX = (int) event.getX();
            lastY = (int) event.getY();
            isInCircle = Math.sqrt(Math.pow(lastX - center.x, 2) + Math.pow(lastY - center.y, 2)) < normalInRadius;
            Log.e("isInCircle", isInCircle + "");
            time = System.currentTimeMillis();
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //FIXME 这边目前是200毫秒开始需求点改动
            long _time = System.currentTimeMillis();
            if (!isPress && _time - time > HAND_TOUCH_TIME) {
                if (onProgressChangeListener != null) {
                    onProgressChangeListener.onTouchStateChange(true, false);
                }
                isPress = true;
                captureAnimator.cancel();
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
            //临时角度 计算当前进度
            float tempDegree = degree;
            if (tempDegree > MAX_DEGREE) {
                tempDegree = MAX_DEGREE;
            } else if (tempDegree < MIN_DEGREE) {
                tempDegree = MIN_DEGREE;
            }
            int newProgress = (int) ((tempDegree / 180) * 100);
            if (onProgressChangeListener != null) {
                onProgressChangeListener.onTouchStateChange(false, false);
            }
            if (progress != newProgress && onProgressChangeListener != null) {
                onProgressChangeListener.onProgressChange(progress, newProgress, isPress);
            }
            progress = newProgress;
            invalidate();
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) {
            time = 0;
            if (isInCircle && !isPress) {
                //触发动画收缩
                captureAnimator.cancel();
                changeAnimator.cancel();
                captureAnimator.start();
                if (onCaptureTouchListener != null) {
                    onCaptureTouchListener.onCaptureTouch();
                }
            } else {
                isInCircle = false;
                isPress = false;
                if (onProgressChangeListener != null) {
                    onProgressChangeListener.onTouchStateChange(false, true);
                }
                captureAnimator.cancel();
                changeAnimator.cancel();
                changeAnimator.start();
            }
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

    private void onInitSize(int width, int height) {
        this.width = width;
        this.height = height;
        //计算所有尺寸参数
        //以下调整UI
        //半径需要除2
        expandOutRadius = width / 1.136f / 2;
        shrinkOutRadius = width / 1.25f / 2;

        expandInRadius = width / 1.89f / 2;
        normalInRadius = width / 2.08f / 2;
        shrinkInRadius = width / 2.3f / 2;

        //计算刻度的比例长度
        scaleLength = (int) (width * 0.04f);
        expandScaleLength = (int) (width * 0.05f);

        sweepWidth = scaleLength + 5;
        mArcPaint.setStrokeWidth(sweepWidth);

        outRadius = shrinkOutRadius;
        inRadius = normalInRadius;
        alpha = shrinkAlpha;

        center.set(width / 2, height / 2);

        backGradient = new LinearGradient(0, 0, width, height, new int[]{0xFFFF48B1, 0xFFFE537F, 0xFFFD5A5C, 0xFFFD5A5C, 0xFFFD5A5C},
//                    new float[]{0.0f, 0.59f, 1.0f},
                null,
                Shader.TileMode.CLAMP);
        mBackgroundPaint.setShader(backGradient);
        mBackgroundPaint.setShadowLayer(20, 0, 0, 0xFFFD5A5C);
    }

    /**
     * 计算前后角度 需要判断顺时针和逆时针问题
     *
     * @param cen
     * @param first
     * @param second
     * @return
     */
    private float angle(Point cen, Point first, Point second) {
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
     * 进度改变监听
     */
    private OnProgressChangeListener onProgressChangeListener;

    /**
     * 采集点击事件
     */
    private OnCaptureTouchListener onCaptureTouchListener;


    /**
     * 设置进度改变监听
     *
     * @param onProgressChangeListener
     */
    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }

    /**
     * 设置采集点击事件
     *
     * @param onCaptureTouchListener
     */
    public void setOnCaptureTouchListener(OnCaptureTouchListener onCaptureTouchListener) {
        this.onCaptureTouchListener = onCaptureTouchListener;
    }

    /**
     * 进度改变监听 0~100仅在进度切换发送监听
     */
    public interface OnProgressChangeListener {

        /**
         * touch 状态改变
         *
         * @param isStart
         * @param isEnd
         */
        void onTouchStateChange(boolean isStart, boolean isEnd);

        /**
         * 手势滑动进度改变
         *
         * @param lastProgress
         * @param progress
         */
        void onProgressChange(int lastProgress, int progress, boolean isFromUser);

    }

    public interface OnCaptureTouchListener {
        /**
         * 相机点击
         */
        void onCaptureTouch();

    }
}
