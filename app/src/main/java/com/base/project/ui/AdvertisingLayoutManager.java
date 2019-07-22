package com.base.project.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * @Desc : 一个特殊的广告栏布局
 * 1. 暂时称之为标签广告栏
 * @Author : csxiong - 2019/7/18
 */
public class AdvertisingLayoutManager extends RecyclerView.LayoutManager {

    /**
     * 缩放因子
     */
    private static float SCALE_FACTOR = 0.012f;

    /**
     * 位移因子
     */
    private static float TRANSLATE_FACTOR = 20;

    /**
     * 目标Rv
     */
    private RecyclerView mRv;

    /**
     * 基准一次滑动的距离
     */
    private int baseDistance = -1;

    /**
     * 每个Item的中心位置
     */
    private Point[] itemCenterPoints;

    /**
     * 中心位置
     */
    private Point centerPoint;

    /**
     * 相对初始位置的偏移量 position == 0 的偏移量X
     */
    private int offsetX;

    /**
     * 当前level到下一个level的进度 百分比进度
     */
    private float currentPer = 0.0f;

    /**
     * 当前level
     */
    private int currentLevel = 0;

    /**
     * 记录滑动状态
     */
    private int lastState = RecyclerView.SCROLL_STATE_IDLE;

    /**
     * 手指释放动画
     */
    private ValueAnimator mSpringBackAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);

    /**
     * 动画内部位移dx
     */
    private int dx;
    /**
     * 动画执行的X
     */
    private int animationX;

    /**
     * 动画是否准备好
     */
    private boolean isAnimationPrepare = false;

    /**
     * 判断手势方向
     */
    private boolean isLeftDrag;

    /**
     * 手势释放更新page的动画更新
     */
    private ValueAnimator.AnimatorUpdateListener animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            if (isAnimationPrepare) {
                float fraction = animation.getAnimatedFraction();
                int progressX = (int) (animationX * fraction);
                layoutChildren(null, progressX - dx);
                dx = progressX;
                Log.e("dx", dx + ":" + animationX);
            }
        }
    };

    /**
     * 动画开始监听
     */
    private ValueAnimator.AnimatorListener animatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationCancel(Animator animation) {
            super.onAnimationCancel(animation);
            isAnimationPrepare = false;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            isAnimationPrepare = false;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            dx = 0;
            isAnimationPrepare = true;
            if (offsetX > 0) {
                animationX = offsetX;
            } else if (offsetX < -baseDistance * (getItemCount() - 1)) {
                animationX = offsetX + baseDistance * (getItemCount() - 1);
            } else {
                if (isLeftDrag) {
                    //想要显示next
                    if (currentPer >= 0.20f) {
                        //向上level
                        animationX = (int) +(baseDistance * (1 - currentPer));
                    } else {
                        //向下level
                        animationX = (int) -(baseDistance * currentPer);
                    }
                } else {
                    //想要显示last
                    if (currentPer <= 0.80f) {
                        //向下level
                        animationX = (int) -(baseDistance * currentPer);
                    } else {
                        animationX = (int) +(baseDistance * (1 - currentPer));
                        //向上level
                    }
                }

            }
        }
    };

    /**
     * 广告布局管理器
     *
     * @param recyclerView
     */
    public AdvertisingLayoutManager(RecyclerView recyclerView) {
        if (recyclerView == null) {
            throw new NullPointerException("can't be null");
        }
        this.mRv = recyclerView;
        mSpringBackAnimator.setDuration(200);
        mSpringBackAnimator.addUpdateListener(animatorUpdateListener);
        mSpringBackAnimator.addListener(animatorListener);
    }

    /**
     * 获取默认的布局属性
     *
     * @return
     */
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 因为目前需求广告栏不多 itemCount个数不多
     * 不采用回收机制
     *
     * @param recycler
     * @param state
     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        int itemCount = getItemCount();
        if (itemCount < 1) {
            return;
        }
        centerPoint = new Point(getWidth() / 2, getHeight() / 2);
        //动态创建符合个数的points
        itemCenterPoints = new Point[itemCount];

        //因为只是广告栏,itemCount个数不多,直接排列出所有的子view,
        for (int i = 0; i < itemCount; i++) {
            View view = recycler.getViewForPosition(i);
            addView(view);
            measureChild(view, 0, 0);
            int viewWidth = getDecoratedMeasuredWidth(view);
            int viewHeight = getDecoratedMeasuredHeight(view);
            int widthSpace = getWidth() - viewWidth;
            int heightSpace = getHeight() - getDecoratedMeasuredHeight(view);

            if (baseDistance == -1) {
                //间隔距离
                baseDistance = viewWidth + widthSpace / 4;
            }
            //记录某个View中心的位置
            itemCenterPoints[i] = new Point(widthSpace / 2 + baseDistance * i + viewWidth / 2, heightSpace / 2 + viewHeight / 2);
            layoutDecoratedWithMargins(view, widthSpace / 2 + baseDistance * i, heightSpace / 2,
                    widthSpace / 2 + viewWidth + baseDistance * i,
                    heightSpace / 2 + viewHeight);
        }
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }

    /**
     * 水平滑动
     *
     * @param dx
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //这个内部view个数有限 也就是说广告个数有限 我们考虑到回收机制的问题，回收机制可以这么做 预滑动 检测到不在屏幕内容的 在滑动过后回收对象
        //计算每个人的位移量
        if (isDraging) {
            layoutChildren(recycler, dx);
        }
        return dx;
    }

    /**
     * 滑动状态切换
     *
     * @param state
     */
    @Override
    public void onScrollStateChanged(int state) {
        isDraging = state == RecyclerView.SCROLL_STATE_DRAGGING;
        if (lastState == RecyclerView.SCROLL_STATE_IDLE && state == RecyclerView.SCROLL_STATE_DRAGGING) {
            //取消动画
            cancelAnimator();
        } else if (lastState == RecyclerView.SCROLL_STATE_DRAGGING && state == RecyclerView.SCROLL_STATE_SETTLING) {
            //手释放 进入fling
            //现在把它做成直接到下一个level
            checkRelease();
        } else if (lastState == RecyclerView.SCROLL_STATE_SETTLING && state == RecyclerView.SCROLL_STATE_IDLE) {
            //fling停止 check springback
            checkRelease();
        } else if (lastState == RecyclerView.SCROLL_STATE_DRAGGING && state == RecyclerView.SCROLL_STATE_IDLE) {
            //draging停止 check springback
            checkRelease();
        }
        lastState = state;
        Log.e("ScrollState", state + "");
        super.onScrollStateChanged(state);
    }

    /**
     * 排列子View
     *
     * @param recycler
     * @param dx
     */
    public void layoutChildren(RecyclerView.Recycler recycler, int dx) {
        offsetX -= dx;
        Log.e("dx", "offsetX ->" + offsetX);
        isLeftDrag = dx > 0;
        Log.e("isLeft", isLeftDrag + "");
        //当前滚动的层级
        if (offsetX < 0) {
            currentLevel = Math.abs(offsetX / baseDistance);
        } else {
            currentLevel = -1;
        }
        //当前百分比进度
        currentPer = -(offsetX + baseDistance * currentLevel) / (float) baseDistance;
        for (int i = 0; i < getItemCount(); i++) {
            View view = mRv.getChildAt(i);
            if (i <= currentLevel) {
                float factorLevel = currentLevel - i + currentPer;
                int centerX = (view.getLeft() + view.getRight()) / 2;
                view.setScaleX(1 - factorLevel * SCALE_FACTOR);
                view.setScaleY(1 - factorLevel * SCALE_FACTOR);
                float targetX = centerPoint.x - factorLevel * TRANSLATE_FACTOR;
                //左移目标进度的差值
                view.offsetLeftAndRight((int) -(centerX - targetX));
            } else {
                view.setScaleX(1);
                view.setScaleY(1);
                view.offsetLeftAndRight(-dx);
            }
        }
    }

    private boolean isDraging;

    /**
     * 检查手指fling
     */
    public void checkRelease() {
        isDraging = false;
        Log.e("currentPer", currentPer + "");
        if (!mSpringBackAnimator.isRunning()) {
            mSpringBackAnimator.start();
        }
    }

    public void cancelAnimator() {
        if (mSpringBackAnimator != null) {
            mSpringBackAnimator.cancel();
        }
    }
}
