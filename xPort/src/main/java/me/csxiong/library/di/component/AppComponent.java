package me.csxiong.library.di.component;

import android.app.Application;

import java.io.File;
import java.util.concurrent.ExecutorService;

import javax.inject.Singleton;

import dagger.Component;
import io.reactivex.Scheduler;
import me.csxiong.library.base.delegate.AppDelegateManager;
import me.csxiong.library.di.module.AppModule;
import me.csxiong.library.di.module.ClientModule;
import me.csxiong.library.di.module.GlobalConfigModule;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Desc : main component for other components
 * Author : csxiong - 2019/7/15
 */
@Singleton
@Component(modules = {AppModule.class, ClientModule.class, GlobalConfigModule.class})
public interface AppComponent {

    /**
     * APP
     *
     * @return
     */
    Application application();

    /**
     * provide retrofit for Rxjava2(default)
     *
     * @return
     */
    Retrofit retrofit();

    /**
     * cache file
     *
     * @return File
     */
    File cacheFile();

    /**
     * provider for httpConnect
     *
     * @return
     */
    OkHttpClient provideHttpClient();

    /**
     * provide base scheduler for RxJava
     *
     * @return
     */
    Scheduler provideScheduler();

    /**
     * provide thread pool service for
     *
     * @return
     */
    ExecutorService provideExcutorService();

    /**
     * 注入到APP代理
     *
     * @param appDelegate
     */
    void inject(AppDelegateManager appDelegate);

}