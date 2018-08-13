package me.csxiong.library.base;

import android.support.annotation.NonNull;

import me.csxiong.library.di.component.AppComponent;


/*
 * -----------------------------------------------------------------
 * Copyright by Walten, All rights reserved.
 * -----------------------------------------------------------------
 * desc：
 * -----------------------------------------------------------------
 * 2018/5/24 : Create IApp.java (Walten);
 * -----------------------------------------------------------------
 */
public interface IApp {
    @NonNull
    AppComponent getAppComponent();
}
