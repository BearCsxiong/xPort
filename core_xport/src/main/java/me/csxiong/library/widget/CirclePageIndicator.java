package me.csxiong.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import me.csxiong.library.R;

/**
 * @Desc : 简单的Page圆点指示器
 * @Author : csxiong create on 2019/7/17
 */
public class CirclePageIndicator extends View {

    public CirclePageIndicator(Context context) {
        this(context, null);
    }

    public CirclePageIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePageIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initPaint();
    }

    //未选中颜色
    public int unSelectColor = 0x55eeeeee;

    //选中颜色
    public int selectColor = Color.WHITE;

    //总个数
    public int size = 3;

    //半径
    public float radius = 10;

    //间距比例
    public float ratio = 2;

    //选中position
    public int position = 0;

    //偏移量
    public float offSet = 0f;

    /**
     * 初始化参数值
     */
    private void initAttrs(Context context, AttributeSet set) {
        if (context != null && set != null) {
            TypedArray ta = context.obtainStyledAttributes(set, R.styleable.CirclePageIndicator);
            //总size
            size = ta.getInt(R.styleable.CirclePageIndicator_cpi_size, 3);
            //选中位置
            position = ta.getInt(R.styleable.CirclePageIndicator_cpi_position, 0);
            //单圆半径
            radius = ta.getDimension(R.styleable.CirclePageIndicator_cpi_radius, 10f);
            //非选中颜色
            unSelectColor = ta.getColor(R.styleable.CirclePageIndicator_cpi_unselect_color, 0xffececec);
            //选中颜色
            selectColor = ta.getColor(R.styleable.CirclePageIndicator_cpi_select_color, Color.WHITE);
            //间距比例
            ratio = ta.getFloat(R.styleable.CirclePageIndicator_cpi_ratio, 2f);
            ta.recycle();
        }
    }

    public Paint mPaint;

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(0x55eeeeee);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth() / 2;
        int h = getHeight() / 2;
        //画默认indicator
        mPaint.setColor(unSelectColor);
        for (int i = 0; i < size; i++) {
            canvas.save();
            canvas.drawCircle(w - radius * ratio * (size - 1) + 2 * ratio * i * radius, h, radius, mPaint);
            canvas.restore();
        }

        //画选中indicator
        mPaint.setColor(selectColor);
        canvas.drawCircle(w - radius * ratio * (size - 1) + 2 * ratio * (position + (isOffsetMove ? offSet : 0)) * radius, h, radius, mPaint);
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

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
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
