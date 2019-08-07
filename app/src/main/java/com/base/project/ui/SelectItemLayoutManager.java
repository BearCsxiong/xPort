package com.base.project.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import me.csxiong.library.utils.ThreadExecutor;

public class SelectItemLayoutManager extends LinearLayoutManager {

    public SelectItemLayoutManager(Context context) {
        super(context, HORIZONTAL, false);
    }

    private int dx;

    private boolean isFirst = true;

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        if (isFirst) {
            isFirst = false;
            ThreadExecutor.get().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    scrollToPosition(0);
                }
            }, 50);
        }
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.dx += dx;
        Log.e("scrollHorizontal", this.dx + "");
        return super.scrollHorizontallyBy(dx, recycler, state);
    }
}
