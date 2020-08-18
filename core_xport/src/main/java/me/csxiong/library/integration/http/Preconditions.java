package me.csxiong.library.integration.http;

import android.support.multidex.BuildConfig;
import android.text.TextUtils;
import android.util.Log;


/**
 * Desc : 前置条件检查
 * Author : csxiong - 2019/6/24
 */
public class Preconditions {

    public static final String TAG = "XHttp";

    /**
     * 日志输出
     *
     * @param message
     */
    public static void e(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        if (BuildConfig.DEBUG) {
            Log.e(TAG, message);
        }
    }

    /**
     * throw out
     *
     * @param message
     */
    public static void error(String message) {
        if (BuildConfig.DEBUG) {
            throw new IllegalStateException(TAG + "\n:\n" + message);
        }
    }

    public static void checkNotNull(Object o) {
        if (null == o) {
            throw new NullPointerException("not be a null object");
        }
    }

    public static void checkNotNull(String s) {
        if (s == null || "".equals(s)) {
            throw new NullPointerException("ensure not be a null or empty string");
        }
    }
}
