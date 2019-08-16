package com.base.project.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import me.csxiong.library.utils.XDisplayUtil;

public class SelectItemLayoutManager extends LinearLayoutManager {

    public int centerX = XDisplayUtil.getScreenWidth() / 2;

    public SelectItemLayoutManager(Context context) {
        super(context, HORIZONTAL, false);
    }

    private int transitionX;

    private boolean isFirst = true;

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        if (isFirst) {
            isFirst = false;
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //dx 左负右正
        int itemCount = getItemCount();
        int firstVisibleItemPosition = findFirstVisibleItemPosition();
        int lastVisibleItemPosition = findLastVisibleItemPosition();

        boolean needSpringBack = false;
        if (firstVisibleItemPosition == 0) {
            View view = findViewByPosition(0);
            int viewCenterX = (view.getLeft() + view.getRight()) / 2;
            needSpringBack = viewCenterX - dx > centerX;
            if (needSpringBack) {
                offsetChildrenHorizontal(-dx);
                return 0;
            }
        } else if (lastVisibleItemPosition == itemCount - 1) {
            View view = findViewByPosition(lastVisibleItemPosition);
            int viewCenterX = (view.getLeft() + view.getRight()) / 2;
            needSpringBack = viewCenterX - dx < centerX;
            Log.e("scrollHorizontalBy", needSpringBack + ":" + dx);
            if (needSpringBack) {
                offsetChildrenHorizontal(-dx);
                return 0;
            }
        }
        return super.scrollHorizontallyBy(dx, recycler, state);
    }
}
