package com.base.project.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import me.csxiong.library.utils.XDisplayUtil;

/**
 * @Desc : 一个特殊的广告栏效果
 * @Author : csxiong - 2019-08-26
 */
public class AdvertisingViewPager extends FrameLayout {

    private FrameLayout mFlBackground;

    /**
     * 背景recyclerView
     */
    private RecyclerView mBackgroundRv;

    /**
     * 前景recyclerView
     */
    private RecyclerView mFrontRv;

    /**
     * 当前滑动状态
     */
    private int curState;

    /**
     *
     */
    private PagerSnapHelper mPagerSnapHelper;

    /**
     * 滑动监听,->前景滑动监听
     */
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //主要寻找逻辑
            if (curState != RecyclerView.SCROLL_STATE_IDLE && newState == RecyclerView.SCROLL_STATE_IDLE) {
                //停止滚动
                if (mBackgroundRv != null) {
                    mBackgroundRv.postDelayed(() -> {
                        if (mBackgroundRv != null && mFrontRv != null) {
                            mBackgroundRv.smoothScrollToPosition(((LinearLayoutManager) mFrontRv.getLayoutManager()).findFirstVisibleItemPosition());
                        }
                    }, 50);
                }
            }
            curState = newState;
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    public AdvertisingViewPager(Context context) {
        this(context, null);
    }

    public AdvertisingViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdvertisingViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPagerSnapHelper = new PagerSnapHelper();
    }

    /**
     * 部分初始化
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //创建两个RecyclerView
        mFlBackground = new FrameLayout(getContext());
        mBackgroundRv = new RecyclerView(getContext());
        mBackgroundRv.setLayoutParams(new LayoutParams(-1, -1));
        mFlBackground.addView(mBackgroundRv);
        addView(mFlBackground, new FrameLayout.LayoutParams(-1, -1));

        mFrontRv = new RecyclerView(getContext());
        LayoutParams lp = new LayoutParams(-1, -1);
        lp.gravity = Gravity.CENTER;
        mFrontRv.setLayoutParams(lp);
        addView(mFrontRv);


        //初始化部分需要前置recycler滑动监听的标志
        mFrontRv.addOnScrollListener(onScrollListener);
        mPagerSnapHelper.attachToRecyclerView(mFrontRv);
    }

    /**
     * 设置双方适配器
     *
     * @param backgroundAdapter
     * @param frontAdapter
     */
    public void setAdapter(RecyclerView.Adapter backgroundAdapter, RecyclerView.Adapter frontAdapter) {
        if (mBackgroundRv != null && mFrontRv != null) {
            mBackgroundRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            mFrontRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

            mBackgroundRv.setAdapter(backgroundAdapter);
            mFrontRv.setAdapter(frontAdapter);
        }
    }

    boolean isNormal = true;

    /**
     * 背景板缩放到内部
     */
    public void scaleContent(boolean isNormal) {
        if (this.isNormal == isNormal) {
            return;
        }
        this.isNormal = isNormal;
        float height = XDisplayUtil.dpToPx(350);
        float width = XDisplayUtil.dpToPx(237);

        int measuredHeight = mBackgroundRv.getMeasuredHeight();
        int measuredWidth = mBackgroundRv.getMeasuredWidth();

        mFlBackground.animate().cancel();
        //内部的所有viewholder都需要做当个内容缩放
        mFlBackground.animate().scaleX(isNormal ? 1.0f : width / measuredWidth).scaleY(isNormal ? 1.0f : height / measuredHeight).setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

}
