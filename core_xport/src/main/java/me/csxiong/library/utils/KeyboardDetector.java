package me.csxiong.library.utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import me.csxiong.library.base.APP;

/**
 * @Desc : 软键盘检测器
 * @Author : csxiong - 2019-10-17
 */
public class KeyboardDetector implements ViewTreeObserver.OnGlobalLayoutListener {

    private View rootView;

    private boolean isKeyboardOpen;

    private Rect globalVisibleRect = new Rect();

    private int keyboardHeight;

    private OnKeyboardStateListener onKeyboardStateListener;

    public KeyboardDetector(View rootView, OnKeyboardStateListener onKeyboardStateListener) {
        this.rootView = rootView;
        this.onKeyboardStateListener = onKeyboardStateListener;
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        rootView.getWindowVisibleDisplayFrame(KeyboardDetector.this.globalVisibleRect);
        int heightDiffInPx = rootView.getRootView().getHeight() - globalVisibleRect.height();
        if (heightDiffInPx > 500) {
            //软键盘打开
            if (!isKeyboardOpen) {
                keyboardHeight = heightDiffInPx;
                isKeyboardOpen = true;
                if (onKeyboardStateListener != null) {
                    onKeyboardStateListener.onKeyboardOpen(keyboardHeight);
                }
            }
        } else {
            //软键盘关闭
            if (isKeyboardOpen) {
                isKeyboardOpen = false;
                if (onKeyboardStateListener != null) {
                    onKeyboardStateListener.onKeyboardClose();
                }
            }
        }
    }

    /**
     * 获取当前软键盘状态
     *
     * @return
     */
    public boolean isKeyboardOpen() {
        return isKeyboardOpen;
    }

    /**
     * 释放资源
     */
    public void release() {
        if (rootView != null) {
            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
        onKeyboardStateListener = null;
        rootView = null;
    }

    /**
     * 键盘显示监听
     */
    public interface OnKeyboardStateListener {

        void onKeyboardOpen(int keyboardHeight);

        void onKeyboardClose();

    }

    /**
     * 显示软键盘
     *
     * @param editText
     */
    public static void showSoftKeyboard(EditText editText) {
        showSoftKeyboard(editText, 0);
    }

    /**
     * 显示软键盘
     *
     * @param editText
     * @param millis   延迟毫秒数
     */
    public static void showSoftKeyboard(EditText editText, long millis) {
        if (editText != null) {
            editText.postDelayed(() -> {
                InputMethodManager inputMethodManager = (InputMethodManager) APP.get().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(editText, 0);
            }, millis);
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param view
     */
    public static void hideSoftKeyboard(View view) {
        if (view == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) APP.get().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
