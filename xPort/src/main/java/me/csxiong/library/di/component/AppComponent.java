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
 * @Desc : 主要的注入器,提供各个模块默认依赖的注入器,使用者可独立再编写注入器
 * @Author : csxiong create on 2019/7/16
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
     * 默认内部使用提供Retrofit实例
     *
     * @return
     */
    Retrofit retrofit();

    /**
     * 默认提供缓存文件
     *
     * @return
     */
    @Deprecated
    File cacheFile();

    /**
     * 默认提供OKHttp客户端
     *
     * @return
     */
    OkHttpClient provideOkHttpClient();

    /**
     * 默认提供线程调度器{@link me.csxiong.library.utils.RxUtils}和{@link io.reactivex.android.schedulers.AndroidSchedulers}
     * 提供Rxjava使用
     *
     * @return
     */
    Scheduler provideScheduler();

    /**
     * 全局线程池
     * TODO 在长连接和短连接需要区分线程池 可优化
     *
     * @return
     */
    ExecutorService provideExcutorService();

    /**
     * 注入到APP代理
     *
     * @param appDelegate 需要注入的对象
     */
    void inject(AppDelegateManager appDelegate);

}