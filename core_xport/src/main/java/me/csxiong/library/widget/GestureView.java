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
 * @Desc : 测试手势View
 * @Author : csxiong - 2020-01-28
 */
public class GestureView extends AppCompatImageView {

    private static final long RESET_DURATION = 300l;

    private static final float SCALE_DAMP = 1.5f;

    private static final float TRANSLATE_DAMP = 3f;

    private static final float MIN_SCALE = 1.0f;

    private static final float MAX_SCALE = 4.0f;

    private final static String TAG = "GestureView";

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

    //-----------------动效内部值-------
    private float startScale;

    private float differScale;

    private float differCenterX;

    private float differCenterY;

    private float differTranslateX;

    private float differTranslateY;
    //-----------------动效内部值-------

    /**
     * 重置位移动效
     */
    private XAnimator resetTranslateAnimator = XAnimator.ofFloat(0, 1)
            .duration(RESET_DURATION)
            .setAnimationListener(new XAnimator.XAnimationListener() {
                @Override
                public void onAnimationUpdate(float fraction, float value) {
                    changeMatrix.set(tempMatrix);
                    changeMatrix.postTranslate(-fraction * differTranslateX, -fraction * differTranslateY);
                    updateChange();
                }

                @Override
                public void onAnimationStart(XAnimator animation) {
                    RectF currentRectf = getCurrentRectf();
                    //记录当前matrix
                    tempMatrix.set(changeMatrix);
                    differTranslateX = 0;
                    differTranslateY = 0;

                    //这边属于逻辑问题 需要中心点和边界分开处理
                    if (currentRectf.width() > width) {
                        if (currentRectf.left > 0) {
                            differTranslateX = currentRectf.left - 0;
                        } else if (currentRectf.right < width) {
                            differTranslateX = currentRectf.right - width;
                        }
                    } else {
                        differTranslateX = currentRectf.centerX() - baseRectf.centerX();
                    }

                    if (currentRectf.height() > height) {
                        if (currentRectf.top > 0) {
                            differTranslateY = currentRectf.top - 0;
                        } else if (currentRectf.bottom < height) {
                            differTranslateY = currentRectf.bottom - height;
                        }
                    } else {
                        differTranslateY = currentRectf.centerY() - baseRectf.centerY();
                    }
                }

                @Override
                public void onAnimationEnd(XAnimator animation) {

                }

                @Override
                public void onAnimationCancel(XAnimator animation) {

                }
            });

    /**
     * 重置缩放动效
     */
    private XAnimator resetScaleAnimator = XAnimator.ofFloat(0, 1)
            .duration(RESET_DURATION)
            .setAnimationListener(new XAnimator.XAnimationListener() {
                @Override
                public void onAnimationUpdate(float fraction, float value) {
                    float scale = startScale + fraction * differScale;
                    changeMatrix.reset();
                    changeMatrix.postScale(scale, scale, baseRectf.centerX(), baseRectf.centerY());
                    changeMatrix.postTranslate(-(1 - fraction) * differCenterX, -(1 - fraction) * differCenterY);
                    updateChange();
                }

                @Override
                public void onAnimationStart(XAnimator animation) {
                    startScale = getCurrentScale();
                    RectF rectf = getCurrentRectf();
                    differCenterX = width / 2f - rectf.centerX();
                    differCenterY = height / 2f - rectf.centerY();
                    if (startScale < MIN_SCALE) {
                        differScale = MIN_SCALE - startScale;
                    } else if (startScale > MAX_SCALE) {
                        differScale = MAX_SCALE - startScale;
                    }

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
            resetScaleAnimator.cancel();
            resetScaleAnimator.cancel();
        }

        @Override
        public void onScaleEnd() {
        }

        @Override
        public void onScale(float scaleFraction, float focusX, float focusY) {
            float currentScale = getCurrentScale();
            //阻尼保护
            if (currentScale < MIN_SCALE && scaleFraction < 1) {
                scaleFraction = (float) (1 - Math.pow(1 - scaleFraction, SCALE_DAMP));
            } else if (currentScale > MAX_SCALE && scaleFraction > 1) {
                scaleFraction = (float) (1 + Math.pow(scaleFraction - 1, SCALE_DAMP));
            }
            changeMatrix.postScale(scaleFraction, scaleFraction, focusX, focusY);
            updateChange();
        }

        @Override
        public void onScroll(float distanceX, float distanceY) {
            RectF currentRectf = getCurrentRectf();
            boolean isDampX = currentRectf.left >= 0 || currentRectf.right <= width;
            boolean isDampY = currentRectf.top >= 0 || currentRectf.bottom <= height;
            if (isDampX) {
                distanceX /= TRANSLATE_DAMP;
            }
            if (isDampY) {
                distanceY /= TRANSLATE_DAMP;
            }
            //防止在重置时触发滚动
            if (!resetScaleAnimator.isRunning()) {
                changeMatrix.postTranslate(-distanceX, -distanceY);
                updateChange();
            }
        }

        @Override
        public void onFling(float velocityX, float velocityY) {
        }

        @Override
        public void onActionDown() {
            resetTranslateAnimator.cancel();
            resetScaleAnimator.cancel();
        }

        @Override
        public void onSingleTap(MotionEvent event) {
        }

        @Override
        public void onDoubleTap(MotionEvent event) {
        }

        @Override
        public void onLongPress() {
        }

        @Override
        public void onActionUp() {
            //缩放检测
            float currentScale = getCurrentScale();
            boolean isResetScale = currentScale < MIN_SCALE
                    || currentScale > MAX_SCALE;
            if (isResetScale) {
                //Tips:内部含中心位移 不需要位移检测
                resetScaleAnimator.cancel();
                resetScaleAnimator.start();
                return;
            }
            //位移检测
            RectF rectf = getCurrentRectf();
            boolean isResetTranslate = rectf.left > 0
                    || rectf.top > 0
                    || rectf.right < width
                    || rectf.bottom < height;
            if (isResetTranslate) {
                resetTranslateAnimator.cancel();
                resetTranslateAnimator.start();
            }
        }
    };

    private void init() {
        gestureDetector = new XGestureDetector(getContext());
        gestureDetector.setOnGestureListener(onGestureListener);
        setScaleType(ScaleType.MATRIX);
    }

    public GestureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GestureView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        Log.e(TAG, baseRectf.toShortString());
    }

    private void updateChange() {
        displayMatrix.reset();
        displayMatrix.set(baseMatrix);
        displayMatrix.postConcat(changeMatrix);
        setImageMatrix(displayMatrix);
    }

    private float getTranslateX() {
        changeMatrix.getValues(values);
        return values[Matrix.MTRANS_X];
    }

    private float getTranslateY() {
        changeMatrix.getValues(values);
        return values[Matrix.MTRANS_Y];
    }

    private float getCenterX() {
        changeMatrix.mapRect(tempRectf);
        return tempRectf.centerX();
    }

    private float getCenterY() {
        changeMatrix.mapRect(tempRectf);
        return tempRectf.centerY();
    }

    private float getCurrentScale() {
        changeMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    private RectF getCurrentRectf() {
        tempRectf.set(baseRectf);
        changeMatrix.mapRect(tempRectf);
        return tempRectf;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
}
