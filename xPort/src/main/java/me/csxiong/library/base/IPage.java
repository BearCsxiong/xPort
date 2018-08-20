package me.csxiong.library.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.csxiong.library.di.component.AppComponent;


/**-------------------------------------------------------------------------------
*| 
*| desc : 基本页面提供方法,规范代码编写
*| 
*|--------------------------------------------------------------------------------
*| on 2018/8/14 created by csxiong 
*|--------------------------------------------------------------------------------
*/
public interface IPage {

    int getLayoutResId();

    void initUI(@Nullable Bundle savedInstanceState);

    void initData(@Nullable Bundle savedInstanceState);

    void initInject();

    AppComponent getAppComponent();

}
