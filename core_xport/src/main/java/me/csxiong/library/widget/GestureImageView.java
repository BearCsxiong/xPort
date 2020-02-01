package me.csxiong.library.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import me.csxiong.library.utils.XAnimator;
import me.csxiong.library.utils.gesture.XGestureDetector;

/**
 * @Desc : 图片手势View
 * @Author : csxiong - 2020-01-28
 * 1.一个强大的PhotoView 预览图片
 * 2.双击放大
 * 3.阻尼缩放
 * 4.放大预览检测
 * 5.边界检测
 * 6.TODO FLING 快滚
 * <p>
 * BUGFIX:
 * 1.FIXME 修复ViewPager手势冲突问题
 * 2.FIXME 添加主Major手指检测
 */
public class GestureImageView extends AppCompatImageView {

    /**
     * 改变时长
     */
    private static final long CHANGE_DURATION = 150;

    /**
     * 缩放阻尼系数
     */
    private static final float SCALE_DAMP = 1.8f;

    /**
     * 位移阻尼系数
     */
    private static final float TRANSLATE_DAMP = 4f;

    /**
     * 最小缩放比例 -> 这里有个对应的隐藏逻辑 这里1的scale 是指图片相对居中适配后的相对值的最小值
     */
    private static final float MIN_SCALE = 1.0f;

    /**
     * 最大缩放比例
     */
    private static final float MAX_SCALE = 4.0f;

    /**
     * 手势检测
     */
    private XGestureDetector gestureDetector;

    /**
     * 获取Matrix中矩阵数据
     */
    private float[] values = new float[9];

    /**
     * 当前矩形
     */
    private RectF tempRectf = new RectF();

    /**
     * 图片基础矩形
     */
    private RectF baseRectf = new RectF();

    /**
     * 当前View宽度
     */
    private int width;

    /**
     * 当前View高度
     */
    private int height;

    /**
     * 临时标志:双击是否触发标志位
     */
    private boolean isDoubleConsume;

    /**
     * 临时标志:是否是ActionDown检查边界
     */
    private boolean isActionDownCheckTouchBound;

    /**
     * 是否跳过onScroll
     */
    private boolean isSkipScroll;

    /**
     * 缩放手势最新FocusX
     */
    private float gestureFocusX;

    /**
     * 缩放手势最新FocusY
     */
    private float gestureFocusY;

    //-----------------动效内部值-------

    /**
     * 缩放差值
     */
    private float differScale;

    /**
     * 位移X差值
     */
    private float differTranslateX;

    /**
     * 位移Y差值
     */
    private float differTranslateY;

    /**
     * 缩放中心X
     */
    private float focusX;

    /**
     * 缩放中心Y
     */
    private float focusY;

    //-----------------动效内部值-------

    /**
     * 位移至目标
     *
     * @param tx 位移X
     * @param ty 位移Y
     */
    private void translateTo(float tx, float ty) {
        changeAnimator.cancel();
        //缩放无变化
        differScale = 1;
        //位移改变
        differTranslateX = tx;
        differTranslateY = ty;
        changeAnimator.start();
    }

    /**
     * 缩放至目标
     *
     * @param scale 目标缩放值
     * @param px    目标中心点X
     * @param py    目标中心点Y
     */
    private void scaleTo(float scale, float px, float py) {
        changeAnimator.cancel();
        //预缩放
        float currentScale = getCurrentScale();
        float scaleRatio = scale / currentScale;
        tempMatrix.set(changeMatrix);
        tempMatrix.postScale(scaleRatio, scaleRatio, px, py);
        tempRectf.set(baseRectf);
        tempMatrix.mapRect(tempRectf);
        //确定将要缩放到的矩形
        //计算中心偏移值 以及边界条件
        differScale = scaleRatio - 1;
        differTranslateX = 0;
        if (tempRectf.width() > width) {
            //边界判断
            if (tempRectf.left > 0) {
                differTranslateX = -tempRectf.left;
            } else if (tempRectf.right < width) {
                differTranslateX = width - tempRectf.right;
            }
        } else {
            //中心点判断
            differTranslateX = baseRectf.centerX() - tempRectf.centerX();
        }

        differTranslateY = 0;
        if (tempRectf.height() > height) {
            if (tempRectf.top > 0) {
                differTranslateY = -tempRectf.top;
            } else if (tempRectf.bottom < height) {
                differTranslateY = height - tempRectf.bottom;
            }
        } else {
            differTranslateY = baseRectf.centerY() - tempRectf.centerY();
        }

        differScale = scaleRatio - 1;
        focusX = px;
        focusY = py;
        changeAnimator.start();
    }

