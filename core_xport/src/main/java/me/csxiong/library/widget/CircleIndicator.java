package me.csxiong.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Desc : 简单的圆点指示器
 * @Author : csxiong create on 2019/7/17
 */
public class CircleIndicator extends View {
    public CircleIndicator(Context context) {
        super(context);
        initPaint();
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public CircleIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    public void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#55eeeeee"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    public Paint mPaint;

    public int unSelectColor = Color.parseColor("#55eeeeee");
    public int selectColor = Color.WHITE;
    public int size = 3;
    public int radius = 5;
    public int radio = 2;
    public int position = 0;
    public float offSet = 0f;//偏移量

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth() / 2;
        int h = getHeight() / 2;
        //画默认indicator
        mPaint.setColor(unSelectColor);
        for (int i = 0; i < size; i++) {
            canvas.save();
            canvas.drawCircle(w - radius * radio * (size - 1) + 2 * radio * i * radius, h, radius, mPaint);
            canvas.restore();
        }

        //画选中indicator
        mPaint.setColor(selectColor);
        canvas.drawCircle(w - radius * radio * (size - 1) + 2 * radio * (position + (isOffsetMove ? offSet : 0)) * radius, h, radius, mPaint);
        if (isFadeOutAtLast) {
            fadeOutAnim();
        }
    }

    public boolean isFadeOutAtLast = false;

    private boolean isOffsetMove = false;

    public boolean isOffsetMove() {
        return isOffsetMove;
    }

    public void setOffsetMove(boolean offsetMove) {
        isOffsetMove = offsetMove;
    }

    public boolean isFadeOutAtLast() {
        return isFadeOutAtLast;
    }

    public void setFadeOutAtLast(boolean fadeOutAtLast) {
        isFadeOutAtLast = fadeOutAtLast;
    }

    public void fadeOutAnim() {
        if (position == size - 2 && offSet > 0) {
            this.setAlpha(1f - offSet * 2);
        } else {
            this.setAlpha(1f);
        }
    }

    public int getUnSelectColor() {
        return unSelectColor;
    }

    public void setUnSelectColor(int unSelectColor) {
        this.unSelectColor = unSelectColor;
    }

    public int getSelectColor() {
        return selectColor;
    }

    public void setSelectColor(int selectColor) {
        this.selectColor = selectColor;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRadio() {
        return radio;
    }

    public void setRadio(int radio) {
        this.radio = radio;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
        invalidate();
    }

    public float getOffSet() {
        return offSet;
    }

    public void setOffSet(float offSet) {
        this.offSet = offSet;
    }
}
