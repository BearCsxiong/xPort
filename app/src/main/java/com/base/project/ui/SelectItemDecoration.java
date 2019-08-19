package com.base.project.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import me.csxiong.library.utils.XDisplayUtil;

/**
 * @Desc : 选中框 带目标自动滚动
 * @Author : csxiong - 2019/8/8
 */
public class SelectItemDecoration extends RecyclerView.ItemDecoration {

    private RecyclerView rv;

    private Paint mPaint;

    private int WIDTH = XDisplayUtil.dpToPxInt(88);

    private int HEIGHT = XDisplayUtil.dpToPxInt(68);

    private int scrollState = RecyclerView.SCROLL_STATE_IDLE;

    private int cornerRadius = XDisplayUtil.dpToPxInt(10);

    private int centerX = XDisplayUtil.getScreenWidth() / 2;

    /**
     * 选中框的alpha
     */
    private ValueAnimator selectAlphaAnimator = ValueAnimator.ofFloat(0.0f, 1.0f)
            .setDuration(200);

    /**
     * 自动滑动duration
     */
    private ValueAnimator autoScrollAnimator = ValueAnimator.ofInt(0).setDuration(200);

    /**
     * 当前的Alpha值
     */
    private float selectAlpha = 1.0f;

    /**
     * 手势放大因子
     */
    private float gestureFactor = .2f;

    /**
     * 是否处于滚动
     */
    private boolean isScroll = false;

    /**
     * 自动滚动当前值
     */
    private int transitionValue;

    /**
     * 选中行 对应的是AdapterPosition 默认选中
     */
    private int selectPosition = 0;

    /**
     * 自动滚动是否准备开始
     */
    private boolean isPrepareStart;

    /**
     * 列表是否左滚动
     */
    private boolean isScrollLeft = true;

