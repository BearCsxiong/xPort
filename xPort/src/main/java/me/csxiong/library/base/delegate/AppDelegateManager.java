package me.csxiong.library.base.delegate;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import me.csxiong.library.R;
import me.csxiong.library.base.IApp;
import me.csxiong.library.di.component.AppComponent;
import me.csxiong.library.di.component.DaggerAppComponent;
import me.csxiong.library.di.module.AppModule;
import me.csxiong.library.di.module.ClientModule;
import me.csxiong.library.di.module.GlobalConfigModule;
import me.csxiong.library.integration.ManifestParser;
import me.csxiong.library.utils.XPreconditions;

/**
 * ---------------------------------------------------
 * |
 * |  Des : Application生命周期的管理类，管理所有注册的IAppDelegate
 * |
 * |----------------------------------------------------
 * |
 * |  Author : csxiong
 * |  Date : 2018/8/14
 * |
 * |----------------------------------------------------
 */
public class AppDelegateManager implements IApp, IAppDelegate {
    private Application mApplication;

    private AppComponent mAppComponent;

    @Inject
    @Named("FragmentLifecycle")
    Application.ActivityLifecycleCallbacks mFragmentLifecycle;

    @Inject
    @Named("ActivityLifecycle")
    Application.ActivityLifecycleCallbacks mActivityLifecycle;

    @Inject
    PrettyFormatStrategy formatStrategy;

    private List<GlobalConfig> mModules;

    private List<IAppDelegate> mIAppLifecycles = new ArrayList<>();

    private List<Application.ActivityLifecycleCallbacks> mActivityLifecycles = new ArrayList<>();

    public AppDelegateManager(@NonNull Context context) {

        //用反射, 将 AndroidManifest.xml 中带有 GlobalConfig 标签的 class 转成对象集合（List<GlobalConfig>）
        this.mModules = new ManifestParser<GlobalConfig>(context).parse(context.getResources().getString(R.string.GlobalConfig));

        //遍历之前获得的集合, 执行每一个 GlobalConfig 实现类的内部所有代理注册方法
        for (GlobalConfig module : mModules) {

            //将框架外部, 开发者实现的 Application 的生命周期回调 (IAppDelegate) 存入 mIAppLifecycles 集合 (此时还未注册回调)
            module.injectAppLifecycle(context, mIAppLifecycles);

            //将框架外部, 开发者实现的 Activity 的生命周期回调 (ActivityLifecycleCallbacks) 存入 mActivityLifecycles 集合 (此时还未注册回调)
            module.injectActivityLifecycle(context, mActivityLifecycles);

        }
    }

    @Override
    public void attachBaseContext(@NonNull Context base) {
        //遍历 mIAppLifecycles, 执行所有已注册的 IAppDelegate 的 attachBaseContext() 方法 (框架外部, 开发者扩展的逻辑)
        for (IAppDelegate lifecycle : mIAppLifecycles) {
            lifecycle.attachBaseContext(base);
        }
    }

    @Override
    public void onCreate(@NonNull Application application) {
        this.mApplication = application;
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(mApplication))
                .globalConfigModule(handlerGlobalConfigModule(GlobalConfigModule.builder()))
                .clientModule(new ClientModule())
                .build();

        mAppComponent.inject(this);

        this.mModules = null;

        //注册框架内部已实现的 Activity 生命周期逻辑 ps : 包括rxjava订阅信息处理逻辑时，界面释放，绑定在对应周期上
        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycle);

        //注册框架内部已实现的 RxLifecycle 逻辑 ps : fragment中rxjava订阅信息处理逻辑是，界面释放，绑定在对应释放周期上
        mApplication.registerActivityLifecycleCallbacks(mFragmentLifecycle);

        //注册框架外部, 开发者扩展的 Activity 生命周期逻辑
        //每个 GlobalConfig 的实现类可以声明多个 Activity 的生命周期回调
        //也可以有 N 个 GlobalConfig 的实现类 (完美支持组件化项目 各个 Module 的各种独特需求)
        for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
            mApplication.registerActivityLifecycleCallbacks(lifecycle);
        }

        //执行框架外部, 开发者扩展的 App onCreate 逻辑
        for (IAppDelegate lifecycle : mIAppLifecycles) {
            lifecycle.onCreate(mApplication);
        }

    }


    private GlobalConfigModule handlerGlobalConfigModule(GlobalConfigModule.Builder builder) {
        //遍历 GlobalConfig 集合, 给全局配置 GlobalConfigModule 添加参数
        for (GlobalConfig module : mModules) {
            module.applyOptions(mApplication, builder);
        }

        return builder.build();
    }


    @Override
    public void onTerminate(@NonNull Application application) {
        if (mActivityLifecycle != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycle);
        }
        if (mFragmentLifecycle != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mFragmentLifecycle);
        }
        if (mActivityLifecycles != null && mActivityLifecycles.size() > 0) {
            for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
                mApplication.unregisterActivityLifecycleCallbacks(lifecycle);
            }
        }
        if (mIAppLifecycles != null && mIAppLifecycles.size() > 0) {
            for (IAppDelegate lifecycle : mIAppLifecycles) {
                lifecycle.onTerminate(mApplication);
            }
        }
        this.mAppComponent = null;
        this.mActivityLifecycle = null;
        this.mFragmentLifecycle = null;
        this.mActivityLifecycles = null;
        this.mIAppLifecycles = null;
        this.mApplication = null;
    }


    @NonNull
    @Override
    public AppComponent getAppComponent() {
        XPreconditions.checkNotNull(mAppComponent,
                "%s cannot be null,first call %s#onCreate(Application) in %s#onCreate()",
                AppComponent.class.getName(), getClass().getName(), Application.class.getName());
        return mAppComponent;
    }


}

