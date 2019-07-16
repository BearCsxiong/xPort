package com.base.project.di;

import com.example.login.di.LoginUIModule;

import dagger.Component;
import me.csxiong.library.base.APP;
import me.csxiong.library.di.component.AppComponent;
import me.csxiong.library.di.scope.AppScope;

/**
 * @Desc : 基础组件Component
 * @Author : csxiong create on 2019/7/17
 */
@AppScope
@Component(dependencies = AppComponent.class,
        modules = {
                MainUIModule.class,
                LoginUIModule.class,
        })
public interface UIComponent {

    /**
     * 注入APP 提供注入
     *
     * @param app
     */
    void inject(APP app);

}
