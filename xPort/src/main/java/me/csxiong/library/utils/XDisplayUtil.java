package me.csxiong.library.utils;

import android.content.Context;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;


/**
 *
 * @author kehua
 * @date 2016-03-17
 */
public class XDisplayUtil {

    public static float dpToPx(Context context,float dp) {
        if (context == null) {
            return -1;
        }
        return dp * getDensity(context);
    }

    public static float pxToDp(Context context,int px) {
        if (context == null) {
            return -1;
        }
        return px / getDensity(context);
    }

    public static int dpToPxInt(Context context,float dp) {
        return (int)(dpToPx(context,dp) + 0.5f);
    }

    public static int pxToDpInt(Context context,int px) {
        return (int)(pxToDp(context,px) + 0.5f);
    }

    public static float spToPx(Context context,float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static float pxToSp(Context context, int px) {
        if (context == null) {
            return -1;
        }
        return px / context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static int spToPxInt(Context context,float dp) {
        return (int)(spToPx(context,dp) + 0.5f);
    }

    public static int pxToSpInt(Context context,int px) {
        return (int)(pxToSp(context,px) + 0.5f);
    }

    /**
     * 屏幕密度
     */
    public static float sDensity = 0f;
    public static float getDensity(Context context){
        if(sDensity == 0f){
            sDensity = getDisplayMetrics(context).density;
        }
        return sDensity;
    }

    /**
     * DisplayMetrics
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    /**
     * 获取屏幕大小
     *
     * @param context
     * @return
     */
    public static int[] getScreenPixelSize(Context context) {
        DisplayMetrics metrics = getDisplayMetrics(context);
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }

    public static void hideSoftInputKeyBoard(Context context, View focusView) {
        if (focusView != null) {
            IBinder binder = focusView.getWindowToken();
            if (binder != null) {
                InputMethodManager imd = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imd.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }
    }

    public static void showSoftInputKeyBoard(Context context, View focusView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(focusView, InputMethodManager.SHOW_FORCED);
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }
}
