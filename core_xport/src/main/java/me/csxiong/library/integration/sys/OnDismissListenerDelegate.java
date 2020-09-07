package me.csxiong.library.integration.sys;

import android.content.DialogInterface;

import java.lang.ref.WeakReference;

/**
     * 防止内存泄漏的隐藏监听。
     */
public class OnDismissListenerDelegate implements DialogInterface.OnDismissListener {

    private WeakReference<DialogInterface.OnDismissListener> listenerWeakReference;

    public OnDismissListenerDelegate(DialogInterface.OnDismissListener listener) {
        listenerWeakReference = new WeakReference<>(listener);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        DialogInterface.OnDismissListener dismissListener = listenerWeakReference.get();
        if (dismissListener != null) {
            dismissListener.onDismiss(dialog);
        }
    }
}