    /**
     * 改变图形位置Animator
     */
    private XAnimator changeAnimator = XAnimator.ofFloat(0, 1)
            .duration(CHANGE_DURATION)
            .setAnimationListener(new XAnimator.XAnimationListener() {
                @Override
                public void onAnimationUpdate(float fraction, float value) {
                    changeMatrix.set(tempMatrix);
                    //仅有缩放变化 动态改变
                    if (differScale != 1) {
                        changeMatrix.postScale(1 + differScale * fraction, 1 + differScale * fraction, focusX, focusY);
                    }
                    //仅有位移变化 动态变化
                    if (differTranslateX != 0 || differTranslateY != 0) {
                        changeMatrix.postTranslate(differTranslateX * fraction, differTranslateY * fraction);
                    }
                    updateChange();
                }

                @Override
                public void onAnimationStart(XAnimator animation) {
                    //保存当前改变的Matrix
                    tempMatrix.set(changeMatrix);
                    Log.e("Temp", "differTranslate:" + differTranslateX + "---" + differTranslateY);
                    Log.e("Temp", "differScale:" + differScale);
                }

                @Override
                public void onAnimationEnd(XAnimator animation) {

                }

                @Override
                public void onAnimationCancel(XAnimator animation) {

                }
            });

    /**
     * 手势检测回调
     */
    private XGestureDetector.OnGestureListener onGestureListener = new XGestureDetector.OnGestureListener() {

        @Override
        public void onMultiTouchChange(boolean isMultiTouch, int touchCount) {
        }

        @Override
        public void onScaleStart() {
            changeAnimator.cancel();
        }

        @Override
        public void onScaleEnd() {
        }

        @Override
        public void onScale(float scaleFraction, float focusX, float focusY) {
            gestureFocusX = focusX;
            gestureFocusY = focusY;
            float currentScale = getCurrentScale();
            //阻尼保护
            if (currentScale <= MIN_SCALE && scaleFraction < 1) {
                scaleFraction = (float) (1 - Math.pow(1 - scaleFraction, SCALE_DAMP));
            } else if (currentScale >= MAX_SCALE && scaleFraction > 1) {
                scaleFraction = (float) (1 + Math.pow(scaleFraction - 1, SCALE_DAMP));
            }
            changeMatrix.postScale(scaleFraction, scaleFraction, focusX, focusY);
            updateChange();
        }

        @Override
        public void onScroll(float distanceX, float distanceY) {
            RectF currentRectf = getCurrentRectf();
            //BugFix:首次触控检测,解决Horizontal手势冲突
            if (isActionDownCheckTouchBound && getParent() != null) {
                //BugFix:校准上下手势,修复部分细微Y轴位移导致触发检测
                boolean isHorizontalScroll = Math.abs(distanceX) > Math.abs(distanceY);
                if (isHorizontalScroll) {
                    if (baseRectf.width() < width) {
                        //BugFix:修复在原始宽度和控件宽度不一致情况下的边界问题
                        if (currentRectf.width() >= baseRectf.width() && currentRectf.width() < width) {
                            //中心点判断是否可以滑动
                            if (Math.abs(currentRectf.centerX() - baseRectf.centerX()) < .1f) {
                                isSkipScroll = true;
                                getParent().requestDisallowInterceptTouchEvent(false);
                            }
                        } else if (currentRectf.width() >= width) {
                            //左右边界 释放手势
                            if ((Math.abs(currentRectf.left) < .1f && distanceX < 0)
                                    || (Math.abs(currentRectf.right - width) < .1f && distanceX > 0)) {
                                isSkipScroll = true;
                                getParent().requestDisallowInterceptTouchEvent(false);
                            }
                        }
                    } else {
                        //左右边界 释放手势
                        if ((Math.abs(currentRectf.left) < .1f && distanceX < 0)
                                || (Math.abs(currentRectf.right - width) < .1f && distanceX > 0)) {
                            isSkipScroll = true;
                            getParent().requestDisallowInterceptTouchEvent(false);
                            return;
                        }
                        Log.e("onScroll", (currentRectf.left >= 0) + "--" + distanceX + ":::" + currentRectf.right + "--" + distanceX);
                    }
                }
                isActionDownCheckTouchBound = false;
                return;
            }
            //BugFix:修复在onScroll在不断回调情况下 部分requestDisallowInterceptTouchEvent释放不及时 导致部分滚动
            if (isSkipScroll) {
                return;
            }
            //阻尼保护 边界保护
            boolean isDampX = currentRectf.left >= 0 || currentRectf.right <= width;
            boolean isDampY = currentRectf.top >= 0 || currentRectf.bottom <= height;
            if (isDampX) {
                distanceX /= TRANSLATE_DAMP;
            }
            if (isDampY) {
                distanceY /= TRANSLATE_DAMP;
            }
            //防止在重置时触发滚动
            if (!changeAnimator.isRunning()) {
                changeMatrix.postTranslate(-distanceX, -distanceY);
                updateChange();
            }
        }

        @Override
        public void onFling(float velocityX, float velocityY) {
        }

        @Override
        public void onActionDown() {
            if (getParent() != null) {
                //手指初次下压 请求不拦截Touch事件
                isSkipScroll = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                //记录手指下压首次
                isActionDownCheckTouchBound = true;
            }
        }

        @Override
        public void onSingleTap(MotionEvent event) {
        }

        @Override
        public void onDoubleTap(MotionEvent event) {
            float currentScale = getCurrentScale();
            RectF currentRectf = getCurrentRectf();
            float x = event.getX();
            float y = event.getY();
            boolean contains = currentRectf.contains(x, y);
            if (contains) {
                if (currentScale >= MAX_SCALE) {
                    isDoubleConsume = true;
                    scaleTo(MIN_SCALE, baseRectf.centerX(), baseRectf.centerY());
                } else if (currentScale >= MIN_SCALE) {
                    isDoubleConsume = true;
                    scaleTo(MAX_SCALE, event.getX(), event.getY());
                }
            }

        }

        @Override
        public void onLongPress() {
        }

        @Override
        public void onActionUp() {
            if (isDoubleConsume) {
                isDoubleConsume = false;
                return;
            }
            //缩放检测
            float currentScale = getCurrentScale();
            RectF currentRectf = getCurrentRectf();
            if (currentScale < MIN_SCALE) {
                scaleTo(MIN_SCALE, baseRectf.centerX(), baseRectf.centerY());
                return;
            } else if (currentScale > MAX_SCALE) {
                //手势最后的FocusX和FocusY 作为缩小的中心
                scaleTo(MAX_SCALE, gestureFocusX, gestureFocusY);
                return;
            }

            float tx = 0;
            float ty = 0;

            if (currentRectf.width() > width) {
                if (currentRectf.left > 0) {
                    tx = -currentRectf.left;
                } else if (currentRectf.right < width) {
                    tx = width - currentRectf.right;
                }
            } else {
                tx = baseRectf.centerX() - currentRectf.centerX();
            }

            if (currentRectf.height() > height) {
                if (currentRectf.top > 0) {
                    ty = -currentRectf.top;
                } else if (currentRectf.bottom < height) {
                    ty = height - currentRectf.bottom;
                }
            } else {
                ty = baseRectf.centerY() - currentRectf.centerY();
            }

            if (tx != 0 || ty != 0) {
                translateTo(tx, ty);
            }
        }
    };

