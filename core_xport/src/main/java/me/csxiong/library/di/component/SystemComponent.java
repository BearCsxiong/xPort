package me.csxiong.library.di.component;

import dagger.Component;
import me.csxiong.library.di.scope.AppScope;
import me.csxiong.library.utils.ThreadExecutor;

/**
 * @Desc : 部分XPort系统组件提供注入
 * @Author : csxiong create on 2019/7/16
 */
@AppScope
@Component(dependencies = AppComponent.class)
public interface SystemComponent {

    void inject(ThreadExecutor threadExecutors);
}
