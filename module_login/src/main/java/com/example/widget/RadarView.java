package com.example.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import me.csxiong.library.utils.XDisplayUtil;

/**
 * @Desc : 雷达图
 * @Author : csxiong - 2020-01-06
 */
public class RadarView extends View {

    //一周为360度
    private static final int DEFAULT_DEGREE = 360;

    //5边性雷达图
    private int lineSize = 5;

    private int mRadius = XDisplayUtil.dpToPxInt(100);

    //每项分5级
    private int level = 5;

    private Paint mPaint;

    private Paint mYellowPaint;

    private Path path = new Path();

    //任何情况下的当前值
    private float[] curData = new float[level];

    //当前值和期望的差值
    private float[] differData = new float[level];

    //开始动画的保存值 主要保存开始动画的值
    private float[] startData = new float[level];

    //期望到达的值
    private float[] forwardData = new float[level];

    private String[] descs = new String[]{
            "击杀", "金钱", "防御", "魔法", "物理"
    };

    private Matrix rotateMatrix = new Matrix();

    private ValueAnimator processAnimator = ValueAnimator.ofFloat(0, 1)
            .setDuration(350);

    private boolean isStart;

    private AnimatorListenerAdapter animatorListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationCancel(Animator animation) {
            super.onAnimationCancel(animation);
            isStart = false;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            isStart = false;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            //处理差值问题
            startData = curData.clone();
            for (int i = 0; i < level; i++) {
                differData[i] = forwardData[i] - curData[i];
            }
            isStart = true;
        }
    };

    private ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (isStart) {
                //动画因子 0-1
                float fraction = animation.getAnimatedFraction();
                for (int i = 0; i < level; i++) {
                    curData[i] = startData[i] + differData[i] * fraction;
                }
                invalidate();
            }
        }
    };

    public RadarView(Context context) {
        super(context);
        init();
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(XDisplayUtil.dpToPxInt(2));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(XDisplayUtil.dpToPx(16));
        mPaint.setColor(Color.RED);

        mYellowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mYellowPaint.setStyle(Paint.Style.FILL);
        mYellowPaint.setStrokeWidth(XDisplayUtil.dpToPxInt(2));
        mYellowPaint.setColor(Color.YELLOW);

        processAnimator.addListener(animatorListenerAdapter);
        processAnimator.addUpdateListener(animatorUpdateListener);

        //5数据组 默认值
        curData = new float[]{
                0, 0, 0, 0, 0
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        double perDegree = DEFAULT_DEGREE / (float) lineSize;

        float totalPer = (float) Math.abs(Math.tan(Math.PI / lineSize) * mRadius);
        double halfDegree = perDegree / 2;
        int radius = mRadius;

        mPaint.setAlpha(255);
        for (int l = 1; l <= level; l++) {
            float ratio = l / (float) level;
            radius = (int) (ratio * mRadius);
            mYellowPaint.setAlpha((int) (255 * ratio));
            float per = (float) Math.abs(Math.tan(Math.PI / lineSize) * radius);
            for (int i = 0; i < lineSize; i++) {
                canvas.save();
                canvas.rotate((float) (perDegree * i));
                //只画左边的雷达线
                float curLevel = curData[l - 1];
                canvas.drawLine(0, 0, -per, radius, mPaint);
                canvas.drawLine(-per, radius, per, radius, mYellowPaint);

                //绘制文字
                if (l == 4) {
                    String desc = descs[i];
                    canvas.translate(-per * 1.5f, radius * 1.5f);
                    canvas.rotate(-(float) (perDegree * i));
                    canvas.drawText(desc, 0, 0, mPaint);
                }

                canvas.restore();
            }
        }

        path.reset();
        mPaint.setAlpha(100);
        //绘制雷达覆盖区域
        for (int i = 0; i < lineSize; i++) {
            canvas.save();
            float degree = (float) (perDegree * i + halfDegree);
            canvas.rotate(degree);
            rotateMatrix.reset();
            rotateMatrix.setRotate((float) (perDegree));
            float curValue = curData[i];
            float curPer = totalPer * (curValue / (float) level);
            float curRadius = mRadius * (curValue / (float) level);
            if (i == 0) {
                path.transform(rotateMatrix);
                path.moveTo(-curPer, curRadius);
            } else {
                path.transform(rotateMatrix);
                path.lineTo(-curPer, curRadius);
            }
            canvas.restore();
        }

        canvas.drawText("", 0, 0, mPaint);

        path.close();
        canvas.drawPath(path, mPaint);
    }

    public void updateData(float[] forwardData) {
        this.forwardData = forwardData;
        processAnimator.cancel();
        processAnimator.start();
    }
}
