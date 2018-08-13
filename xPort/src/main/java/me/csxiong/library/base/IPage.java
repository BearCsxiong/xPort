package me.csxiong.library.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.csxiong.library.di.component.AppComponent;


/*
 * -----------------------------------------------------------------
 * Copyright by Walten, All rights reserved.
 * -----------------------------------------------------------------
 * desc：
 * -----------------------------------------------------------------
 * 2018/5/24 : Create IPage.java (Walten);
 * -----------------------------------------------------------------
 */
public interface IPage {

    int getLayoutResId();

    void initUI(@Nullable Bundle savedInstanceState);

    void initData(@Nullable Bundle savedInstanceState);

    void initInject();

    AppComponent getAppComponent();

}