    public SelectItemDecoration(RecyclerView rv) {
        this.rv = rv;
        //添加滚动监听
        rv.addOnScrollListener(onScrollListener);
        selectAlphaAnimator.addUpdateListener(animation -> {
            selectAlpha = animation.getAnimatedFraction();
            if (!isScroll) {
                rv.invalidate();
            }
        });

        //自动滚动 更新
        autoScrollAnimator.addUpdateListener(animation -> {
            if (isPrepareStart) {
                Object animatedValue = animation.getAnimatedValue();
                if (animatedValue instanceof Integer) {
                    int currentValue = (int) animatedValue;
                    int dx = currentValue - transitionValue;
                    rv.scrollBy(dx, 0);
                    transitionValue = (int) animatedValue;
                }
            }
        });

        //自动滚动动画监听
        autoScrollAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                isPrepareStart = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                checkSelect();
                isPrepareStart = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isPrepareStart = true;
                transitionValue = 0;
            }
        });

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(0xffFF453A);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 绘制decoration
     * 1.主要是选中框和选中状态切换时的变更
     *
     * @param c
     * @param parent
     * @param state
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int width = c.getWidth();
        int height = c.getHeight();
        //在选中和非选中需要切换状态
        float selectRectFAlpht = isScroll ? selectAlpha : 1 - selectAlpha;

        //绘制外圈
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAlpha((int) (selectRectFAlpht * 255));
        c.drawRoundRect(width / 2 - WIDTH / 2, height / 2 - HEIGHT / 2, width / 2 + WIDTH / 2, height / 2 + HEIGHT / 2, cornerRadius, cornerRadius, mPaint);

        //绘制选中内圈
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAlpha((int) ((1 - selectRectFAlpht) * .1f * 255));
        c.drawRoundRect(width / 2 - WIDTH / 2, height / 2 - HEIGHT / 2, width / 2 + WIDTH / 2, height / 2 + HEIGHT / 2, cornerRadius, cornerRadius, mPaint);
    }

    /**
     * 滑动监听
     */
    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (scrollState == RecyclerView.SCROLL_STATE_IDLE && newState != RecyclerView.SCROLL_STATE_IDLE) {
                checkScroll();
            } else if (scrollState != RecyclerView.SCROLL_STATE_IDLE && newState == RecyclerView.SCROLL_STATE_IDLE) {
                checkRelease();
            }
            scrollState = newState;
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            isScrollLeft = dx > 0;
        }
    };

    /**
     * 检测手势释放
     */
    private void checkRelease() {
        RecyclerView.LayoutManager lm = rv.getLayoutManager();
        if (lm instanceof LinearLayoutManager) {
            LinearLayoutManager llm = (LinearLayoutManager) lm;
            int firstVisibleItem = llm.findFirstVisibleItemPosition();
            int lastVisibleItem = llm.findLastVisibleItemPosition();
            Log.e("position", "first:" + firstVisibleItem + "-last:" + lastVisibleItem);

            if (firstVisibleItem == -1 || lastVisibleItem == -1) {
                checkOverScroll();
                return;
            }

            int tempSelectPosition = firstVisibleItem;
            int offsetX = centerX;
            for (int i = firstVisibleItem; i <= lastVisibleItem; i++) {
                View view = llm.findViewByPosition(i);
                //手势趋势因子
                int xFraction = isScrollLeft ? (int) (-i * gestureFactor * centerX) : (int) (i * gestureFactor * centerX);
                int tempOffsetX = Math.abs((view.getLeft() + view.getRight()) / 2 - centerX) + xFraction;
                if (offsetX > tempOffsetX) {
                    offsetX = tempOffsetX;
                    tempSelectPosition = i;
                }
            }
            selectPosition(tempSelectPosition);
        }
    }

    /**
     * 检查overScroll
     */
    public void checkOverScroll() {
        //这边默认检查首尾
        View firstItem = rv.getLayoutManager().findViewByPosition(0);
        View lastItem = rv.getLayoutManager().findViewByPosition(rv.getLayoutManager().getItemCount() - 1);
        if (firstItem != null) {
            selectPosition(0);
        } else if (lastItem != null) {
            selectPosition(rv.getLayoutManager().getItemCount() - 1);
        }
    }

    /**
     * 滑动到某行
     *
     * @param selectPosition 注意 scrollToTarget滑动到当前在recyclerView中的对应View
     */
    public void selectPosition(int selectPosition) {
        selectPosition(selectPosition, 1);
    }

    /**
     * 滑动到某行
     *
     * @param selectPosition
     * @param damping
     */
    public void selectPosition(int selectPosition, int damping) {
        this.selectPosition = selectPosition;
        View itemView = rv.getLayoutManager().findViewByPosition(selectPosition);
        if (itemView != null) {
            autoScrollAnimator.setIntValues(0, (itemView.getLeft() / 2 + itemView.getRight() / 2 - centerX) * damping);
            autoScrollAnimator.start();
        }
    }

    /**
     * 检测滚动
     * 1.手势drag
     * 2.内部滚动
     */
    private void checkScroll() {
        if (autoScrollAnimator.isRunning()) {
            autoScrollAnimator.cancel();
        }
        if (selectAlphaAnimator.isRunning()) {
            selectAlphaAnimator.cancel();
        }
        isScroll = true;
        if (onPageListener != null) {
            onPageListener.onPageDetach(selectPosition);
        }
        selectAlphaAnimator.start();
    }

    /**
     * 检测选中状态
     */
    private void checkSelect() {
        isScroll = false;
        if (selectAlphaAnimator.isRunning()) {
            selectAlphaAnimator.cancel();
        }
        if (onPageListener != null) {
            onPageListener.onPageAttach(selectPosition);
        }
        selectAlphaAnimator.start();
    }

    /**
     * page选中事件
     */
    private OnPageListener onPageListener;

    /**
     * 注册page选中事件
     *
     * @param onPageListener
     */
    public void setOnPageListener(OnPageListener onPageListener) {
        this.onPageListener = onPageListener;
    }

    /**
     * page选中事件
     */
    public interface OnPageListener {

        /**
         * page选中事件
         *
         * @param position 选中行 adapterPosition
         */
        void onPageAttach(int position);

        /**
         * page取消选中事件
         *
         * @param position 选中行 adapterPosition
         */
        void onPageDetach(int position);
    }

}
