package me.csxiong.library.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.csxiong.library.di.component.AppComponent;


/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : default method declare to override for build Page UI
 * |
 * |--------------------------------------------------------------------------------
 * | on 2018/8/14 created by csxiong
 * |--------------------------------------------------------------------------------
 */
public interface IPage {

    int getLayoutResId();

    void initUI(@Nullable Bundle savedInstanceState);

    void initData(@Nullable Bundle savedInstanceState);

    AppComponent getAppComponent();

}
