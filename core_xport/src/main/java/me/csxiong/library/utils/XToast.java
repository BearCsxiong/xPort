package me.csxiong.library.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import me.csxiong.library.R;
import me.csxiong.library.base.APP;

/**
 * @Desc : 全局Toast工具封装,内部实现暴露
 * @Author : csxiong create on 2019/7/16
 */
@SuppressLint("InflateParams")
public class XToast {

    @ColorInt
    private static final int DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF");

    @ColorInt
    private static final int ERROR_COLOR = Color.parseColor("#FD4C5B");

    @ColorInt
    private static final int INFO_COLOR = Color.parseColor("#3F51B5");

    @ColorInt
    private static final int SUCCESS_COLOR = Color.parseColor("#388E3C");

    @ColorInt
    private static final int WARNING_COLOR = Color.parseColor("#FFA900");

    private static final String TOAST_TYPEFACE = "sans-serif-condensed";

    //----------------------------show----------------------------

    public static void show(@NonNull String message) {
        show(APP.get(), message, Gravity.BOTTOM, Toast.LENGTH_SHORT, null, false);
    }

    public static void show(@NonNull String message, int gravity) {
        show(APP.get(), message, gravity, Toast.LENGTH_SHORT, null, false);
    }

    public static void show(@NonNull String message, Drawable icon) {
        show(APP.get(), message, Gravity.BOTTOM, Toast.LENGTH_SHORT, icon, true);
    }

    public static void show(@NonNull String message, int duration, Drawable icon) {
        show(APP.get(), message, Gravity.BOTTOM, duration, icon, true);
    }

    public static void show(@NonNull String message, int duration, Drawable icon, boolean withIcon) {
        custom(APP.get(), message, Gravity.BOTTOM, icon, DEFAULT_TEXT_COLOR, duration, withIcon);
    }

    public static void show(@NonNull Context context, @NonNull String message, int gravity, int duration, Drawable icon, boolean withIcon) {
        custom(context, message, gravity, icon, DEFAULT_TEXT_COLOR, duration, withIcon);
    }

    //----------------------------warning----------------------------

    public static void warning(@NonNull String message) {
        warning(APP.get(), message, Toast.LENGTH_SHORT, true);
    }

    public static void warning(@NonNull String message, int duration) {
        warning(APP.get(), message, duration, true);
    }

    public static void warning(@NonNull Context context, @NonNull String message) {
        warning(context, message, Toast.LENGTH_SHORT, true);
    }

    public static void warning(@NonNull Context context, @NonNull String message, int duration) {
        warning(context, message, duration, true);
    }

    public static void warning(@NonNull String message, int duration, boolean withIcon) {
        custom(APP.get(), message, Gravity.CENTER, getDrawable(APP.get(), R.mipmap.qmui_icon_notify_error), DEFAULT_TEXT_COLOR, WARNING_COLOR, duration, withIcon, true);
    }

    public static void warning(@NonNull Context context, @NonNull String message, int duration, boolean withIcon) {
        custom(context, message, Gravity.CENTER, getDrawable(context, R.mipmap.qmui_icon_notify_error), DEFAULT_TEXT_COLOR, WARNING_COLOR, duration, withIcon, true);
    }

    //----------------------------info----------------------------

    public static void info(@NonNull String message) {
        info(APP.get(), message, Toast.LENGTH_SHORT, true);
    }

    public static void info(@NonNull String message, int duration) {
        info(APP.get(), message, duration, true);
    }

    public static void info(@NonNull String message, int duration, boolean withIcon) {
        custom(APP.get(), message, Gravity.CENTER, getDrawable(APP.get(), R.mipmap.qmui_icon_notify_info), DEFAULT_TEXT_COLOR, INFO_COLOR, duration, withIcon, true);
    }

    public static void info(@NonNull Context context, @NonNull String message) {
        info(context, message, Toast.LENGTH_SHORT, true);
    }

    public static void info(@NonNull Context context, @NonNull String message, int duration) {
        info(context, message, duration, true);
    }

    public static void info(@NonNull Context context, @NonNull String message, int duration, boolean withIcon) {
        custom(context, message, Gravity.CENTER, getDrawable(context, R.mipmap.qmui_icon_notify_info), DEFAULT_TEXT_COLOR, INFO_COLOR, duration, withIcon, true);
    }

    //----------------------------success----------------------------

    public static void success(@NonNull String message) {
        success(APP.get(), message, Toast.LENGTH_SHORT, true);
    }

    public static void success(@NonNull String message, int duration) {
        success(APP.get(), message, duration, true);
    }

    public static void success(@NonNull String message, int duration, boolean withIcon) {
        custom(APP.get(), message, Gravity.CENTER, getDrawable(APP.get(), R.mipmap.qmui_icon_notify_done), DEFAULT_TEXT_COLOR, SUCCESS_COLOR, duration, withIcon, true);
    }

    public static void success(@NonNull Context context, @NonNull String message) {
        success(context, message, Toast.LENGTH_SHORT, true);
    }

    public static void success(@NonNull Context context, @NonNull String message, int duration) {
        success(context, message, duration, true);
    }

