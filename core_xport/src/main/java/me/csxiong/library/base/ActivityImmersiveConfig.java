package me.csxiong.library.base;

import android.app.Activity;

/**
 * @Desc : 一个Acitivity的配置
 * @Author : Bear - 2020/8/23
 */
public interface ActivityImmersiveConfig {

    /**
     * 在ContentView之前的配置
     */
    void beforeContentView(Activity activity);

    /**
     * 在ContentView之后的配置
     */
    void afterContentView(Activity activity);

}
