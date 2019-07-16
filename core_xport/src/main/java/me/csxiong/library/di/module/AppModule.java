package me.csxiong.library.di.module;


import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.csxiong.library.integration.ApiProvider;

/**
 * @Desc : 主App模块 提供部分拓展参数的注入
 * @Author : csxiong create on 2019/7/16
 */
@Module
public class AppModule {

    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplicationContext() {
        return application;
    }

    /**
     * 提供ApiService
     *
     * @param provider
     * @return
     */
    @Provides
    @Singleton
    ApiProvider provideServiceProvider(ApiProvider provider) {
        return provider;
    }
}
