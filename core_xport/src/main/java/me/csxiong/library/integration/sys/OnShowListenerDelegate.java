package me.csxiong.library.integration.sys;

import android.content.DialogInterface;

import java.lang.ref.WeakReference;

/**
     * 防止内存泄漏的展示监听。
     */
public class OnShowListenerDelegate implements DialogInterface.OnShowListener {

    private WeakReference<DialogInterface.OnShowListener> listenerWeakReference;

    public OnShowListenerDelegate(DialogInterface.OnShowListener listener) {
        listenerWeakReference = new WeakReference<>(listener);
    }

    @Override
    public void onShow(DialogInterface dialog) {
        DialogInterface.OnShowListener showListener = listenerWeakReference.get();
        if (showListener != null) {
            showListener.onShow(dialog);
        }
    }
}