    /**
     * 初始化 添加手势
     */
    private void init() {
        gestureDetector = new XGestureDetector(getContext());
        gestureDetector.setOnGestureListener(onGestureListener);
        setScaleType(ScaleType.MATRIX);
    }

    public GestureImageView(Context context) {
        super(context);
        init();
    }

    public GestureImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GestureImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 起始矩阵 用于初始化矩阵位置信息
     */
    private Matrix baseMatrix = new Matrix();

    /**
     * 中间过程的变换矩阵
     */
    private Matrix changeMatrix = new Matrix();

    /**
     * 展示矩阵 用于最终的矩阵
     */
    private Matrix displayMatrix = new Matrix();

    /**
     * 临时矩阵 用于部分计算
     * 1:边界位移使用
     */
    private Matrix tempMatrix = new Matrix();

    /**
     * 默认使用矩阵作为ScaleType
     *
     * @param scaleType
     */
    @Override
    public void setScaleType(ScaleType scaleType) {
//        super.setScaleType(scaleType);
        //仅实用Matrix矩阵变换
        super.setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initSize(right - left, bottom - top);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initSize(w, h);
    }

    /**
     * 初始化Size 默认逻辑 图片居中显示
     *
     * @param width
     * @param height
     */
    private void initSize(int width, int height) {
        this.width = width;
        this.height = height;
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        float dw = drawable.getIntrinsicWidth();
        float dh = drawable.getIntrinsicHeight();
        if (dw == 0 || dh == 0) {
            return;
        }
        float scaleX = width / dw;
        float scaleY = height / dh;

        float baseScale = Math.min(scaleX, scaleY);
        float tw = baseScale * dw;
        float th = baseScale * dh;
        float dx = (width - tw) / 2;
        float dy = (height - th) / 2;

        baseMatrix.reset();
        baseMatrix.postScale(baseScale, baseScale);
        baseMatrix.postTranslate(dx, dy);
        //默认所有的相片内切在其中
        updateChange();
        baseRectf.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        baseMatrix.mapRect(baseRectf);
    }

    /**
     * 动态更新图片矩阵 更新矩阵变换
     */
    private void updateChange() {
        displayMatrix.reset();
        displayMatrix.set(baseMatrix);
        displayMatrix.postConcat(changeMatrix);
        setImageMatrix(displayMatrix);
    }

    /**
     * 获取当前缩放值
     *
     * @return
     */
    private float getCurrentScale() {
        changeMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    /**
     * 获取当前可视矩形
     *
     * @return
     */
    private RectF getCurrentRectf() {
        tempRectf.set(baseRectf);
        changeMatrix.mapRect(tempRectf);
        return tempRectf;
    }

    /**
     * Touch事件拦截
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
}
