package me.csxiong.library.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import java.lang.reflect.Field;

import me.csxiong.library.R;
import me.csxiong.library.base.APP;
import me.csxiong.library.widget.GradientDrawableFactory;

/**
 * @Desc : 全局Toast工具封装,内部实现暴露
 * @Author : csxiong create on 2019/7/16
 *
 * 提供部分BugFix
 */
public class XToast {

    private static Toast sToast;
    private static boolean isJumpWhenMore = true;
    private static Field sField_TN;
    private static Field sField_TN_Handler;

    static {
        try {
            sField_TN = Toast.class.getDeclaredField("mTN");
            sField_TN.setAccessible(true);
            sField_TN_Handler = sField_TN.getType().getDeclaredField("mHandler");
            sField_TN_Handler.setAccessible(true);
        } catch (Throwable e) {

        }
    }

    @ColorInt
    private static final int DEFAULT_TEXT_COLOR = 0xffffffff;

    @ColorInt
    public static final int DEFAULT_COLOR = 0xB2323232;

    @ColorInt
    private static final int ERROR_COLOR = 0xffFD4C5B;

    @ColorInt
    private static final int INFO_COLOR = 0xff3F51B5;

    @ColorInt
    private static final int SUCCESS_COLOR = 0xff388E3C;

    @ColorInt
    private static final int WARNING_COLOR = 0xffFFA900;

    //----------------------------show----------------------------

    public static void show(@NonNull String message) {
        show(APP.get(), message, Gravity.CENTER, Toast.LENGTH_SHORT, null);
    }

    public static void show(@NonNull Context context, @NonNull String message, int gravity, int duration, Drawable icon) {
        custom(context, message, gravity, icon, DEFAULT_TEXT_COLOR,DEFAULT_COLOR, duration);
    }

    //----------------------------warning----------------------------

    public static void warning(@NonNull String message) {
        warning(APP.get(), message, Toast.LENGTH_SHORT);
    }

    public static void warning(@NonNull Context context, @NonNull String message, int duration) {
        custom(context, message, Gravity.CENTER, getDrawable(context, R.mipmap.ic_warning), DEFAULT_TEXT_COLOR, WARNING_COLOR, duration);
    }

    //----------------------------info----------------------------

    public static void info(@NonNull String message) {
        info(APP.get(), message, Toast.LENGTH_SHORT);
    }

    public static void info(@NonNull Context context, @NonNull String message, int duration) {
        custom(context, message, Gravity.CENTER, getDrawable(context, R.mipmap.ic_info), DEFAULT_TEXT_COLOR, INFO_COLOR, duration);
    }

    //----------------------------success----------------------------

    public static void success(@NonNull String message) {
        success(APP.get(), message, Toast.LENGTH_SHORT);
    }

    public static void success(@NonNull Context context, @NonNull String message, int duration) {
        custom(context, message, Gravity.CENTER, getDrawable(context, R.mipmap.ic_success), DEFAULT_TEXT_COLOR, SUCCESS_COLOR, duration);
    }

    //----------------------------error----------------------------

    public static void error(@NonNull String message) {
        error(APP.get(), message, Gravity.CENTER, Toast.LENGTH_SHORT, true);
    }

    public static void error(@NonNull Context context, @NonNull String message, int gravity, int duration, boolean withIcon) {
        custom(context, message, gravity, getDrawable(context, R.mipmap.ic_error), DEFAULT_TEXT_COLOR, ERROR_COLOR, duration);
    }


    public static void custom(@NonNull Context context, @NonNull String message, int gravity, Drawable icon, @ColorInt int textColor, int duration) {
        custom(context, message, gravity, icon, textColor, -1, duration);
    }

    public static void custom(@NonNull Context context, @NonNull String message, int gravity, @DrawableRes int iconRes, @ColorInt int textColor, @ColorInt int tintColor, int duration) {
        custom(context, message, gravity, getDrawable(context, iconRes), textColor, tintColor, duration);
    }

    public static void custom(@NonNull Context context, @NonNull String message, int gravity, Drawable icon, @ColorInt int textColor, @ColorInt int tintColor, int duration) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            return;
        }
        if (isJumpWhenMore) {
            cancelToast();
        }
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        if (sToast == null) {
            sToast = Toast.makeText(context, null, duration);
            try {
                sToast.getView().setBackground(new ColorDrawable(Color.TRANSPARENT));
                TextView textView = (TextView) ((ViewGroup) sToast.getView()).getChildAt(0);
                textView.setText(message);
                textView.setTextColor(Color.WHITE);
                if (icon != null) {
                    textView.setPadding((int) (12 * dm.density), (int) (5 * dm.density), (int) (16 * dm.density),
                            (int) (6 * dm.density));
                    icon.setBounds(0, 0, XDisplayUtil.dpToPxInt(18), XDisplayUtil.dpToPxInt(18));
                    textView.setCompoundDrawables(icon, null, null, null);
                    textView.setCompoundDrawablePadding(XDisplayUtil.dpToPxInt(5));
                    textView.setBackground(GradientDrawableFactory.createDrawable(tintColor, XDisplayUtil.dpToPx(40)));
                } else {
                    textView.setBackground(GradientDrawableFactory.createDrawable(tintColor, XDisplayUtil.dpToPx(5)));
                    textView.setPadding((int) (14 * dm.density), (int) (5 * dm.density), (int) (14 * dm.density),
                            (int) (6 * dm.density));
                }
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
                sToast.getView().setPadding(0, 0, 0, 0);
                layoutParams.bottomMargin = 0;
                layoutParams.leftMargin = (int) (15 * dm.density);
                layoutParams.rightMargin = (int) (15 * dm.density);
                layoutParams.topMargin = 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (sToast.getView() != null) {
                TextView textView = (TextView) ((ViewGroup) sToast.getView()).getChildAt(0);
                textView.setText(message);
            }
        }
        sToast.setDuration(duration);
        if (gravity == Gravity.CENTER) {
            sToast.setGravity(gravity, 0, 0);
        } else {
            sToast.setGravity(gravity, 0, (int) (24 * dm.density + 0.5f));
        }
        hook(sToast);
        sToast.show();
    }

    public static final Drawable getDrawable(@NonNull Context context, @DrawableRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    private static void hook(Toast toast) {
        try {
            Object tn = sField_TN.get(toast);
            Handler preHandler = (Handler) sField_TN_Handler.get(tn);
            sField_TN_Handler.set(tn, new SafelyHandlerWrapper(preHandler));
        } catch (Throwable e) {
        }
    }

    private static class SafelyHandlerWrapper extends Handler {

        private Handler impl;

        public SafelyHandlerWrapper(Handler impl) {
            this.impl = impl;
        }

        @Override
        public void dispatchMessage(Message msg) {
            try {
                super.dispatchMessage(msg);
            } catch (Exception e) {
            }
        }

        @Override
        public void handleMessage(Message msg) {
            impl.handleMessage(msg);// 需要委托给原Handler执行
        }
    }

    /**
     * 取消吐司显示
     */
    public static void cancelToast() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }

}