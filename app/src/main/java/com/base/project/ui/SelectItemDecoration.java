package com.base.project.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import me.csxiong.library.utils.XDisplayUtil;

public class SelectItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;

    private int WIDTH = XDisplayUtil.dpToPxInt(200);

    public SelectItemDecoration() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int width = c.getWidth();
        int height = c.getHeight();
        c.drawRoundRect(width / 2 - WIDTH / 2, height / 2 - WIDTH / 2, width / 2 + WIDTH / 2, height / 2 + WIDTH / 2, 10, 10, mPaint);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildAdapterPosition(view);
        int endPosition = parent.getAdapter().getItemCount();
        int width = parent.getWidth();
        int height = parent.getHeight();
        // 水平方向。
        int firstItemMargin = (width - WIDTH) / 2;
        if (position == 0) {
            // 第一个Item左边距要很大。
            outRect.left = firstItemMargin - XDisplayUtil.dpToPxInt(20);
            outRect.right = 0;
        } else if (position == parent.getAdapter().getItemCount() - 1) {
            // 最后一个Item右边距要大。
            outRect.right = firstItemMargin - XDisplayUtil.dpToPxInt(20);
            outRect.left = 0;
        }
    }
}
