package me.csxiong.library.di.component;

import android.app.Application;

import me.csxiong.library.base.delegate.AppDelegateManager;
import me.csxiong.library.di.module.AppModule;
import me.csxiong.library.di.module.ClientModule;
import me.csxiong.library.di.module.GlobalConfigModule;

import java.io.File;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by csxiong on 2018/5/23.
 */
@Singleton
@Component(modules = {AppModule.class, ClientModule.class, GlobalConfigModule.class})
public interface AppComponent {

    /**
     * 应用实例
     *
     * @return
     */
    Application application();

    /**
     * 网络请求客户端
     *
     * @return
     */
    Retrofit retrofit();

    /**
     * 缓存文件
     *
     * @return File
     */
    File cacheFile();

    /**
     * 注入到APP代理
     *
     * @param appDelegate
     */
    void inject(AppDelegateManager appDelegate);

}