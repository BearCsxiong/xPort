package com.base.multi.business.home.flingswipe;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import java.util.ArrayList;

/**
 * Created by csxiong on 2018/10/10.
 */

public class CardView extends AdapterView {

    Adapter mAdapter;

    DataSetObserver mDataSetObserver;

    private View mActiveView;

    ArrayList<View> cacheItems = new ArrayList<>();

    private int CACHE_SIZE = 5;

    private int position = 0;

    public CardView(Context context) {
        super(context);
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        mAdapter = adapter;

        if (mAdapter != null) {
            mDataSetObserver = new DataSetObserver() {
                @Override
                public void onChanged() {
                    requestLayout();
                }

                @Override
                public void onInvalidated() {
                    requestLayout();
                }
            };
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
    }

    @Override
    public View getSelectedView() {
        return mActiveView;
    }

    @Override
    public void setSelection(int i) {
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        View view = getCacheView(position);
        //1.获取顶部第一个View
        mActiveView = mAdapter.getView(position, view, this);
        //2.为当前首View设置当前Touch手势监听
        mActiveView.setOnTouchListener(new FlingCardListener(mActiveView, mAdapter.getItem(position), 0,
                new FlingCardListener.FlingListener() {
                    @Override
                    public void onCardExited() {

                    }

                    @Override
                    public void leftExit(Object dataObject) {

                    }

                    @Override
                    public void rightExit(Object dataObject) {

                    }

                    @Override
                    public void onClick(MotionEvent event, View v, Object dataObject) {

                    }

                    @Override
                    public void onScroll(float progress, float scrollXProgress) {

                    }
                }));

    }

    public View getCacheView(int position) {
        View view = null;
        if (0 < position && cacheItems.size() > CACHE_SIZE / 2) {
            view = cacheItems.get(CACHE_SIZE / 2);
        }
        return view;
    }
}
