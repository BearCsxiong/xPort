package me.csxiong.library.integration.sys;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Desc : 防止监听导致在Dalvik环境下出现内存泄漏问题的Dialog封装。https://blog.csdn.net/u012403246/article/details/48222753
 * @Author : Bear - 2020/9/7
 */
public class NoLeakDialog extends Dialog {
    private OnShowListener onShowListener;
    private OnDismissListener onDismissListener;
    private OnCancelListener onCancelListener;

    public NoLeakDialog(@NonNull Context context) {
        super(context);
    }

    public NoLeakDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void setOnDismissListener(@Nullable DialogInterface.OnDismissListener listener) {
        onDismissListener = listener;
        if (onDismissListener != null) {
            super.setOnDismissListener(new OnDismissListenerDelegate(listener));
        } else {
            super.setOnDismissListener(null);
        }
    }

    public OnDismissListener getOnDismissListener() {
        return onDismissListener;
    }

    @Override
    public void setOnCancelListener(@Nullable DialogInterface.OnCancelListener listener) {
        onCancelListener = listener;
        if (onCancelListener != null) {
            super.setOnCancelListener(new OnCancelListenerDelegate(listener));
        } else {
            super.setOnCancelListener(null);
        }
    }

    public OnCancelListener getOnCancelListener() {
        return onCancelListener;
    }

    @Override
    public void setOnShowListener(@Nullable DialogInterface.OnShowListener listener) {
        onShowListener = listener;
        if (onShowListener != null) {
            super.setOnShowListener(new OnShowListenerDelegate(listener));
        } else {
            super.setOnShowListener(null);
        }
    }

    public OnShowListener getOnShowListener() {
        return onShowListener;
    }


}
