package me.csxiong.library.integration.sys;

import android.content.DialogInterface;

import java.lang.ref.WeakReference;

public class OnCancelListenerDelegate implements DialogInterface.OnCancelListener {

        private WeakReference<DialogInterface.OnCancelListener> listenerWeakReference;

        public OnCancelListenerDelegate(DialogInterface.OnCancelListener listener) {
            listenerWeakReference = new WeakReference<>(listener);
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            DialogInterface.OnCancelListener cancelListener = listenerWeakReference.get();
            if (cancelListener != null) {
                cancelListener.onCancel(dialog);
            }
        }
    }