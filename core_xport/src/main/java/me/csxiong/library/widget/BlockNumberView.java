package me.csxiong.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.IntRange;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.view.View;

import me.csxiong.library.utils.XDisplayUtil;

/**
 * @Desc : 块风格的数字风格View
 * @Author : csxiong - 2019-11-07
 */
public class BlockNumberView extends View {

    private int color = Color.WHITE;

    private Paint mPaint;
    /**
     * 块状path
     */
    private Path blockPath = new Path();

    /**
     * 每块的宽度
     */
    private int blockWidth = XDisplayUtil.dpToPxInt(8);

    /**
     * 每块之间间隔
     */
    private int space = XDisplayUtil.dpToPxInt(2);

    private int startX;

    private int startY;

    private BlockNumber number;

    public BlockNumberView(Context context) {
        this(context, null);
    }

    public BlockNumberView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlockNumberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (number != null) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 3; j++) {
                    int index = i * 3 + j;
                    if (number.getBlocks()[index] == 1) {
                        int startX = this.startX + (j + 1) * space + j * blockWidth;
                        int startY = this.startY + (i + 1) * space + i * blockWidth;
                        Path path = getBlockPath(startX, startY);
                        canvas.drawPath(path, mPaint);
                    }
                }
            }
        }
    }

    /**
     * 绘制右边的块
     *
     * @param startX
     * @param startY
     * @return
     */
    private Path getBlockPath(int startX, int startY) {
        blockPath.reset();
        blockPath.moveTo(startX, startY);
        blockPath.lineTo(startX + blockWidth, startY);
        blockPath.lineTo(startX + blockWidth, startY + blockWidth);
        blockPath.lineTo(startX, startY + blockWidth);
        blockPath.lineTo(startX, startY);
        blockPath.close();
        return blockPath;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        onInitSize(right - left, bottom - top);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        onInitSize(w, h);
    }

    /**
     * 初始化size
     *
     * @param width
     * @param height
     */
    public void onInitSize(int width, int height) {
        blockWidth = width / 4;
        space = blockWidth / 20;

        int innerHeight = 5 * blockWidth + 6 * space;
        //循环会加 暂时不加
        startX = (blockWidth - space * 4) / 2;
        startY = (height - innerHeight) / 2;
    }

    @UiThread
    public void setNumber(@IntRange(from = 0, to = 9) int number) {
        switch (number) {
            case 0:
                this.number = BlockNumber.ZERO;
                break;
            case 1:
                this.number = BlockNumber.ONE;
                break;
            case 2:
                this.number = BlockNumber.TWO;
                break;
            case 3:
                this.number = BlockNumber.THREE;
                break;
            case 4:
                this.number = BlockNumber.FOUR;
                break;
            case 5:
                this.number = BlockNumber.FIVE;
                break;
            case 6:
                this.number = BlockNumber.SIX;
                break;
            case 7:
                this.number = BlockNumber.SEVEN;
                break;
            case 8:
                this.number = BlockNumber.EIGHT;
                break;
            case 9:
                this.number = BlockNumber.NINE;
                break;
            default:
                this.number = null;
                break;
        }
        invalidate();
    }


    /**
     * 块状数字
     */
    public enum BlockNumber {
        ZERO(new int[]{
                1, 1, 1,
                1, 0, 1,
                1, 0, 1,
                1, 0, 1,
                1, 1, 1}),
        ONE(new int[]{
                0, 0, 1,
                0, 0, 1,
                0, 0, 1,
                0, 0, 1,
                0, 0, 1}),
        TWO(new int[]{
                1, 1, 1,
                0, 0, 1,
                1, 1, 1,
                1, 0, 0,
                1, 1, 1
        }),
        THREE(new int[]{
                1, 1, 1,
                0, 0, 1,
                1, 1, 1,
                0, 0, 1,
                1, 1, 1
        }),
        FOUR(new int[]{
                1, 0, 1,
                1, 0, 1,
                1, 1, 1,
                0, 0, 1,
                0, 0, 1
        }),
        FIVE(new int[]{
                1, 1, 1,
                1, 0, 0,
                1, 1, 1,
                0, 0, 1,
                1, 1, 1
        }),
        SIX(new int[]{
                1, 1, 1,
                1, 0, 0,
                1, 1, 1,
                1, 0, 1,
                1, 1, 1
        }),
        SEVEN(new int[]{
                1, 1, 1,
                0, 0, 1,
                0, 0, 1,
                0, 0, 1,
                0, 0, 1
        }),
        EIGHT(new int[]{
                1, 1, 1,
                1, 0, 1,
                1, 1, 1,
                1, 0, 1,
                1, 1, 1
        }),
        NINE(new int[]{
                1, 1, 1,
                1, 0, 1,
                1, 1, 1,
                0, 0, 1,
                1, 1, 1
        });

        private int[] blocks;

        BlockNumber(int[] blocks) {
            this.blocks = blocks;
        }

        public int[] getBlocks() {
            return blocks;
        }

        public void setBlocks(int[] blocks) {
            this.blocks = blocks;
        }}


}
