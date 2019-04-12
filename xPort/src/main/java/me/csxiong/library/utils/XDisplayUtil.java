package me.csxiong.library.utils;

import android.content.Context;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

import me.csxiong.library.base.APP;


/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : dp、px等转换工具,显示型参数数据获取
 * |
 * |--------------------------------------------------------------------------------
 * | on 2018/9/28 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public class XDisplayUtil {

    private XDisplayUtil() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    public static float dpToPx(float dp) {
        return dp * getDensity();
    }

    public static float pxToDp(int px) {
        return px / getDensity();
    }

    public static int dpToPxInt(float dp) {
        return (int) (dpToPx(dp) + 0.5f);
    }

    public static int pxToDpInt(int px) {
        return (int) (pxToDp(px) + 0.5f);
    }

    public static float spToPx(float dp) {
        return dp * APP.getInstance().getResources().getDisplayMetrics().scaledDensity;
    }

    public static float pxToSp(int px) {
        return px / APP.getInstance().getResources().getDisplayMetrics().scaledDensity;
    }

    public static int spToPxInt(float dp) {
        return (int) (spToPx(dp) + 0.5f);
    }

    public static int pxToSpInt(int px) {
        return (int) (pxToSp(px) + 0.5f);
    }

    /**
     * 屏幕密度
     */
    public static float sDensity = 0f;

    public static float getDensity() {
        if (sDensity == 0f) {
            sDensity = getDisplayMetrics().density;
        }
        return sDensity;
    }

    /**
     * DisplayMetrics
     *
     * @return
     */
    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) APP.getInstance().getApplicationContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    /**
     * 获取屏幕大小
     *
     * @return
     */
    public static int[] getScreenPixelSize() {
        DisplayMetrics metrics = getDisplayMetrics();
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }

    public static void hideSoftInputKeyBoard(View focusView) {
        if (focusView != null) {
            IBinder binder = focusView.getWindowToken();
            if (binder != null) {
                InputMethodManager imd = (InputMethodManager) APP.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
                imd.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }
    }

    public static void showSoftInputKeyBoard(View focusView) {
        InputMethodManager imm = (InputMethodManager) APP.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(focusView, InputMethodManager.SHOW_FORCED);
    }

    public static int getScreenWidth() {
        return APP.getInstance().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return APP.getInstance().getResources().getDisplayMetrics().heightPixels;
    }

    public static int getStatusBarHeight() {
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = APP.getInstance().getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
}