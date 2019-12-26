package com.example.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import me.csxiong.library.utils.XDisplayUtil;

/**
 * @Desc : 多边形View
 * @Author : csxiong - 2019-12-23
 */
public class PolygonView extends View {

    //一周为360度
    private static final int DEFAULT_DEGREE = 360;

    private int lineSize = 4;

    private int mRadius = XDisplayUtil.dpToPxInt(50);

    private Paint mPaint;

    public PolygonView(Context context) {
        super(context);
        init();
    }

    public PolygonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PolygonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(XDisplayUtil.dpToPxInt(2));
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
        double perDegree = DEFAULT_DEGREE / (float) lineSize;
        float per = (float) Math.abs(Math.tan(Math.PI/lineSize) * mRadius);
        for (int i = 0; i < lineSize; i++) {
            canvas.save();
            canvas.rotate((float) (perDegree * i));
            canvas.drawLine(-per, mRadius, per, mRadius, mPaint);
            canvas.restore();
        }
    }

    public void setLineSize(int lineSize) {
        this.lineSize = lineSize;
    }
}
