package com.base.project.di.component;

import com.base.project.di.module.MainUIModule;
import com.example.login.di.LoginUIModule;

import dagger.Component;
import me.csxiong.library.base.APP;
import me.csxiong.library.di.component.AppComponent;
import me.csxiong.library.di.scope.AppScope;

/**
 * -------------------------------------------------------------------------------
 * |
 * | desc : design for dagger.android for multi module
 * |
 * |    just inject into APP UI factory injector like :Activity、Fragment、Support's component
 * |
 * |--------------------------------------------------------------------------------
 * | on 2019/4/14 created by csxiong
 * |--------------------------------------------------------------------------------
 */
@AppScope
@Component(dependencies = AppComponent.class,
        modules = {
                MainUIModule.class,//主UI模块
                LoginUIModule.class,//登录UI模块
        })
public interface UIComponent {

    void inject(APP app);

}
