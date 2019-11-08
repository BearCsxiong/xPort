package me.csxiong.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.IntRange;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.view.View;

import me.csxiong.library.utils.XDisplayUtil;

/**
 * @Desc : 闹钟的Number
 * @Author : csxiong - 2019-11-05
 */
public class DigtalView extends View {

    private int color = Color.WHITE;

    private int length;

    private int width = XDisplayUtil.dpToPxInt(8);

    private int space;

    private Paint mPaint;

    private Path horizontalPath;

    private Path verticalPath;

    private Point indexTL;

    private Point indexTR;

    private Point indexL;

    private Point indexR;

    private Point indexBL;

    private Number number;

    public DigtalView(Context context) {
        this(context, null);
    }

    public DigtalView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DigtalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);

        horizontalPath = new Path();
        verticalPath = new Path();
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (number != null) {
            Path path;
            if (number.isBound_1()) {
                path = getHorizontalPath(indexTL.x + space, indexTL.y);
                canvas.drawPath(path, mPaint);
            }

            if (number.isBound_2()) {
                path = getVerticalPath(indexTL.x, indexTL.y + space);
                canvas.drawPath(path, mPaint);
            }

            if (number.isBound_3()) {
                path = getVerticalPath(indexTR.x, indexTR.y + space);
                canvas.drawPath(path, mPaint);
            }

            if (number.isBound_4()) {
                path = getHorizontalPath(indexL.x + space, indexL.y);
                canvas.drawPath(path, mPaint);
            }

            if (number.isBound_5()) {
                path = getVerticalPath(indexL.x, indexL.y + space);
                canvas.drawPath(path, mPaint);
            }

            if (number.isBound_6()) {
                path = getVerticalPath(indexR.x, indexR.y + space);
                canvas.drawPath(path, mPaint);
            }

            if (number.isBound_7()) {
                path = getHorizontalPath(indexBL.x + space, indexBL.y);
                canvas.drawPath(path, mPaint);
            }

        }
    }

    @UiThread
    public void setNumber(@IntRange(from = 0, to = 9) int number) {
        switch (number) {
            case 0:
                this.number = Number.ZERO;
                break;
            case 1:
                this.number = Number.ONE;
                break;
            case 2:
                this.number = Number.TWO;
                break;
            case 3:
                this.number = Number.THREE;
                break;
            case 4:
                this.number = Number.FOUR;
                break;
            case 5:
                this.number = Number.FIVE;
                break;
            case 6:
                this.number = Number.SIX;
                break;
            case 7:
                this.number = Number.SEVEN;
                break;
            case 8:
                this.number = Number.EIGHT;
                break;
            case 9:
                this.number = Number.NINE;
                break;
            default:
                this.number = null;
                break;
        }
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        onInitMeasureSize(right - left, bottom - top);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        onInitMeasureSize(w, h);
    }

    public void onInitMeasureSize(int w, int h) {
        int length = w / 2;
        this.length = length;
        length += width;

        space = XDisplayUtil.dpToPxInt(3);

        int x = (w - length - space * 4) / 2;
        int y = (h - length * 2 - space * 6) / 2;

        indexTL = new Point(x + space, y + space);
        indexTR = new Point(x + space * 3 + length, y + space);
        indexL = new Point(x + space, y + space * 3 + length);
        indexR = new Point(x + space * 3 + length, y + space * 3 + length);
        indexBL = new Point(x + space, y + space * 5 + length * 2);
    }

    public Path getHorizontalPath(int startX, int startY) {
        horizontalPath.reset();
        int halfWidth = width / 2;
        horizontalPath.moveTo(startX, startY);
        horizontalPath.lineTo(startX + halfWidth, startY - halfWidth);
        horizontalPath.lineTo(startX + halfWidth + length, startY - halfWidth);
        horizontalPath.lineTo(startX + width + length, startY);
        horizontalPath.lineTo(startX + halfWidth + length, startY + halfWidth);
        horizontalPath.lineTo(startX + halfWidth, startY + halfWidth);
        horizontalPath.lineTo(startX, startY);
        horizontalPath.close();
        return horizontalPath;
    }

    public Path getVerticalPath(int startX, int startY) {
        verticalPath.reset();
        int halfWidth = width / 2;
        verticalPath.moveTo(startX, startY);
        verticalPath.lineTo(startX + halfWidth, startY + halfWidth);
        verticalPath.lineTo(startX + halfWidth, startY + length + halfWidth);
        verticalPath.lineTo(startX, startY + length + width);
        verticalPath.lineTo(startX - halfWidth, startY + length + halfWidth);
        verticalPath.lineTo(startX - halfWidth, startY + halfWidth);
        verticalPath.lineTo(startX, startY);
        return verticalPath;
    }

    public enum Number {
        ZERO(true, true, true, false, true, true, true),
        ONE(false, false, true, false, false, true, false),
        TWO(true, false, true, true, true, false, true),
        THREE(true, false, true, true, false, true, true),
        FOUR(false, true, true, true, false, true, false),
        FIVE(true, true, false, true, false, true, true),
        SIX(true, true, false, true, true, true, true),
        SEVEN(true, false, true, false, false, true, false),
        EIGHT(true, true, true, true, true, true, true),
        NINE(true, true, true, true, false, true, true);

        private boolean bound_1;
        private boolean bound_2;
        private boolean bound_3;
        private boolean bound_4;
        private boolean bound_5;
        private boolean bound_6;
        private boolean bound_7;

        Number(boolean bound_1, boolean bound_2, boolean bound_3, boolean bound_4, boolean bound_5, boolean bound_6, boolean bound_7) {
            this.bound_1 = bound_1;
            this.bound_2 = bound_2;
            this.bound_3 = bound_3;
            this.bound_4 = bound_4;
            this.bound_5 = bound_5;
            this.bound_6 = bound_6;
            this.bound_7 = bound_7;
        }

        public boolean isBound_1() {
            return bound_1;
        }

        public void setBound_1(boolean bound_1) {
            this.bound_1 = bound_1;
        }

        public boolean isBound_2() {
            return bound_2;
        }

        public void setBound_2(boolean bound_2) {
            this.bound_2 = bound_2;
        }

        public boolean isBound_3() {
            return bound_3;
        }

        public void setBound_3(boolean bound_3) {
            this.bound_3 = bound_3;
        }

        public boolean isBound_4() {
            return bound_4;
        }

        public void setBound_4(boolean bound_4) {
            this.bound_4 = bound_4;
        }

        public boolean isBound_5() {
            return bound_5;
        }

        public void setBound_5(boolean bound_5) {
            this.bound_5 = bound_5;
        }

        public boolean isBound_6() {
            return bound_6;
        }

        public void setBound_6(boolean bound_6) {
            this.bound_6 = bound_6;
        }

        public boolean isBound_7() {
            return bound_7;
        }

        public void setBound_7(boolean bound_7) {
            this.bound_7 = bound_7;
        }
    }
}
