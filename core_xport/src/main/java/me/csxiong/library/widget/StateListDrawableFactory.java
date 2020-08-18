package me.csxiong.library.widget;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import me.csxiong.library.base.APP;


public class StateListDrawableFactory {
    /**
     * 从 drawable 获取图片 id 给 Imageview 添加 selector
     */
    public static StateListDrawable createPressDrawable(int unPress, int pressed){
        StateListDrawable drawable = new StateListDrawable();
        Drawable normal = APP.get().getResources().getDrawable(unPress);
        Drawable press = APP.get().getResources().getDrawable(pressed);
        drawable.addState(new int[]{android.R.attr.state_pressed},press);
        drawable.addState(new int[]{-android.R.attr.state_pressed},normal);
        return drawable;
    }
    /**
     * 从 drawable 获取图片 id 给 Imageview 添加 selector
     */
    public static StateListDrawable createPressDrawable(Drawable unPress, Drawable pressed){
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed},pressed);
        drawable.addState(new int[]{-android.R.attr.state_pressed},unPress);
        return drawable;
    }

    /**
     *  checkedDrawable
     * @param unCheck
     * @param check
     * @return
     */
    public static StateListDrawable createCheckDrawable(int unCheck, int check){
        StateListDrawable drawable = new StateListDrawable();
        Drawable normal = APP.get().getResources().getDrawable(unCheck);
        Drawable press = APP.get().getResources().getDrawable(check);
        drawable.addState(new int[]{android.R.attr.state_checked},press);
        drawable.addState(new int[]{-android.R.attr.state_checked},normal);
        return drawable;
    }
    /**
     *  checkedDrawable
     * @param unCheck
     * @param check
     * @return
     */
    public static StateListDrawable createCheckDrawable(Drawable unCheck, Drawable check){
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_checked},check);
        drawable.addState(new int[]{-android.R.attr.state_checked},unCheck);
        return drawable;
    }

    /**
     *  selectDrawable
     * @param unSelect
     * @param select
     * @return
     */
    public static StateListDrawable createSelectDrawable(int unSelect, int select){
        StateListDrawable drawable = new StateListDrawable();
        Drawable normal = APP.get().getResources().getDrawable(unSelect);
        Drawable press = APP.get().getResources().getDrawable(select);
        drawable.addState(new int[]{android.R.attr.state_selected},press);
        drawable.addState(new int[]{-android.R.attr.state_selected},normal);
        return drawable;
    }
    /**
     *  selectDrawable
     * @param unSelect
     * @param select
     * @return
     */
    public static StateListDrawable createSelectDrawable(Drawable unSelect, Drawable select){
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_selected},select);
        drawable.addState(new int[]{-android.R.attr.state_selected},unSelect);
        return drawable;
    }

}