    public static void success(@NonNull Context context, @NonNull String message, int duration, boolean withIcon) {
        custom(context, message, Gravity.CENTER, getDrawable(context, R.mipmap.qmui_icon_notify_done), DEFAULT_TEXT_COLOR, SUCCESS_COLOR, duration, withIcon, true);
    }

    //----------------------------error----------------------------

    public static void error(@NonNull String message) {
        error(APP.get(), message, Gravity.CENTER, Toast.LENGTH_SHORT, true);
    }

    public static void error(@NonNull String message, int duration) {
        error(APP.get(), message, Gravity.CENTER, duration, true);
    }

    public static void error(@NonNull String message, int duration, boolean withIcon) {
        custom(APP.get(), message, Gravity.CENTER, getDrawable(APP.get(), R.mipmap.qmui_icon_notify_error), DEFAULT_TEXT_COLOR, ERROR_COLOR, duration, withIcon, true);
    }

    public static void error(@NonNull Context context, @NonNull String message) {
        error(context, message, Gravity.CENTER, Toast.LENGTH_SHORT, true);
    }

    public static void error(@NonNull Context context, @NonNull String message, int gravity, int duration) {
        error(context, message, gravity, duration, true);
    }

    public static void error(@NonNull Context context, @NonNull String message, int gravity, int duration, boolean withIcon) {
        custom(context, message, gravity, getDrawable(context, R.mipmap.qmui_icon_notify_error), DEFAULT_TEXT_COLOR, ERROR_COLOR, duration, withIcon, true);
    }

    public static void custom(@NonNull Context context, @NonNull String message, int gravity, Drawable icon, @ColorInt int textColor, int duration, boolean withIcon) {
        custom(context, message, gravity, icon, textColor, -1, duration, withIcon, false);
    }

    public static void custom(@NonNull Context context, @NonNull String message, int gravity, @DrawableRes int iconRes, @ColorInt int textColor, @ColorInt int tintColor, int duration, boolean withIcon, boolean shouldTint) {
        custom(context, message, gravity, getDrawable(context, iconRes), textColor, tintColor, duration, withIcon, shouldTint);
    }

    public static void custom(@NonNull Context context, @NonNull String message, int gravity, Drawable icon, @ColorInt int textColor, @ColorInt int tintColor, int duration, boolean withIcon, boolean shouldTint) {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            return;
        }
        Toast currentToast = new Toast(context);
        LinearLayout contentWrap = (LinearLayout) LayoutInflater.from(APP.get()).inflate(R.layout.x_toast_layout, null);

        ImageView imageView = contentWrap.findViewById(R.id.toast_icon);
        //添加ICON
        if (withIcon) {
            contentWrap.setBackgroundResource(R.drawable.x_tip_dialog_bg);
            contentWrap.setMinimumHeight((int) XResUtils.getXDimen(150));
            contentWrap.setMinimumWidth((int) XResUtils.getXDimen(240));
            imageView.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams imageViewLP = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(imageViewLP);
            imageViewLP.setMargins(0, XResUtils.getDimenInt(R.dimen.x20), 0, 0);
            imageView.setImageDrawable(icon);
        } else {
            imageView.setVisibility(View.GONE);
        }

        //TOAST 背景
        //仅无内容背景显示
        if (!shouldTint) {
            Drawable drawableFrame = getDrawable(context, R.drawable.x_toast_frame);
            setBackground(contentWrap, drawableFrame);
        }

        TextView toastTextView = contentWrap.findViewById(R.id.toast_text);
        if (!withIcon) {
//            toastTextView.setBackgroundResource(R.drawable.x_toast_frame);
            toastTextView.setPadding((int) XResUtils.getXDimen(38), (int) XResUtils.getXDimen(12), (int) XResUtils.getXDimen(38), (int) XResUtils.getXDimen(12));
        } else {
            toastTextView.setPadding(0, (int) XResUtils.getXDimen(15), 0, 0);
        }
        toastTextView.setEllipsize(TextUtils.TruncateAt.END);
        toastTextView.setGravity(Gravity.CENTER);
        toastTextView.setMaxLines(1);
        toastTextView.setTextColor(textColor);
        toastTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        toastTextView.setText(message);

        toastTextView.setTextColor(textColor);
        toastTextView.setText(message);
        toastTextView.setTypeface(Typeface.create(TOAST_TYPEFACE, Typeface.NORMAL));

        currentToast.setView(contentWrap);
        if (withIcon) {
            currentToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            if (gravity != Gravity.BOTTOM) {
                currentToast.setGravity(gravity, 0, 0);
            } else {
                currentToast.setGravity(Gravity.BOTTOM, 0, (int) XResUtils.getXDimen(120));
            }
        }
        currentToast.setDuration(duration);
        currentToast.show();
    }

    public static final Drawable tint9PatchDrawableFrame(@NonNull Context context, @ColorInt int tintColor) {
        final Drawable toastDrawable = getDrawable(context, R.drawable.x_toast_frame);
        toastDrawable.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
        return toastDrawable;
    }

    public static final void setBackground(@NonNull View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    public static final Drawable getDrawable(@NonNull Context context, @DrawableRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

}