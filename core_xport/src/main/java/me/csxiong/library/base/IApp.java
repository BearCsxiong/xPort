package me.csxiong.library.base;

import android.support.annotation.NonNull;

import me.csxiong.library.di.component.AppComponent;

/**
 * @Desc : 提供AppComponent
 * @Author : csxiong create on 2019/7/17
 */
public interface IApp {

    @NonNull
    AppComponent getAppComponent();
}
