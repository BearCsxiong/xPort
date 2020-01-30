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

    private float gestureFocusX;

    private float gestureFocusY;

    //-----------------动效内部值-------
    private float differScale;

    private float focusX;

    private float focusY;

    private float differTranslateX;

    private float differTranslateY;

    //-----------------动效内部值-------

    private void translateTo(float tx, float ty) {
        changeAnimator.cancel();
        //缩放无变化
        differScale = 1;
        //位移改变
        differTranslateX = tx;
        differTranslateY = ty;
        changeAnimator.start();
    }

    private void scaleTo(float scale, float px, float py) {
        float currentScale = getCurrentScale();
        float scaleRatio = scale / currentScale;
        tempMatrix.set(changeMatrix);
        tempMatrix.postScale(scaleRatio, scaleRatio, px, py);
        tempRectf.set(baseRectf);
        tempMatrix.mapRect(tempRectf);
        differScale = scaleRatio - 1;

        if (tempRectf.width() > width) {
            //边界判断
            if (tempRectf.left > 0) {
                differTranslateX = tempRectf.left;
            } else if (tempRectf.right < width) {
                differTranslateX = tempRectf.right - width;
            }
        } else {
            //中心点判断
            differTranslateX = baseRectf.centerX() - tempRectf.centerX();
        }

        differTranslateY = 0;
        if (tempRectf.height() > height) {
            if (tempRectf.top > 0) {
                differTranslateY = tempRectf.top;
            } else if (tempRectf.bottom < height) {
                differTranslateY = tempRectf.bottom - height;
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
            .duration(RESET_DURATION)
            .setAnimationListener(new XAnimator.XAnimationListener() {
                @Override
                public void onAnimationUpdate(float fraction, float value) {
                    changeMatrix.set(tempMatrix);
                    if (differScale != 1) {
                        changeMatrix.postScale(1 + differScale * fraction, 1 + differScale * fraction, focusX, focusY);
                    }
                    if (differTranslateX != 0 || differTranslateY != 0) {
                        changeMatrix.postTranslate(differTranslateX * fraction, differTranslateY * fraction);
                    }
                    updateChange();
                }

                @Override
                public void onAnimationStart(XAnimator animation) {
                    //保存当前改变的Matrix
                    tempMatrix.set(changeMatrix);
                }

                @Override
                public void onAnimationEnd(XAnimator animation) {

                }

                @Override
                public void onAnimationCancel(XAnimator animation) {

                }
            });

    private boolean isDoubleConsume;

    /**
     * 手势检测回调
     */
    private XGestureDetector.OnGestureListener onGestureListener = new XGestureDetector.OnGestureListener() {
        @Override
        public void onMultiTouchChange(boolean isMultiTouch, int touchCount) {
            Log.e(TAG, "onMultiTouchChange");
        }

        @Override
        public void onScaleStart() {
            Log.e(TAG, "onScaleStart");
            changeAnimator.cancel();
        }

        @Override
        public void onScaleEnd() {
            Log.e(TAG, "onScaleEnd");
        }

        @Override
        public void onScale(float scaleFraction, float focusX, float focusY) {
            Log.e(TAG, "onScale");
            gestureFocusX = focusX;
            gestureFocusY = focusY;
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
            Log.e(TAG, "onScroll");
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
            if (!changeAnimator.isRunning()) {
                changeMatrix.postTranslate(-distanceX, -distanceY);
                updateChange();
            }
        }

        @Override
        public void onFling(float velocityX, float velocityY) {
            Log.e(TAG, "onFling");
        }

        @Override
        public void onActionDown() {
            Log.e(TAG, "onActionDown");
            changeAnimator.cancel();
        }

        @Override
        public void onSingleTap(MotionEvent event) {
            Log.e(TAG, "onSingleTap");
        }

        @Override
        public void onDoubleTap(MotionEvent event) {
            Log.e(TAG, "onDoubleTap");
            float currentScale = getCurrentScale();
            if (currentScale >= MAX_SCALE) {
                isDoubleConsume = true;
                scaleTo(MIN_SCALE, baseRectf.centerX(), baseRectf.centerY());
            } else if (currentScale >= MIN_SCALE) {
                isDoubleConsume = true;
                scaleTo(MAX_SCALE, event.getX(), event.getY());
            }
        }

        @Override
        public void onLongPress() {
            Log.e(TAG, "onLongPress");
        }

        @Override
        public void onActionUp() {
            if (isDoubleConsume) {
                isDoubleConsume = false;
                return;
            }
            Log.e(TAG, "onActionUp");
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
