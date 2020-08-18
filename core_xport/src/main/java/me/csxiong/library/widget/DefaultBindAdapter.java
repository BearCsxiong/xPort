package me.csxiong.library.widget;

import androidx.databinding.BindingAdapter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import me.csxiong.library.base.APP;

/**
 * @Desc : 模拟基础属性使用
 * @Author : Bear - 2020/8/18
 */
public class DefaultBindAdapter {

    @BindingAdapter(value = {"imageUrl", "placeDrawableId", "errorDrawableId"})
    public static void setImageUrl(ImageView imageView, String imageUrl, int placeDrawableId, int errorDrawableId) {

    }

    /**
     * shape {@link GradientDrawable#RECTANGLE, GradientDrawable#OVAL, GradientDrawable#LINE, GradientDrawable#RING}
     * 分别为 0，1，2，3
     *
     * @param view
     * @param solid
     */
    @BindingAdapter(value = {"shape", "solid", "strokeWidth", "strokeColor", "radius", "radiusLeftTop",
        "radiusLeftBottom", "radiusRightTop", "radiusRightBottom"}, requireAll = false)
    public static void gradientDrawable(View view, int shape, int solid, float strokeWidth, int strokeColor,
                                        float radius, float radiusLeftTop, float radiusLeftBottom, float radiusRightTop, float radiusRightBottom) {
        float[] floats;
        if (radius > 0) {
            floats = new float[] {dip2px(radius)};
        } else {
            floats = new float[] {dip2px(radiusLeftTop), dip2px(radiusLeftTop), dip2px(radiusRightTop),
                dip2px(radiusRightTop), dip2px(radiusRightBottom), dip2px(radiusRightBottom), dip2px(radiusLeftBottom),
                dip2px(radiusLeftBottom)};
        }
        view.setBackground(GradientDrawableFactory.createDrawable(shape, solid, strokeWidth, strokeColor, floats));
    }

    /**
     *  需要press 状态的背景
     * @param view
     * @param unPressDrawable
     * @param pressDrawable
     */
    @BindingAdapter(value = {"unPressDrawableId", "pressDrawableId"})
    public static void pressDrawable(View view, int unPressDrawable, int pressDrawable) {
        view.setBackground(StateListDrawableFactory.createPressDrawable(unPressDrawable, pressDrawable));
    }

    /**
     *  需要press 状态的背景
     * @param view
     * @param unPressDrawable
     * @param pressDrawable
     */
    @BindingAdapter(value = {"unPressDrawable", "pressDrawable"})
    public static void pressDrawable(View view, Drawable unPressDrawable, Drawable pressDrawable) {
        view.setBackground(StateListDrawableFactory.createPressDrawable(unPressDrawable, pressDrawable));
    }

    /**
     *  类似checkbox 场景
     * @param view
     * @param unCheckDrawable
     * @param checkDrawable
     */
    @BindingAdapter(value = {"unCheckDrawableId", "checkDrawableId"})
    public static void checkDrawable(View view, int unCheckDrawable, int checkDrawable) {
        view.setBackground(StateListDrawableFactory.createPressDrawable(unCheckDrawable, checkDrawable));
    }

    /**
     *  类似checkbox 场景
     * @param view
     * @param unCheckDrawable
     * @param checkDrawable
     */
    @BindingAdapter(value = {"unCheckDrawable", "checkDrawable"})
    public static void checkDrawable(View view, Drawable unCheckDrawable, Drawable checkDrawable) {
        view.setBackground(StateListDrawableFactory.createPressDrawable(unCheckDrawable, checkDrawable));
    }

    /**
     * 背景选中状态
     * @param view
     * @param unSelectDrawable
     * @param selectDrawable
     */
    @BindingAdapter(value = {"unSelectDrawableId", "selectDrawableId"})
    public static void selectDrawable(View view, int unSelectDrawable, int selectDrawable) {
        view.setBackground(StateListDrawableFactory.createSelectDrawable(unSelectDrawable, selectDrawable));
    }

    /**
     * 背景选中状态
     * @param view
     * @param unSelectDrawable
     * @param selectDrawable
     */
    @BindingAdapter(value = {"unSelectDrawable", "selectDrawable"})
    public static void selectDrawable(View view, Drawable unSelectDrawable, Drawable selectDrawable) {
        view.setBackground(StateListDrawableFactory.createSelectDrawable(unSelectDrawable, selectDrawable));
    }

    /**
     * text 设置删除线
     *
     * @param view
     */
    @BindingAdapter("deleteLine")
    public static void setTextViewDeleteLine(TextView view, boolean delete) {
        if (delete) {
            // 方式1
            view.setPaintFlags(view.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            // 方式2
            // view.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    /**
     * text 设置下划线
     * @param view
     */
    @BindingAdapter("underLine")
    public static void setTextViewUnderLine(TextView view, boolean drawUnderLine) {
        if (drawUnderLine) {
            // 方式1
            view.setPaintFlags(view.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            // 方式2
            // view.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    /**
     * 密度转换像素
     * @param dipValue dp值
     * @return 像素
     */
    public static int dip2px(float dipValue) {
        DisplayMetrics dm = APP.get().getResources().getDisplayMetrics();
        return (int) (dipValue * dm.density + 0.5f);
    }

    /**
     * 重新View设置宽度方法。
     * @param view
     * @param width
     */
    @BindingAdapter("android:layout_width")
    public static void setLayoutWidth(View view, float width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = (int) width;
        view.setLayoutParams(params);
    }

    /**
     * 重新View设置高度方法。
     * @param view
     * @param height
     */
    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(View view, float height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = (int) height;
        view.setLayoutParams(params);

    }

}
