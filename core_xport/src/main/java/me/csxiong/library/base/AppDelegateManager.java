package me.csxiong.library.base;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import me.csxiong.library.R;
import me.csxiong.library.di.component.AppComponent;
import me.csxiong.library.di.component.DaggerAppComponent;
import me.csxiong.library.di.module.AppModule;
import me.csxiong.library.di.module.ClientModule;
import me.csxiong.library.di.module.GlobalConfigModule;
import me.csxiong.library.integration.ManifestParser;
import me.csxiong.library.utils.XPreconditions;

/**
 * @Desc : Application代理管理器,管理所有注入GlobalConfig的配置
 * @Author : csxiong create on 2019/7/17
 */
public class AppDelegateManager implements IApp, IAppDelegate {

    /**
     * 当前Application
     */
    private Application mApplication;

    /**
     * 当前Application注入器
     */
    private AppComponent mAppComponent;

    @Inject
    PrettyFormatStrategy formatStrategy;

    /**
     * module 集合
     */
    private List<GlobalConfig> mModules;

    /**
     * AppLifecycle 集合
     */
    private List<IAppDelegate> mIAppLifecycles = new ArrayList<>();

    /**
     * ActivityLifecycle 集合
     */
    private List<Application.ActivityLifecycleCallbacks> mActivityLifecycles = new ArrayList<>();

    /**
     * 构造
     *
     * @param context
     */
    public AppDelegateManager(@NonNull Context context) {
        this.mModules = new ManifestParser<GlobalConfig>(context).parse(context.getResources().getString(R.string.GlobalConfig));
        for (GlobalConfig module : mModules) {
            module.injectAppLifecycle(context, mIAppLifecycles);
            module.injectActivityLifecycle(context, mActivityLifecycles);
        }
    }

    @Override
    public void attachBaseContext(@NonNull Context base) {
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
        for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
            mApplication.registerActivityLifecycleCallbacks(lifecycle);
        }
        for (IAppDelegate lifecycle : mIAppLifecycles) {
            lifecycle.onCreate(mApplication);
        }
    }

    private GlobalConfigModule handlerGlobalConfigModule(GlobalConfigModule.Builder builder) {
        for (GlobalConfig module : mModules) {
            module.applyOptions(mApplication, builder);
        }
        return builder.build();
    }

    @Override
    public void onTerminate(@NonNull Application application) {
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

